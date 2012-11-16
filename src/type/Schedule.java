package type;

import java.util.List;

import kit.Kit;

public class Schedule {
	private int x[][];
	private int Nk = 0;
	private int Smax = 0;
	private List<Server> lserver = null;
	public Schedule(int n,int s, List<Server> l)
	{
		this.Nk = n;
		this.Smax = s;
		this.lserver = l;
		this.x = new int[Nk][Smax];
	}
	public int[][] doSchedule()
	{
		int i = 0;
		int j = 0;
		for(i=0;i<Nk;i++)
		{
			for(j=0;j<Smax;j++)
				x[i][j] = 0;
		}
		for(i=0;i<Nk;i++)
		{
			int jstar = 0;
			double jlist[] = new double[Smax]; 
			for(j=0;j<Smax;j++)
			{
				jlist[j] = (1/lserver.get(j).getBandwidth());
				for(int t=0; t<i; t++)
				{
					jlist[j] += x[t][j] * (1/lserver.get(j).getBandwidth());
				}
			}
			jstar = Kit.getMinId(jlist);
			//System.out.println("jstar = "+jstar);
			x[i][jstar] = 1;
		}
		printX();
		return x;
	}
	
	public void printX()
	{
		for(int i=0;i<Smax;i++)
		{
			for(int j=0;j<Nk;j++){
				System.out.print(x[j][i] + "	");
			}
			System.out.println();
		}
	}
	
	
}
