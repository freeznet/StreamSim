package type;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
		ServerCompartor sc = new ServerCompartor(0);
		Collections.sort(lserver,sc);
		//System.out.println("Server list inited done!");
		/*for(int i=0;i<lserver.size();i++)
		{
			Server ser = lserver.get(i);
			System.out.println(i + ":" + ser.getId() + " - - - " + ser.getBandAvgwidth());
		}*/
	}
	public List<Server> getLserver() {
		return lserver;
	}
	public void setLserver(List<Server> lserver) {
		this.lserver = lserver;
	}
	
	public void resort(int t)
	{
		ServerCompartor sc = new ServerCompartor(t);
		Collections.sort(lserver,sc);
	}
	
}
