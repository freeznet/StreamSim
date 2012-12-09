package fragOnly;

import java.util.ArrayList;
import java.util.List;

public class History {
	private List<Info> infoList = new ArrayList<Info>();
	
	public History(){}
	
	public void addInfo(Info i)
	{
		boolean add = true;
		for(Info t : infoList)
		{
			if(t.getFragID()==i.getFragID()){
				add = false;
				break;
			}
		}
		
		if(add)
		{
			infoList.add(i);
		}
	}
	
	public Info getInfo(int i)
	{
		if(i>=infoList.size())
			return null;
		return infoList.get(i);
	}
	
	public Info getInfoByFragID(int i)
	{
		for(Info t : infoList)
		{
			if(t.getFragID()==i){
				return t;
			}
		}
		return null;
	}
	
	public void printMinMax()
	{
		double min = 999999;
		double max = -999999;
		for(Info t : infoList)
		{
			if(t.getEndDownBuffer() < min)
				min = t.getEndDownBuffer();
			if(t.getEndDownBuffer() > max)
				max = t.getEndDownBuffer();
		}
		System.out.println("Min Buffer = " + min + " , Max Buffer = " + max);
	}
	
	public void print()
	{
		for(Info t : infoList)
		{
			System.out.println(t.getDownServerID() + " " + "0 0"+" "+t.getFragID() + " " + Math.round(t.getEndDownTime()) + " " + t.getBitrate() + " " + t.getEndDownBuffer());
		}
	}
	
	public void printByServer()
	{
		for(int i=1;i<5;i++)
		{
			for(Info t : infoList)
			{
				if(t.getDownServerID()==i)
					System.out.println(t.getDownServerID() + " " + "0 0"+" "+t.getFragID() + " " + t.getEndDownTime() + " " + t.getBitrate() + " " + t.getEndDownBuffer());
			}
			System.out.println();
		}
	}
}
