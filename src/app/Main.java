package app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import type.*;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int tatolFragNum = 100;
		int maxLengthBlock = 10;
		double maxPlayBackTime = 1000;
		
		//init video list
		int[] r = {100,300,500,800,1500};
		VideoRateList rateList = new VideoRateList(5,r);
		
		//init server list
		double []sbw = {300,900,200,1400,2500};
		ServerList slist = new ServerList(sbw);
		
		//init buffer
		Buffer buffer = new Buffer();
		
		//init Block List
		List<Block> bList = new ArrayList<Block>();
		
		//while(maxPlayBackTime!=buffer.getLengthSec())
		{
			Block nowBlock = new Block(buffer,slist,maxLengthBlock);
			
		}
	}
	
	/*
	 * Flow
	 * - init server list
	 * - init connection
	 * - get bandwidth
	 * - server list ordered
	 * - loop
	 * - - update block length
	 * - - schedule frag req
	 * - - buffer
	 * - loop end
	
	
	*/

}
