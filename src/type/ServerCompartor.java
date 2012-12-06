package type;

import java.util.Comparator;

public class ServerCompartor implements Comparator<Server> {
	private int time = 0;
	public ServerCompartor (int t)
	{
		time = t;
	}
	@Override
	public int compare(Server o1, Server o2) {
		// TODO Auto-generated method stub
		if(o1.getBandAvgwidth(time) < o2.getBandAvgwidth(time)){
			return 1;
		}else if(o1.getBandAvgwidth(time) == o2.getBandAvgwidth(time)){
			return 0;
		}else{
			return -1;
		}
	}

}
