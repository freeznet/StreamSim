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
		Schedule sch = new Schedule(fragNum,serverSize,slist.getLserver());
		sch.doSchedule();
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
	}
	
	private void initFragment(int id)
	{
		Fragment f = new Fragment(this,rate,playtime,id);
		fragList.add(f);
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
}
