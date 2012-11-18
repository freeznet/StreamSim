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
		double playBackTime = 5;
		double maxPlayBackTime = 1000;
		double qmin = 5;
		double qmax = 45;
		
		//init video list
		int[] r = {300,700,1500,2500,3500};
		VideoRateList rateList = new VideoRateList(5,r,playBackTime);
		
		//init server list
		double []sbw = {300,900,200,1400,2500};
		ServerList slist = new ServerList(sbw);
		
		//init Block List
		List<Block> bList = new ArrayList<Block>();
		
		//init buffer
		Buffer buffer = new Buffer(bList);
		
		
		
		//while(maxPlayBackTime!=buffer.getLengthSec())
		{
			Block nowBlock = new Block(buffer,rateList,slist,maxLengthBlock, 0);
			bList.add(nowBlock);
			System.out.println("buffer(Block1End) = " + buffer.getBlockEndBufferLength(0));
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
