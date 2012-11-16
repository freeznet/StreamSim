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
		return ret;
	}
	
}
