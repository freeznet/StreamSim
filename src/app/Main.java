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
		double [][]sbww = {{100,200,300,300,350,280,250,290,310,220},
						   {600,800,900,950,900,880,860,980,790,830},
						   {90,100,120,130,80,70,100,150,70,90},
						   {900,1300,1500,1400,1450,1350,1290,1370,1440,1300},
						   {1600,1900,2400,2450,2590,2610,2700,2500,2600,2510}};
		ServerList slist = new ServerList(sbww);
		
		//init Block List
		List<Block> bList = new ArrayList<Block>();
		
		//init buffer
		Buffer buffer = new Buffer(bList);
		
		
		
		//while(maxPlayBackTime!=buffer.getLengthSec())
		{
			int startTime = 0;
			Block nowBlock = new Block(buffer,rateList,slist,maxLengthBlock, 0, bList);
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
