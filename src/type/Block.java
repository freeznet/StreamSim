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
	
	public Block(Buffer buf, VideoRateList rateList, ServerList s, int max, int selectID,List<Block> st)
	{
		this.buffer = buf;
		this.slist = s;
		this.maxLengthBlock = max;
		this.selectID = selectID;
		this.rate = rateList.getRateList()[selectID];
		this.playtime = rateList.getPlaybackTime();
		this.bList = st;
		serverSize = s.getLserver().size();
		initBlock();
		System.out.println("fragNum = " +  fragNum + " - - - serverSize = "+ serverSize);
		sch = new Schedule(fragNum,serverSize,slist.getLserver(), fragList);
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

	public void initBlock()
	{
		serverSize = slist.getLserver().size();
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
		Fragment f = new Fragment(this,rate,playtime,id,null);
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
			System.out.println("Block download dur ("+n+"/"+(fragNum-1)+") - "+ret + "(" + fndur + "*" + temp +")");
		}
		return ret;
	}
	
	public double getalphan(int n)
	{
		double ret = 0;
		double p = 0;
		double q = 0;
		for(int i=0;i<getServerSize();i++)
		{
			q += sch.getX(n, i) * slist.getLserver().get(i).getBandAvgwidth(buffer.getdownloadDurWithBlock(this, n));
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
		
		int kP = 5, kD = 5;
		int k = bList.indexOf(this);
		
		double BufferLength = buffer.getBlockEndBufferLength(bList.indexOf(this));
		if(BufferLength<qmax && BufferLength>qmin)
			return pRate;
		else
		{
			double []vk = new double[fragNum];
			int q0 = 0;
			for(int i=0;i<fragNum;i++)
			{
				vk[i] = ((1/playtime*getalphan(i)) * kP * (BufferLength - q0))
						+ 
						((1/playtime*getalphan(i)) * kD * ((buffer.getBufferLengthWithBlocknFrag(k, i) - buffer.getBlockStartBufferLength(bList.indexOf(this))) / (getDownloadDur(i))));
			}
			//compute max min
		}
		return 0;
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


}
