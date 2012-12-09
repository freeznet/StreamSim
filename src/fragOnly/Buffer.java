package fragOnly;

import java.util.List;

import fragOnly.Fragment;

public class Buffer {

	private double lengthSec = 0;
	private double downloadDur = 0;
	List<Fragment> bList = null;
	private double initBufferLength = 0;
	private ServerList sList = null;
	private BitRateHistory brHistory = null;
	public Buffer(List<Fragment> b, ServerList sList, BitRateHistory brH){
		this.bList = b;
		this.sList = sList;
		this.brHistory = brH;
	};
	public double getLengthSec() {
		return lengthSec;
	}

	public void setLengthSec(double lengthSec) {
		this.lengthSec = lengthSec;
	}
	
	public double getdownloadDurWithFragment(Fragment b)
	{
		double ret = 0;
		
		for(Fragment n: bList)
		{
			if(n!=b)
			{
				ret += n.getDownloadDur();
			}
			if(n==b)
				break;
		}
		
		return ret;
	}
	
	public double getBufferLengthWithBlocknFrag(int k, int n)
	{
		double ret = getBlockStartBufferLength(k);
		ret += (n+1) * bList.get(k).getPlaytime();
		ret = ret - bList.get(k).getPlaytime() * bList.get(k).getRate() * bList.get(k).getalphan(n);
		//System.out.println("n= " + n + " -> " + bList.get(k).getalphan(n));
		return ret;
	}
	/*
	public double getBufferLengthWithBlocknFrag(Block k, Fragment n)
	{
		double ret = getBlockStartBufferLength(k.getId());
		int index = k.getFragList().indexOf(n);
		ret += (index+1) * bList.get(k.getId()).getPlaytime();
		ret = ret - bList.get(k.getId()).getPlaytime() * bList.get(k.getId()).getRate() * bList.get(k.getId()).getalphan(index);
		//System.out.println("n= " + n + " -> " + bList.get(k).getalphan(n));
		return ret;
	}
	*/
	public double getBlockStartBufferLength(int k)
	{
		double ret = initBufferLength;
		if(k==0)
			return ret;
		
		//ret = getBlockEndBufferLength(k-1);
		return ret;
	}
	/*
	public double getBlockEndBufferLength(int k)
	{
		double ret = getBlockStartBufferLength(k);
		ret += bList.get(k).getFragList().size() * bList.get(k).getPlaytime();
		double download = bList.get(k).getDownloadDur(bList.get(k).getFragNum()-1);
		ret -= download;
		return ret;
	}*/
	public double getDownloadDur() {
		return downloadDur;
	}
	public void setDownloadDur(double downloadDur) {
		this.downloadDur = downloadDur;
	}
	public List<Fragment> getbList() {
		return bList;
	}
	public void setbList(List<Fragment> bList) {
		this.bList = bList;
	}
	public double getInitBufferLength() {
		return initBufferLength;
	}
	public void setInitBufferLength(double initBufferLength) {
		this.initBufferLength = initBufferLength;
	}
	public double getBufferLengthWithFrag(Fragment fragment) {
		double ret = 0;
		
		for(Fragment f : bList)
		{
			if(f != fragment && f.isDone() && f.getDownloadEndTime() <= fragment.getDownloadEndTime())
				ret += (f.getPlaytime() - f.getDownloadDur());
		}
		ret += (fragment.getPlaytime() - fragment.getDownloadDur());
		return ret;
	}
	
	public double getBufferLengthWithTime(Fragment fragment) {
		double ret = 0;
		double endTime = fragment.getDownloadEndTime();
		int intTime = (int) Math.floor(endTime);
		double doubleTime = endTime - intTime;
		
		//System.out.println("endTime = " + endTime + ",intTime = " + intTime + ", doubleTime = " + doubleTime + ",brHistory.getChangeRate(i) = " + brHistory.getChangeRate(0) );
		
		for(int i=1;i<=intTime;i++)
		{
			for(Server s:sList.getLserver())
			{
				//System.out.println("i = " + i + " -> " + s.getBandwidth(i) / brHistory.getChangeRate(i));
				ret += (s.getBandwidth(i) / brHistory.getChangeRate(i,s.getId()));
			}
		}
		
		for(Server s:sList.getLserver())
		{
			//System.out.println("doubleTime = " + doubleTime + " -> " + s.getBandwidth(intTime) * doubleTime / brHistory.getChangeRate(intTime));
			ret += (s.getBandwidth(intTime) * doubleTime / brHistory.getChangeRate(intTime,s.getId()));
		}
		
		return ret - endTime;
	}
	
	public double getBufferLengthWithTime(double time) {
		double ret = 0;
		double endTime = time;
		int intTime = (int) Math.floor(endTime);
		double doubleTime = endTime - intTime;
		
		//System.out.println("endTime = " + endTime + ",intTime = " + intTime + ", doubleTime = " + doubleTime + ",brHistory.getChangeRate(i) = " + brHistory.getChangeRate(0) );
		
		for(int i=1;i<=intTime;i++)
		{
			for(Server s:sList.getLserver())
			{
				//System.out.println("i = " + i + "/" + brHistory.getChangeRate(i) + " -> " + s.getBandwidth(i) / brHistory.getChangeRate(i));
				ret += (s.getBandwidth(i) / brHistory.getChangeRate(i,s.getId()));
			}
		}
		
		for(Server s:sList.getLserver())
		{
			//System.out.println("doubleTime = " + doubleTime + " -> " + s.getBandwidth(intTime) * doubleTime / brHistory.getChangeRate(intTime));
			ret += (s.getBandwidth(intTime) * doubleTime / brHistory.getChangeRate(intTime,s.getId()));
		}
		
		return ret - endTime;
	}

}
