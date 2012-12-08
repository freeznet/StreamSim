package fragOnly;

public class VideoRateList {

	private int length = 0;
	private int[] rateList;
	private int nowDownload = 0;
	private int nowPlay = 0;
	private double playbackTime = 0;

	public VideoRateList(int l, int[] r, double pt)
	{
		this.length = l;
		this.rateList = r;
		this.playbackTime = pt;
	}
	
	public int getNewRateID(double r)//up == false(取下限) else 取上限
	{
		int ret = 0;
		double temp = rateList[0];
		for(int i=0;i<rateList.length;i++)
		{
			if(rateList[i]<=r)
			{
				temp = rateList[i];
				ret = i;
			}
			//System.out.println(ret);
		}
		return ret;
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
	public double getPlaybackTime() {
		return playbackTime;
	}
	public void setPlaybackTime(double playbackTime) {
		this.playbackTime = playbackTime;
	}

}
