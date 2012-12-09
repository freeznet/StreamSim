package fragOnly;

public class Info {
	private int fragID = 0;
	private double endDownTime = 0;
	private double endDownBuffer = 0;
	private Server downBy = null;
	private int downServerID = 0;
	private double bitrate = 0;
	public Info(Fragment f)
	{
		fragID = f.getId();
		endDownTime = f.getDownloadEndTime();
		endDownBuffer = f.getDownloadEndBufferLength();
		downBy = f.getDownloadedBy();
		downServerID = downBy.getId();
		bitrate = f.getRate();
		
		if(endDownBuffer<0)
			endDownBuffer = 0;
	}
	public int getFragID() {
		return fragID;
	}
	public void setFragID(int fragID) {
		this.fragID = fragID;
	}
	public double getEndDownTime() {
		return endDownTime;
	}
	public void setEndDownTime(double endDownTime) {
		this.endDownTime = endDownTime;
	}
	public double getEndDownBuffer() {
		return endDownBuffer;
	}
	public void setEndDownBuffer(double endDownBuffer) {
		this.endDownBuffer = endDownBuffer;
	}
	public Server getDownBy() {
		return downBy;
	}
	public void setDownBy(Server downBy) {
		this.downBy = downBy;
	}
	public int getDownServerID() {
		return downServerID;
	}
	public void setDownServerID(int downServerID) {
		this.downServerID = downServerID;
	}
	public double getBitrate() {
		return bitrate;
	}
	public void setBitrate(double bitrate) {
		this.bitrate = bitrate;
	}
}
