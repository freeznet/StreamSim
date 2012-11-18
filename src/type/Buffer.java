package type;

import java.util.List;

public class Buffer {
	private double lengthSec = 0;
	List<Block> bList = null;
	private double initBufferLength = 0;
	public Buffer(List<Block> b){
		this.bList = b;
	};
	public double getLengthSec() {
		return lengthSec;
	}

	public void setLengthSec(double lengthSec) {
		this.lengthSec = lengthSec;
	}
	
	public double getBufferLengthWithBlocknFrag(int k, int n)
	{
		double ret = getBlockStartBufferLength(k);
		ret += (n+1) * bList.get(k).getPlaytime();
		ret = ret - bList.get(k).getPlaytime() * bList.get(k).getRate() * bList.get(k).getalphan(n);
		System.out.println("n= " + n + " -> " + bList.get(k).getalphan(n));
		return ret;
	}
	
	public double getBlockStartBufferLength(int k)
	{
		double ret = initBufferLength;
		if(k==0)
			return ret;
		for(int i=1;i<=k;i++)
		{
			ret += getBlockEndBufferLength(i-1);
		}
		return ret;
	}
	
	public double getBlockEndBufferLength(int k)
	{
		double ret = getBlockStartBufferLength(k);
		ret += bList.get(k).getFragList().size() * bList.get(k).getPlaytime();
		/*double m= 0 ,n= 0 ,o= 0 ,p = 0;
		for(int j = 0; j<bList.get(k).getServerSize(); j++)
		{
			for(int i=0;i<bList.get(k).getFragList().size();i++)
			{
				p += bList.get(k).getSch().getX(i, j);
			}
			o += bList.get(k).getSch().getX(bList.get(k).getFragList().size()-1, j) * p;
			n += bList.get(k).getSch().getX(bList.get(k).getFragList().size()-1, j) * bList.get(k).getSlist().getLserver().get(j).getBandwidth();
		}
		m = o / n;*/
		double download = bList.get(k).getDownloadDur(bList.get(k).getFragNum()-1);
		ret -= download;
		return ret;
	}
}
