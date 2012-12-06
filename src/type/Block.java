package type;

import java.util.ArrayList;
import java.util.List;

import kit.Kit;

public class Block {
	private Buffer buffer = null;
	private int length = 0;
	private List<Fragment> fragList = new ArrayList<Fragment>();
	private int rate = 0;
	private double playtime = 0;
	private Schedule sch = null;
	private int serverSize = 0;
	private int allocFrag = 0;
	private ServerList slist = null;
	private int fragNum = 0;
	private int maxLengthBlock = 0;
	private int selectID = 0;
	private double startTime = 0;
	private List<Block> bList = null;
	private double qmax, qmin;
	private VideoRateList rList;
	private int id = 0;
	private Timeline tline;
	
	public Block(Buffer buf, VideoRateList rateList, ServerList s, int max, int selectID,List<Block> st, Timeline tl)
	{
		this.buffer = buf;
		this.slist = s;
		this.maxLengthBlock = max;
		this.selectID = selectID;
		this.rList = rateList;
		this.rate = rateList.getRateList()[selectID];
		this.playtime = rateList.getPlaybackTime();
		this.bList = st;
		setId(st.size());
		serverSize = s.getLserver().size();
		tline = tl;
	}
	
	public void init()
	{
		initBlock(slist.getLserver().size());
		//System.out.println("fragNum = " +  fragNum + " - - - serverSize = "+ serverSize);
		sch = new Schedule(fragNum,serverSize,slist.getLserver(), fragList);
		sch.doSchedule();
		for(int i=0;i<fragNum;i++)
			getDownloadDur(i);
	}
	
	public void initBW()
	{
		initBlock(1);
		//System.out.println("fragNum = " +  fragNum + " - - - serverSize = "+ serverSize);
		sch = new Schedule(fragNum,1,slist.getLserver(), fragList);
		sch.doSchedule();
		for(int i=0;i<fragNum;i++)
			getDownloadDur(i);
	}
	
	public boolean isFirstBlock()
	{
		if(bList.indexOf(this) == 0)
			return true;
		else return false;
	}

	public boolean isLastBlock()
	{
		if(bList.indexOf(this) == bList.size()-1)
			return true;
		else return false;
	}
	
	public void initBlock(int ssize)
	{
		serverSize = ssize;
		double cmin = slist.getLserver().get(serverSize-1).getBandwidth(buffer.getDownloadDur());
		while(true)
		{
			slist.getLserver().get(serverSize-1).setNumOfFragReq(1);//nSmax = 1;
			fragNum = 1;
			for (int i = 0; i<serverSize-1;i++)
			{
				double ci = slist.getLserver().get(i).getBandwidth(buffer.getDownloadDur());
				slist.getLserver().get(i).setNumOfFragReq((int) Kit.round(ci,cmin));
				//System.out.println(i + " - - - "+ slist.getLserver().get(i).getNumOfFragReq());
				fragNum += slist.getLserver().get(i).getNumOfFragReq();
			}
			if(fragNum>maxLengthBlock)
			{
				serverSize--;
				cmin = slist.getLserver().get(serverSize-1).getBandwidth(buffer.getDownloadDur());
			}
			else
			{
				break;
			}
		}
		for(int i =0;i<fragNum;i++)
		{
			initFragment(i);
		}
	}
	
	private void initFragment(int id)
	{
		Fragment f = new Fragment(this,rate,playtime,id,null,buffer, tline);
		fragList.add(f);
	}
	
	public double getDownloadDur(int n)
	{
		double ret = 0;
		if(n<=fragNum)
		{
			double fndur = fragList.get(n).getDownloadDur();
			double temp = 0;
			for(int j = 0;j<serverSize;j++)
			{
				double t = sch.getX(n, j);
				double m = 0;
				for(int i=0;i<=n;i++)
					m += sch.getX(i, j);
				temp += t * m;
			}
			ret = fndur * temp;
			//System.out.println("Block download dur ("+n+"/"+(fragNum-1)+") - "+ret + "(" + fndur + "*" + temp +")");
		}
		return ret;
	}
	
	public double getalphan(int n)
	{
		double ret = 0;
		double p = 0;
		double q = 0;
		for(int i=0;i<serverSize;i++)
		{
			//q += sch.getX(n, i) * slist.getLserver().get(i).getBandAvgwidth(buffer.getdownloadDurWithBlock(this, n));
			q += sch.getX(n, i) * slist.getLserver().get(i).getBandAvgwidth(fragList.get(n).getDownloadEndTime());
		}
		for(int j = 0;j<serverSize;j++)
		{
			double t = sch.getX(n, j);
			double m = 0;
			for(int i=0;i<=n;i++)
				m += sch.getX(i, j);
			p += t * m;
		}
		ret = p/q;

		return ret;
	}
	
