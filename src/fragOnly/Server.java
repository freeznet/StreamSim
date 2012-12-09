package fragOnly;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import fragOnly.Fragment;

public class Server {

	private int id = 0;
	private double[] bandwidth;
	private String url = "";
	private Queue<String> q = new LinkedList<String>();
	private int numOfFragReq = 0;
	private double downloadSec = 0;
	private List<Fragment> downloadFrag = new ArrayList<Fragment>();
	private List<Double> haltTime = new ArrayList<Double>();
	private boolean isAvailable = true;
	
	private double nextAvailableTime = 0;
	
	public Server(int id, String url, double[] bandwidth)
	{
		this.id = id;
		this.url = url;
		this.bandwidth = bandwidth;
	}
	
	public Fragment getNextFragment(Fragment f)
	{
		Fragment r = null;
		for(Fragment i:downloadFrag)
		{
			if(i==f)
			{
				int no = downloadFrag.indexOf(f);
				if(downloadFrag.indexOf(f) < downloadFrag.size()-1){
					r = downloadFrag.get(no+1);
					break;
				}
			}
		}
		return r;
	}
	
	public Fragment getLastFragment()
	{
		if(downloadFrag.size()>=1)
			return downloadFrag.get(downloadFrag.size()-1);
		return null;
	}
	
	public void assignFragment(Fragment f)
	{
		downloadFrag.add(f);
	}
	
	public int getFragmentIndex(Fragment f)
	{
		return downloadFrag.indexOf(f);
	}
	
	public Fragment getFragment(int i)
	{
		return downloadFrag.get(i);
	}
	
	public int getId() {
		return id;
	}
	public double getBandAvgwidth() {
		double ret = 0;
		int i;
		for(i=0;i<bandwidth.length;i++)
		{
			ret += bandwidth[i];
		}
		ret = ret / (i);
		return ret;
	}
	
	public double getBandAvgwidth(double sec)
	{
		double ret = 0;
		int i;
		int startTime = 0;
		int endTime = (int)Math.ceil(sec + 0.00000000000000001);
		int cnt = 0;
		if(endTime > 15)
			startTime = endTime - 15;
		for(i=startTime;i<endTime;i++)
		{
			ret += bandwidth[i];
			cnt++;
			//ret += bandwidth[0];
		}
		ret = ret / cnt;
		//System.out.println("get "+sec+"'s BW:" + ret + " with i = "+i);
		return ret;
	}
	
	public double getBandwidth(int sec) {
		return bandwidth[sec];
	}
	public double getBandwidth(double sec) {
		//System.out.println(sec + "---" +bandwidth[(int) Math.floor(sec)]);
		return bandwidth[(int) Math.floor(sec)];
		//return bandwidth[0];
	}
	public String getUrl() {
		return url;
	}

	public void setId(int id) {
		this.id = id;
	}
	public void setBandwidth(double[] bandwidth) {
		this.bandwidth = bandwidth;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public int getNumOfFragReq() {
		return numOfFragReq;
	}
	public void setNumOfFragReq(int numOfFragReq) {
		this.numOfFragReq = numOfFragReq;
	}
	public Queue<String> getQ() {
		return q;
	}
	public void setQ(Queue<String> q) {
		this.q = q;
	}
	public double getDownloadSec() {
		double ret = 0;
		for(Fragment f:downloadFrag)
		{
			ret += f.getDownloadDur();
		}
		downloadSec = ret;
		return downloadSec;
	}
	public double getDownloadSec(Fragment n) {
		double ret = 0;
		for(Fragment f:downloadFrag)
		{
			if(f==n)
				break;
			ret += f.getDownloadDur();
		}
		downloadSec = ret;
		return downloadSec;
	}
	public void setDownloadSec(double downloadSec) {
		this.downloadSec = downloadSec;
	}
	public double[] getBandwidth() {
		return bandwidth;
	}
	public List<Fragment> getDownloadFrag() {
		return downloadFrag;
	}
	public void setDownloadFrag(List<Fragment> downloadFrag) {
		this.downloadFrag = downloadFrag;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

	public double getNextAvailableTime() {
		return nextAvailableTime;
	}

	public void setNextAvailableTime(double nextAvailableTime) {
		this.nextAvailableTime = nextAvailableTime;
	}

}
