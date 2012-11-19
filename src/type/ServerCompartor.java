package type;

import java.util.Comparator;

public class ServerCompartor implements Comparator<Server> {

	@Override
	public int compare(Server o1, Server o2) {
		// TODO Auto-generated method stub
		if(o1.getBandAvgwidth() < o2.getBandAvgwidth()){
			return 1;
		}else if(o1.getBandAvgwidth() == o2.getBandAvgwidth()){
			return 0;
		}else{
			return -1;
		}
	}

}
