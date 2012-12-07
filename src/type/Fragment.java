package type;

import java.util.List;

public class Fragment {
	private Server downloadedBy;
	private double downloadDur;
	private double downloadStartTime = 0;
	private double downloadEndTime;
	private double downloadStartBufferLength;
	private double downloadEndBufferLength;
	private double bitrate;
	private double playbackTime;
	private Block block;
	private int id = 0;
	private Buffer buffer;
	private boolean done = false;
	private Timeline tline;
	public Fragment(Block b, double br, double pt, int i, Server f, Buffer bf, Timeline line){
		block = b;
		bitrate = br;
		playbackTime = pt;
		id = i;
		downloadedBy = f;
		buffer = bf;
		tline = line;
		/*
		if(block.isFirstBlock()){
			downloadStartBufferLength = 0;
			downloadStartTime = 0;
		}
		else if(!block.isFirstBlock())
		{
			downloadStartTime = block.
		}*/
		
	};
	
	public double getDownloadDur()
	{
		if(done==true)
			return downloadDur;
		
		double fileSize = playbackTime * bitrate;
		double time = downloadStartTime;
		while(fileSize>0)
		{
			double bw = 0;
			
			for(int i=0;i<block.getServerSize();i++)
			{
				bw += block.getSch().getX(id, i) * block.getSlist().getLserver().get(i).getBandwidth(time);
			}
			double downloadTempDur = 0;
			
			double downloadSize = (Math.ceil(time+0.0000000001) - time) * bw;
			//System.out.println("bw = " + bw + " downloadSize = "+downloadSize);
			if(fileSize - downloadSize>=0){
				downloadTempDur = Math.ceil(time+0.0000000001) - time;
				fileSize -= downloadSize;
				tline.setBufferSize((int) Math.floor(time), downloadSize/bitrate);
				tline.setServerBufferSize(downloadedBy.getId()-1, (int) Math.floor(time), downloadSize/bitrate);
			}
			else
			{
				//double s = fileSize;
				downloadTempDur = fileSize / bw;
				tline.setBufferSize((int) Math.floor(time), fileSize/bitrate);
				tline.setServerBufferSize(downloadedBy.getId()-1, (int) Math.floor(time), fileSize/bitrate);
				fileSize = 0;
			}
			time += downloadTempDur;
		}
		
		downloadDur = time - downloadStartTime;
		downloadEndTime = time;
		if(downloadedBy!=null)
		{
			Fragment next = downloadedBy.getNextFragment(this,block);
			if(next!=null)
				next.downloadStartTime = downloadEndTime;
		}
		done=true;
		//System.out.println("id " + block.getId() +"/"+id + ":" + downloadedBy.getId()+" - > downloadStartTime:" + downloadStartTime + " downloadEndTime:" + downloadEndTime);
		System.out.println(downloadedBy.getId() + " " + block.getId()+" "+ id + " " +"0 " + downloadEndTime + " " + bitrate + " " + buffer.getBufferLengthWithBlocknFrag(block, this));
		//System.out.println(downloadedBy.getId() + " " + block.getId()+" "+ id + " " +"0 " + downloadStartTime + " " + bitrate + " " + buffer.getBufferLengthWithBlocknFrag(block, this));

		return downloadDur;
	}

	public Server getDownloadedBy() {
		return downloadedBy;
	}

	public void setDownloadedBy(Server downloadedBy) {
		this.downloadedBy = downloadedBy;
		if(downloadedBy.isFirstFragDownloadinBlock(this, block))
		{
			int blockIndex = block.getbList().indexOf(block);
			if(blockIndex>0)
			{
				Block lastBlock = block.getbList().get(blockIndex - 1);
				Fragment f = lastBlock.getFragList().get(lastBlock.getFragList().size()-1);
				downloadStartTime = f.downloadEndTime;
			}
			
		}
		//else
		//	downloadStartTime = downloadedBy.getDownloadSec(this);
		
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
