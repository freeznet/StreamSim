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
	private BitRateHistory brH;
	
	private boolean used = false;
	
	private int selectID = 0;
	
	private double qmax,qmin;
	
	public Fragment(int index, Buffer b, ServerList sList, VideoRateList vList, List<Fragment> fList, Server downBy, int br, BitRateHistory brH)
	{
		id = index;
		buffer = b;
		serverList = sList;
		vrateList = vList;
		fragList = fList;
		downloadedBy = downBy;
		bitrate = vList.getRateList()[br];
		selectID = br;
		playbackTime = vList.getPlaybackTime();
		downloadedBy.setAvailable(false);
		if(downloadedBy.getLastFragment()!=null)
			downloadStartTime = downloadedBy.getLastFragment().getDownloadEndTime();
		downloadedBy.assignFragment(this);
		this.brH = brH;
	}
	
	public void setDownloadStartTime(double t)
	{
		downloadStartTime = t;
	}

	public double getDownloadDur() {
		if(done==true)
			return downloadDur;
		
		downloadStartBufferLength = buffer.getBufferLengthWithTime(downloadStartTime);
		//bitrate = brH.getChangeRate(downloadStartTime);
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
		//System.out.println(downloadedBy.getId() + " " + "0 0"+" "+id + " " + downloadEndTime + " " + bitrate + " " + buffer.getBufferLengthWithTime(this));
		downloadEndBufferLength = buffer.getBufferLengthWithTime(this);
		return downloadDur;
	}

	public void printLog()
	{
		System.out.println(downloadedBy.getId() + " " + "0"+" "+id + " " + downloadEndTime + " " + bitrate + " " + buffer.getBufferLengthWithFrag(this));
	}
	
	public double getDownloadEndBufferLength()
	{
		return downloadEndBufferLength;
	}
	
	public int getNewRate()
	{
		double pRate = bitrate;
		int newSelect = 0;
		//double kP = 0.002, kD = 2;
		double kP = 0.5, kD = 0.02;
		
		/*for(Server s : serverList.getLserver())
		{
			pRate += s.getBandAvgwidth(downloadEndTime);
		}
		pRate /= serverList.getLserver().size();*/
		double BufferLength = downloadEndBufferLength;
		if(Math.floor(BufferLength)<qmax && Math.floor(BufferLength)>qmin)
			return selectID;
		else
		{
			double vk = 0;
			
			double q0 = 0;//
			
			if(Math.floor(BufferLength)<=qmin)
				q0 = qmin;
			else if(Math.floor(BufferLength)>=qmax)
				q0 = qmax;
			
		
			double alphaN = 1 / downloadedBy.getBandAvgwidth(downloadEndTime);
			double q_fragtEnk = downloadEndBufferLength;
			double q_blockSk = downloadStartBufferLength;
			double fragDownDur = downloadDur;
			vk = ((1/(playbackTime*alphaN)) * kP * (BufferLength - q0))
					+ 
					((1/(playbackTime*alphaN)) * kD * (((BufferLength - q0) - (q_blockSk - q0)) / (fragDownDur)));
			/*System.out.println("alphaN = " + alphaN + " ,playbackTime = " + playbackTime);
			System.out.println("BufferLength = " + BufferLength + " ,q0 = " + q0);
			System.out.println("q_fragtEnk = " + q_fragtEnk + " ,q_blockSk = " + q_blockSk);
			System.out.println("fragDownDur = " + fragDownDur + " ,1st = " + (1/(playbackTime*alphaN)) * kP * (BufferLength - q0));
			System.out.println("2nd = " + ((1/(playbackTime*alphaN)) * kD * ((q_fragtEnk - q_blockSk) / (fragDownDur))));*/
			/*System.out.println("===================" + i + "/" + (fragNum-1)+"===================");
			System.out.println("q0 = " + q0);
			System.out.println("alphaN = " + alphaN);
			System.out.println("BufferLength = " + BufferLength);
			System.out.println("q_fragtEnk = " + q_fragtEnk);
			System.out.println("q_blockSk = " + q_blockSk);
			System.out.println("fragDownDur = " + fragDownDur);
			System.out.println("vk["+i+"] = " + vk[i]);*/
			
			//compute max min
			double vtemp = 0;
			if(BufferLength<=qmin)
			{
				vtemp = vk;
			}
			else if(BufferLength>=qmax)
			{
				vtemp = vk;
			}
			//compute new rate
			double newRate = pRate + vtemp;
			
			//System.out.println("newRate = " + newRate);
			
			newSelect = vrateList.getNewRateID(newRate);
			
			//System.out.println("newRate = " + newRate + " ,newSelect = " + newSelect + " ,selectID = " + selectID);
			
			if(Math.floor(BufferLength)<=qmin && newSelect>selectID)
				newSelect = selectID;
			else if(Math.floor(BufferLength)>=qmax && newSelect<selectID)
				newSelect = selectID;
			
			//System.out.println("newSelect = " + newSelect + "selectID + " + selectID);
			
			/*
			if(Math.floor(BufferLength)<=qmin && newSelect==selectID && newSelect>0)
			{
				newSelect--;
			}
			else if(Math.floor(BufferLength)>=qmax && newSelect==selectID && newSelect<rList.getLength()-1)
			{
				newSelect++;
			}*/
	
			//System.out.println("New Rate : " + newRate + " ---- newSelect: " + newSelect);
		}
		return newSelect;
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

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public Server getDownloadedBy() {
		return downloadedBy;
	}

	public void setDownloadedBy(Server downloadedBy) {
		this.downloadedBy = downloadedBy;
	}

	public double getDownloadStartBufferLength() {
		return downloadStartBufferLength;
	}

	public void setDownloadStartBufferLength(double downloadStartBufferLength) {
		this.downloadStartBufferLength = downloadStartBufferLength;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getDownloadStartTime() {
		return downloadStartTime;
	}

}
