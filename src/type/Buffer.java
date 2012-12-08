package type;

import java.util.List;

public class Buffer {
	private double lengthSec = 0;
	private double downloadDur = 0;
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
	
	public double getdownloadDurWithBlock(Block b)
	{
		double ret = 0;
		
		for(Block n: bList)
		{
			if(n!=b)
			{
				ret += n.getDownloadDur(n.getFragNum()-1);
			}
			if(n==b)
				break;
		}
		
		return ret;
	}
	
	public double getdownloadDurWithBlock(Block b, int f)
	{
		double ret = 0;
		int bn = bList.indexOf(b);
		
		for(int i=0;i<=bn;i++)
		{
			Block k = bList.get(i);
			if(k!=b)
			{
				ret += k.getDownloadDur(k.getFragNum()-1);
			}
			else
			{
				for(int j=0;j<=f;j++)
					ret += k.getDownloadDur(j);
			}
		}
		
		return ret;
	}
	
	public double getBufferLengthWithBlocknFrag(int k, int n)
	{
		double ret = getBlockStartBufferLength(k);
		ret += (n+1) * bList.get(k).getPlaytime();
		ret = ret - bList.get(k).getPlaytime() * bList.get(k).getRate() * bList.get(k).getalphan(n);
		//System.out.println("n= " + n + " -> " + bList.get(k).getalphan(n));
		return ret;
	}
	
	public double getBufferLengthWithBlocknFrag(Block k, Fragment n)
	{
		double ret = getBlockStartBufferLength(k.getId());
		int index = k.getFragList().indexOf(n);
		ret += (index+1) * bList.get(k.getId()).getPlaytime();
		ret = ret - bList.get(k.getId()).getPlaytime() * bList.get(k.getId()).getRate() * bList.get(k.getId()).getalphan(index);
		//System.out.println("n= " + n + " -> " + bList.get(k).getalphan(n));
		return ret;
	}
	
	public double getBlockStartBufferLength(int k)
	{
		double ret = initBufferLength;
		if(k==0)
			return ret;
		
		ret = getBlockEndBufferLength(k-1);
		return ret;
	}
	
	public double getBlockEndBufferLength(int k)
	{
		double ret = getBlockStartBufferLength(k);
		ret += bList.get(k).getFragList().size() * bList.get(k).getPlaytime();
		//System.out.println("k = "+ k + " full buffer = " + ret);
		
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
	
	public double getBlockEndBufferLength(Block k)
	{
		double ret = getBlockStartBufferLength(bList.indexOf(k));
		ret += k.getFragList().size() * k.getPlaytime();
		//System.out.println("k = "+ k + " full buffer = " + ret);
		
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
		double download = k.getDownloadDur(k.getFragNum()-1);
		ret -= download;
		return ret;
	}
	
	public double getDownloadDur() {
		return downloadDur;
	}
	public void setDownloadDur(double downloadDur) {
		this.downloadDur = downloadDur;
	}
	public List<Block> getbList() {
		return bList;
	}
	public void setbList(List<Block> bList) {
		this.bList = bList;
	}
	public double getInitBufferLength() {
		return initBufferLength;
	}
	public void setInitBufferLength(double initBufferLength) {
		this.initBufferLength = initBufferLength;
	}
}
