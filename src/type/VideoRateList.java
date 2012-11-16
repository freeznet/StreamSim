package type;

public class VideoRateList {
	private int length = 0;
	private int[] rateList;
	private int nowDownload = 0;
	private int nowPlay = 0;
	public VideoRateList(int l, int[] r)
	{
		this.length = l;
		this.rateList = r;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int[] getRateList() {
		return rateList;
	}
	public void setRateList(int[] rateList) {
		this.rateList = rateList;
	}
	public int getNowDownload() {
		return nowDownload;
	}
	public void setNowDownload(int nowDownload) {
		this.nowDownload = nowDownload;
	}
	public int getNowPlay() {
		return nowPlay;
	}
	public void setNowPlay(int nowPlay) {
		this.nowPlay = nowPlay;
	}
	
}