	public int getNewRate() {
		int pRate = rate;
		if(serverSize == 1)
		{
			pRate = 0;
			for(int i = 0;i<slist.getLserver().size();i++)
				pRate += slist.getLserver().get(i).getBandAvgwidth(getDownloadEndTime());
			pRate /= slist.getLserver().size();
		}
		int newSelect = 0;
		//double kP = 0.002, kD = 2;
		double kP = 0.3, kD = 0.03;
		int k = bList.indexOf(this);
		
		double BufferLength = buffer.getBlockEndBufferLength(bList.indexOf(this));
		if(Math.floor(BufferLength)<qmax && Math.floor(BufferLength)>qmin)
			return selectID;
		else
		{
			double []vk = new double[fragNum];
			
			double q0 = 0;//
			
			if(Math.floor(BufferLength)<=qmin)
				q0 = qmin;
			else if(Math.floor(BufferLength)>=qmax)
				q0 = qmax;
			
			for(int i=0;i<fragNum;i++)
			{
				double alphaN = getalphan(i);
				double q_fragtEnk = buffer.getBufferLengthWithBlocknFrag(k, i);
				double q_blockSk = buffer.getBlockStartBufferLength(bList.indexOf(this));
				double fragDownDur = getDownloadDur(i);
				vk[i] = ((1/(playtime*alphaN)) * kP * (BufferLength - q0))
						+ 
						((1/(playtime*alphaN)) * kD * ((q_fragtEnk - q_blockSk) / (fragDownDur)));
				//System.out.println("alphaN = " + alphaN);
				/*System.out.println("===================" + i + "/" + (fragNum-1)+"===================");
				System.out.println("q0 = " + q0);
				System.out.println("alphaN = " + alphaN);
				System.out.println("BufferLength = " + BufferLength);
				System.out.println("q_fragtEnk = " + q_fragtEnk);
				System.out.println("q_blockSk = " + q_blockSk);
				System.out.println("fragDownDur = " + fragDownDur);
				System.out.println("vk["+i+"] = " + vk[i]);*/
			}
			//compute max min
			double vtemp = vk[0];
			for(int i = 0;i<fragNum;i++)
			{
				if(BufferLength<=qmin && vtemp>vk[i])
				{
					vtemp = vk[i];
				}
				else if(BufferLength>=qmax && vtemp<vk[i])
				{
					vtemp = vk[i];
				}
			}
			//compute new rate
			double newRate = pRate + vtemp;
			
			newSelect = rList.getNewRateID(newRate);
			
			//System.out.println("newRate = " + newRate + " ,newSelect = " + newSelect + " ,selectID = " + selectID);
			
			if(Math.floor(BufferLength)<=qmin && newSelect>selectID)
				newSelect = selectID;
			else if(Math.floor(BufferLength)>=qmax && newSelect<selectID)
				newSelect = selectID;
			
			//System.out.println("newSelect = " + newSelect + "selectID + " + selectID);
			
			/*
			if(Math.floor(BufferLength)<=qmin && newSelect==selectID && newSelect>0)
			{
				newSelect--;
			}
			else if(Math.floor(BufferLength)>=qmax && newSelect==selectID && newSelect<rList.getLength()-1)
			{
				newSelect++;
			}*/
	
			//System.out.println("New Rate : " + newRate + " ---- newSelect: " + newSelect);
		}
		return newSelect;
	}
	
	public double getDownloadEndTime()
	{
		return fragList.get(fragNum-1).getDownloadEndTime();
	}
	

	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public List<Fragment> getFragList() {
		return fragList;
	}
	public void setFragList(List<Fragment> fragList) {
		this.fragList = fragList;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public Schedule getSch() {
		return sch;
	}
	public void setSch(Schedule sch) {
		this.sch = sch;
	}

	public int getServerSize() {
		return serverSize;
	}

	public void setServerSize(int serverSize) {
		this.serverSize = serverSize;
	}

	public ServerList getSlist() {
		return slist;
	}

	public void setSlist(ServerList slist) {
		this.slist = slist;
	}

	public Buffer getBuffer() {
		return buffer;
	}

	public void setBuffer(Buffer buffer) {
		this.buffer = buffer;
	}

	public double getPlaytime() {
		return playtime;
	}

	public void setPlaytime(double playtime) {
		this.playtime = playtime;
	}

	public int getAllocFrag() {
		return allocFrag;
	}

	public void setAllocFrag(int allocFrag) {
		this.allocFrag = allocFrag;
	}

	public int getFragNum() {
		return fragNum;
	}

	public void setFragNum(int fragNum) {
		this.fragNum = fragNum;
	}

	public int getMaxLengthBlock() {
		return maxLengthBlock;
	}

	public void setMaxLengthBlock(int maxLengthBlock) {
		this.maxLengthBlock = maxLengthBlock;
	}

	public int getSelectID() {
		return selectID;
	}

	public void setSelectID(int selectID) {
		this.selectID = selectID;
	}

	public double getStartTime() {
		return startTime;
	}

	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}

	public List<Block> getbList() {
		return bList;
	}

	public void setbList(List<Block> bList) {
		this.bList = bList;
	}

	public double getQmax() {
		return qmax;
	}

	public void setQmax(double qmax) {
		this.qmax = qmax;
	}

	public double getQmin() {
		return qmin;
	}

	public void setQmin(double qmin) {
		this.qmin = qmin;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


}
