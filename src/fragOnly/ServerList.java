package fragOnly;

import java.util.ArrayList;
import java.util.List;

import fragOnly.Server;

public class ServerList {

	List<Server> lserver = new ArrayList<Server>();
	public int bwLength = 0;
	public ServerList(double [][] s)
	{
		int l = s.length;
		bwLength = s[0].length;
		for(int i=0;i<l;i++)
		{
			lserver.add(new Server(i+1,"",s[i]));
		}
	}
	public List<Server> getLserver() {
		return lserver;
	}
	
	public Server getServer(int i)
	{
		return lserver.get(i);
	}
	
	public void setLserver(List<Server> lserver) {
		this.lserver = lserver;
	}
	
	public Server getAvailableServer()
	{
		double avaTime = lserver.get(0).getNextAvailableTime();
		Server avaServer = lserver.get(0);
		for(Server s : lserver)
		{
			if(s.getNextAvailableTime() < avaTime)
			{
				//System.out.println("avaTime = "+avaTime + " ,newavaTime = " + s.getNextAvailableTime());
				avaTime = s.getNextAvailableTime();
				avaServer = s;
			}
		}
		return avaServer;
	}
	public List<Server> getDoneServer(int time) {
		List<Server> ret = new ArrayList<Server>();
		return null;
	}
	
	

}
