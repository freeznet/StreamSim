package type;

public class Timeline {

	/**
	 * @param args
	 */
	private double []bufferDownload = null;
	private double []bufferSize = null;
	
	private double [][] serverBufferSize = null;
	
	private int serverSize = 0;
	
	public Timeline(int size, int serverSize)
	{
		bufferDownload = new double[size];
		bufferSize = new double[size];
		serverBufferSize = new double[serverSize][size];
		this.serverSize = serverSize;
		for(int i=0;i<size;i++)
		{
			bufferDownload[i] = 0;
			bufferSize[i] = 0;
			for(int j=0;j<serverSize;j++)
				serverBufferSize[j][i] = 0;
		}
	}
	
	public void setBufferSize(int t, double s)
	{
		bufferDownload[t] += s;
		if(t>0)
			bufferSize[t] = bufferSize[t-1] + bufferDownload[t] - 1;
	}
	
	public void setServerBufferSize(int ser, int t, double s)
	{
		serverBufferSize[ser][t] += s;
	}
	
	public double getBufferSize(int i)
	{
		return bufferDownload[i];
	}
	
	public double [] getBufferSize()
	{
		return bufferDownload;
	}
	
	public void print()
	{
		for(int i=0;i<bufferDownload.length;i++)
		{
			
			System.out.println(i+" " + bufferDownload[i] + " " + bufferSize[i]);
		}
	}
	public void printServer(int j)
	{
		for(int i=0;i<bufferDownload.length;i++)
		{
			
			System.out.println(i+" " + serverBufferSize[j][i]);
		}
	}
	
	public void printServerAll()
	{
		
		for(int i=0;i<bufferDownload.length;i++)
		{
			int cnt = 0;
			String log = "";
			log = i+" ";
			double sum = 0;
			for(int j=0;j<serverSize;j++)
			{
				if(serverBufferSize[j][i] == 0)
					cnt ++;
				log += serverBufferSize[j][i] + "\t";
				sum += serverBufferSize[j][i];
			}
			log += sum + "\t";
			if(cnt!=serverSize)
				System.out.println(log);
		}
	}
}
