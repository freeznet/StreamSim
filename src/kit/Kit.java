package kit;

public class Kit {
	public static double min(double a[])
	{
		double temp = 0;  
		for (int i = 0; i < a.length; i++)  
		{  
			if(i == 0)  
			{   
				temp = a[i];  
			}  
			else  
			{  
				temp = temp < a[i]?temp:a[i];  
			}  
		}  
		return temp;
	}
	
	public static int getMinId(double a[])
	{
		double t = min(a);
		for(int i=0;i<a.length;i++)
		{
			if(t==a[i])
				return i;
		}
		return -1;
	}
	
	public static int round(double a, double b)
	{
		double t = a/b;
		double x = Math.floor(t);
		if(t-x>0.8)
		{
			return (int)x+1;
		}
		else
			return (int)x;
	}
	
}
