package fragOnly;

import java.util.List;

public class Fragment {
	private int id = 0;
	private Buffer buffer = null;
	private ServerList serverList = null;
	private VideoRateList vrateList = null;
	private List<Fragment> fragList = null;
	private Server downloadedBy = null;
	private double downloadDur;
	private double downloadStartTime = 0;
	private double downloadEndTime;
	private double downloadStartBufferLength;
	private double downloadEndBufferLength;
	private double bitrate;
	private double playbackTime;
	private boolean done = false;
	
	private double qmax,qmin;
	
	public Fragment(int index, Buffer b, ServerList sList, VideoRateList vList, List<Fragment> fList, Server downBy, int br)
	{
		id = index;
		buffer = b;
		serverList = sList;
		vrateList = vList;
		fragList = fList;
		downloadedBy = downBy;
		bitrate = vList.getRateList()[br];
		playbackTime = vList.getPlaybackTime();
		downloadedBy.setAvailable(false);
		if(downloadedBy.getLastFragment()!=null)
			downloadStartTime = downloadedBy.getLastFragment().getDownloadEndTime();
		downloadedBy.assignFragment(this);
	}
	
	public void setDownloadStartTime(double t)
	{
		downloadStartTime = t;
	}

	public double getDownloadDur() {
		if(done==true)
			return downloadDur;
		
		double fileSize = playbackTime * bitrate;
		double time = downloadStartTime;
		while(fileSize>0)
		{
			double bw = downloadedBy.getBandwidth(time);

			double downloadTempDur = 0;
			
			double downloadSize = (Math.ceil(time+0.0000000001) - time) * bw;
			//System.out.println("bw = " + bw + " downloadSize = "+downloadSize);
			if(fileSize - downloadSize>=0){
				downloadTempDur = Math.ceil(time+0.0000000001) - time;
				fileSize -= downloadSize;
			}
			else
			{
				//double s = fileSize;
				downloadTempDur = fileSize / bw;
				fileSize = 0;
			}
			time += downloadTempDur;
		}
		
		downloadDur = time - downloadStartTime;
		downloadEndTime = time;
		/*if(downloadedBy!=null)
		{
			Fragment next = downloadedBy.getNextFragment(this);
			if(next!=null)
				next.downloadStartTime = downloadEndTime;
		}*/
		done=true;
		downloadedBy.setAvailable(true);
		downloadedBy.setNextAvailableTime(downloadEndTime);
		//System.out.println("id " + block.getId() +"/"+id + ":" + downloadedBy.getId()+" - > downloadStartTime:" + downloadStartTime + " downloadEndTime:" + downloadEndTime);
		
		return downloadDur;
	}

	public void printLog()
	{
		System.out.println(downloadedBy.getId() + " " + "0"+" "+id + " " + downloadEndTime + " " + bitrate + " " + buffer.getBufferLengthWithFrag(this));
	}
	
	public double getPlaytime() {
		// TODO Auto-generated method stub
		return playbackTime;
	}

	public double getRate() {
		// TODO Auto-generated method stub
		return bitrate;
	}

	public int getalphan(int n) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setQmax(double qmax) {
		this.qmax = qmax;
	}
	public void setQmin(double qmax) {
		this.qmin = qmax;
	}

	public double getDownloadEndTime() {
		return downloadEndTime;
	}

	public boolean isDone() {
		return done;
	}

}
