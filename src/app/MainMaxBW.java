package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import type.Block;
import type.Buffer;
import type.ServerList;
import type.Timeline;
import type.VideoRateList;

public class MainMaxBW {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DecimalFormat dcmFmt = new DecimalFormat("0.00");
		int tatolFragNum = 100;
		int maxLengthBlock = 10;
		double playBackTime = 5;
		double maxPlayBackTime = 1000;
		double qmin = 15;
		double qmax = 50;
		
		double maxBufferLength = 0;
		double minBufferLength = 99999;
		
		//init video list
		int[] r = {300,700,1500,2500,3500};
		VideoRateList rateList = new VideoRateList(5,r,playBackTime);
		
		//init server list
		double []sbw = {300,900,200,1400,2500};
		/*double [][]sbww = {{100,200,300,300,350,280,250,290,310,220},
						   {600,800,900,950,900,880,860,980,790,830},
						   {90,100,120,130,80,70,100,150,70,90},
						   {900,1300,1500,1400,1450,1350,1290,1370,1440,1300},
						   {1600,1900,2400,2450,2590,2610,2700,2500,2600,2510}};*/
		double [][]sbww = {{1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000},
				{150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150},
				{100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100},
				{500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500},
				{900,900,900,900,900,900,900,900,900,900,900,900,900,900,900,900,900,900,900,900}};
		ServerList slist = new ServerList(loadBandwidth());
		//ServerList slist = new ServerList(sbww);
		//init Block List
		List<Block> bList = new ArrayList<Block>();
		
		Timeline tline = new Timeline(slist.bwLength, slist.getLserver().size());
		//init buffer
		Buffer buffer = new Buffer(bList);
		
		int nowPlay = 0;
		int nowRate = 0;
		
		Block nowBlock = new Block(buffer,rateList,slist,maxLengthBlock, nowRate, bList, tline);
		nowBlock.setQmax(qmax);
		nowBlock.setQmin(qmin);
		nowBlock.setServerSize(1);
		bList.add(nowBlock);
		nowBlock.initBW();
		//System.out.println("buffer(Block"+nowPlay+"End) = " + nowBlock.getDownloadDur(nowBlock.getFragNum()-1) + " -> " + buffer.getBlockEndBufferLength(nowPlay));
		//System.out.println(nowRate + " " + nowBlock.getDownloadDur(nowBlock.getFragNum()-1) + " " +buffer.getBlockEndBufferLength(nowPlay)+" ");
		maxBufferLength = buffer.getBlockEndBufferLength(nowPlay);
		minBufferLength = buffer.getBlockEndBufferLength(nowPlay);
		nowRate = nowBlock.getNewRate();
		
		//maxPlayBackTime -= buffer.getBlockEndBufferLength(nowPlay);
		while(bList.size()<370)
		//for(int i=0;i<10;i++)
		{
			//int startTime = 0;
			slist.resort((int)Math.floor(nowBlock.getDownloadEndTime()));
			nowBlock = new Block(buffer,rateList,slist,maxLengthBlock, nowRate, bList, tline);
			nowBlock.setQmax(qmax);
			nowBlock.setQmin(qmin);
			nowBlock.setServerSize(1);
			bList.add(nowBlock);
			nowBlock.initBW();
			if(buffer.getBlockEndBufferLength(nowPlay) > maxBufferLength)
				maxBufferLength = buffer.getBlockEndBufferLength(nowPlay);
			if(buffer.getBlockEndBufferLength(nowPlay) < minBufferLength)
				minBufferLength = buffer.getBlockEndBufferLength(nowPlay);
			nowPlay++;
			
			
			
			//System.out.println("buffer(Block"+nowPlay+"End) = " + nowBlock.getDownloadDur(nowBlock.getFragNum()-1) + " -> " + buffer.getBlockEndBufferLength(nowPlay));
			//System.out.println(nowRate + " " + nowBlock.getDownloadDur(nowBlock.getFragNum()-1) + " " +buffer.getBlockEndBufferLength(nowPlay)+" ");
			nowRate = nowBlock.getNewRate();
		}
		//System.out.println("maxBufferLength = " + maxBufferLength + " , minBufferLength = " + minBufferLength);
	}
	
	public static double [][] loadBandwidth()
	{
		double[][] ret = new double[4][2001];
		File file = new File("bandwidth.txt");
		FileReader fr;
		StringTokenizer tokenizer;
		int cnt = 0;
		try {
			fr = new FileReader(file);
			BufferedReader inFile = new BufferedReader(fr);
			String line = inFile.readLine();
			while(line!=null)
			{
				tokenizer = new StringTokenizer(line);
				if(cnt>=0 && cnt<250){
					ret[0][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.4;
					ret[1][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.8;
					ret[2][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.4;
					ret[3][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.7;
				}
				else if(cnt>=250 && cnt<550){
					ret[0][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.2;
					ret[1][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.0;
					ret[2][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.3;
					ret[3][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.2;
				}
				else if(cnt>=550 && cnt<800){
					ret[0][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.0;
					ret[1][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.5;
					ret[2][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.0;
					ret[3][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.0;
				}
				else if(cnt>=800 && cnt<1300){
					ret[0][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.1;
					ret[1][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.2;
					ret[2][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.3;
					ret[3][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.4;
				}
				else if(cnt>=1300){
					ret[0][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.1;
					ret[1][cnt] = Double.parseDouble(tokenizer.nextToken()) * 5.2;
					ret[2][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.1;
					ret[3][cnt] = Double.parseDouble(tokenizer.nextToken()) * 3.9;
				}
				line = inFile.readLine();
				cnt++;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}   
		/*
		for(int i=0;i<10;i++)
		{
			System.out.println(ret[0][i]);
			System.out.println(ret[1][i]);
			System.out.println(ret[2][i]);
			System.out.println(ret[3][i]);
		}
		*/
		return ret;
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
