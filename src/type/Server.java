package type;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Server {
	private int id = 0;
	private double[] bandwidth;
	private String url = "";
	private Block nowBlock = null;
	private Queue<String> q = new LinkedList<String>();
	private int numOfFragReq = 0;
	private double downloadSec = 0;
	private List<Fragment> downloadFrag = new ArrayList<Fragment>();
	
	public Server(int id, String url, double[] bandwidth)
	{
		this.id = id;
		this.url = url;
		this.bandwidth = bandwidth;
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
		ret = ret / i;
		return ret;
	}
	public double getBandwidth(int sec) {
		return bandwidth[sec];
	}
	public double getBandwidth(double sec) {
		
		return bandwidth[(int) Math.floor(sec)];
	}
	public String getUrl() {
		return url;
	}
	public Block getNowBlock() {
		return nowBlock;
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
	public void setNowBlock(Block nowBlock) {
		this.nowBlock = nowBlock;
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
	
}
