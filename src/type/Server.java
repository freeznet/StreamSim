package type;

import java.util.LinkedList;
import java.util.Queue;

public class Server {
	private int id = 0;
	private double bandwidth = 0;
	private String url = "";
	private Block nowBlock = null;
	private Queue<String> q = new LinkedList<String>();
	private int numOfFragReq = 0;
	
	public Server(int id, String url, double bandwidth)
	{
		this.id = id;
		this.url = url;
		this.bandwidth = bandwidth;
	}
	public int getId() {
		return id;
	}
	public double getBandwidth() {
		return bandwidth;
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
	public void setBandwidth(double bandwidth) {
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
	
}
