package type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerList {
	List<Server> lserver = new ArrayList<Server>();
	public ServerList(double [] s)
	{
		int l = s.length;
		for(int i=0;i<l;i++)
		{
			lserver.add(new Server(i+1,"",s[i]));
		}
		ServerCompartor sc = new ServerCompartor();
		Collections.sort(lserver,sc);
		System.out.println("Server list inited done!");
		for(int i=0;i<lserver.size();i++)
		{
			Server ser = lserver.get(i);
			System.out.println(i + ":" + ser.getId() + " - - - " + ser.getBandwidth());
		}
	}
	public List<Server> getLserver() {
		return lserver;
	}
	public void setLserver(List<Server> lserver) {
		this.lserver = lserver;
	}
	
}
