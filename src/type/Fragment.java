package type;

public class Fragment {
	private Server downloadedBy;
	private double downloadDur;
	private double downloadStartTime;
	private double downloadEndTime;
	private double downloadStartBufferLength;
	private double downloadEndBufferLength;
	private double bitrate;
	private double playbackTime;
	private Block block;
	private int id = 0;
	public Fragment(Block b, double br, double pt, int i){
		block = b;
		bitrate = br;
		playbackTime = pt;
		id = i;
	};
	
	public double getDownloadDur()
	{
		double ret = 0;
		for(int i=0;i<block.getServerSize();i++)
		{
			ret += block.getSch().getX(id, i) * block.getSlist().getLserver().get(i).getBandwidth();
		}
		ret = (playbackTime * bitrate) / ret;
		downloadDur = ret;
		//System.out.println("id " + id + " - > download_dur:" + ret);
		return ret;
	}

	public Server getDownloadedBy() {
		return downloadedBy;
	}

	public void setDownloadedBy(Server downloadedBy) {
		this.downloadedBy = downloadedBy;
	}

	public double getDownloadStartTime() {
		return downloadStartTime;
	}

	public void setDownloadStartTime(double downloadStartTime) {
		this.downloadStartTime = downloadStartTime;
	}

	public double getDownloadEndTime() {
		return downloadEndTime;
	}

	public void setDownloadEndTime(double downloadEndTime) {
		this.downloadEndTime = downloadEndTime;
	}

	public double getDownloadStartBufferLength() {
		return downloadStartBufferLength;
	}

	public void setDownloadStartBufferLength(double downloadStartBufferLength) {
		this.downloadStartBufferLength = downloadStartBufferLength;
	}

	public double getDownloadEndBufferLength() {
		return downloadEndBufferLength;
	}

	public void setDownloadEndBufferLength(double downloadEndBufferLength) {
		this.downloadEndBufferLength = downloadEndBufferLength;
	}

	public double getBitrate() {
		return bitrate;
	}

	public void setBitrate(double bitrate) {
		this.bitrate = bitrate;
	}

	public double getPlaybackTime() {
		return playbackTime;
	}

	public void setPlaybackTime(double playbackTime) {
		this.playbackTime = playbackTime;
	}

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setDownloadDur(double downloadDur) {
		this.downloadDur = downloadDur;
	}
	
}
