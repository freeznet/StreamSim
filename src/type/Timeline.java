package type;

public class Timeline {

	/**
	 * @param args
	 */
	private double []bufferDownload = null;
	private double []bufferSize = null;
	
	
	public Timeline(int size)
	{
		bufferDownload = new double[size];
		bufferSize = new double[size];
		for(int i=0;i<size;i++)
		{
			bufferDownload[i] = 0;
			bufferSize[i] = 0;
		}
	}
	
	public void setBufferSize(int t, double s)
	{
		bufferDownload[t] += s;
		if(t>0)
			bufferSize[t] = bufferSize[t-1] + bufferDownload[t] - 1;
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
}
