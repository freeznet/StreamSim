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
	public Block(Buffer buf, VideoRateList rateList, ServerList s, int max, int selectID)
	{
		this.buffer = buf;
		this.slist = s;
		this.maxLengthBlock = max;
		this.selectID = selectID;
		this.rate = rateList.getRateList()[selectID];
		this.playtime = rateList.getPlaybackTime();
		serverSize = s.getLserver().size();
		initBlock();
		System.out.println("fragNum = " +  fragNum + " - - - serverSize = "+ serverSize);
		sch = new Schedule(fragNum,serverSize,slist.getLserver());
		sch.doSchedule();
		getDownloadDur(0);
		getDownloadDur(1);
		getDownloadDur(2);
		getDownloadDur(3);
	}

	public void initBlock()
	{
		serverSize = slist.getLserver().size();
		double cmin = slist.getLserver().get(serverSize-1).getBandwidth();
		while(true)
		{
			slist.getLserver().get(serverSize-1).setNumOfFragReq(1);//nSmax = 1;
			fragNum = 1;
			for (int i = 0; i<serverSize-1;i++)
			{
				double ci = slist.getLserver().get(i).getBandwidth();
				slist.getLserver().get(i).setNumOfFragReq((int) Kit.round(ci,cmin));
				//System.out.println(i + " - - - "+ slist.getLserver().get(i).getNumOfFragReq());
				fragNum += slist.getLserver().get(i).getNumOfFragReq();
			}
			if(fragNum>maxLengthBlock)
			{
				serverSize--;
				cmin = slist.getLserver().get(serverSize-1).getBandwidth();
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
		Fragment f = new Fragment(this,rate,playtime,id);
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
			System.out.println("Block download dur ("+n+"/"+(fragNum-1)+") - "+ret);
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
			q += sch.getX(n, i) * slist.getLserver().get(i).getBandwidth();
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
}
