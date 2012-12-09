package fragOnly;

public class BitRateHistory {

	private double []changeTime = new double[2048]; 
	private int []changeRate = new int[2048]; 
	private int []changeServerID = new int[2048];
	private int cnt = 0;

	public BitRateHistory(){
		
	}
	
	public int getChangeRate(double time, int ServerID)
	{
		int ret = 0;
		for(int i=0;i<cnt;i++)
		{
			if(changeTime[i]<=time && changeServerID[i] == ServerID)
			{
				ret = changeRate[i];
			}
			else if(changeTime[i]>time)
				break;
		}
		return ret;
	}
	
	public void setChangeRate(double time, int rate, int ServerID)
	{
		changeTime[cnt] = time;
		changeRate[cnt] = rate;
		changeServerID[cnt] = ServerID;
		cnt ++;
	}
	
}
