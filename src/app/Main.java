package app;

import java.io.BufferedReader;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;
import com.panayotis.iodebug.Debug;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;


import type.*;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DecimalFormat dcmFmt = new DecimalFormat("0.00");
		int tatolFragNum = 0;
		int maxLengthBlock = 5;
		double playBackTime = 5;
		double maxPlayBackTime = 1000;
		double qmin = 15;
		double qmax = 50;
		
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

		
		/*double [][]sbww = {{1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000,1000},
				{150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150,150},
				{100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100,100},
				{500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500,500},
				{900,900,900,900,900,900,900,900,900,900,900,900,900,900,900,900,900,900,900,900}};*/
		ServerList slist = new ServerList(loadBandwidth());
		//ServerList slist = new ServerList(sbww);
		//init Block List
		List<Block> bList = new ArrayList<Block>();
		
		Timeline tline = new Timeline(slist.bwLength, slist.getLserver().size());
		
		//init buffer
		Buffer buffer = new Buffer(bList);
		
		double maxBufferLength = 0;
		double minBufferLength = 99999;
		
		int nowPlay = 0;
		int nowRate = 0;
		double timedownloadDur = 0;
		Block nowBlock = new Block(buffer,rateList,slist,maxLengthBlock, nowRate, bList, tline);
		nowBlock.setQmax(qmax);
		nowBlock.setQmin(qmin);
		bList.add(nowBlock);
		nowBlock.init();
		maxBufferLength = buffer.getBlockEndBufferLength(nowPlay);
		minBufferLength = buffer.getBlockEndBufferLength(nowPlay);
		//System.out.println("buffer(Block"+nowPlay+"End) = " + nowBlock.getDownloadDur(nowBlock.getFragNum()-1) + " -> " + buffer.getBlockEndBufferLength(nowPlay));
		//System.out.println(nowRate + " " + nowBlock.getDownloadDur(nowBlock.getFragNum()-1) + " " +buffer.getBlockEndBufferLength(nowPlay)+" ");
		timedownloadDur += nowBlock.getDownloadDur(nowBlock.getFragNum()-1);
		tatolFragNum += nowBlock.getFragNum();
		nowRate = nowBlock.getNewRate();
		
		//maxPlayBackTime -= buffer.getBlockEndBufferLength(nowPlay);
		while(bList.size()<134)
		//for(int i=0;i<10;i++)
		{
			//int startTime = 0;
			slist.resort((int)Math.floor(nowBlock.getDownloadEndTime()));
			nowBlock = new Block(buffer,rateList,slist,maxLengthBlock, nowRate, bList, tline);
			nowBlock.setQmax(qmax);
			nowBlock.setQmin(qmin);
			bList.add(nowBlock);
			nowBlock.init();
			if(buffer.getBlockEndBufferLength(nowPlay) > maxBufferLength)
				maxBufferLength = buffer.getBlockEndBufferLength(nowPlay);
			if(buffer.getBlockEndBufferLength(nowPlay) < minBufferLength)
				minBufferLength = buffer.getBlockEndBufferLength(nowPlay);
			nowPlay++;
			//System.out.println("buffer(Block"+nowPlay+"End) = " + nowBlock.getDownloadDur(nowBlock.getFragNum()-1) + " -> " + buffer.getBlockEndBufferLength(nowPlay));
			//System.out.println(nowRate + " " + nowBlock.getDownloadDur(nowBlock.getFragNum()-1) + " " +buffer.getBlockEndBufferLength(nowPlay)+" ");
			timedownloadDur += nowBlock.getDownloadDur(nowBlock.getFragNum()-1);
			nowRate = nowBlock.getNewRate();
			
			tatolFragNum += nowBlock.getFragNum();
		}
		
		//System.out.println("maxBufferLength = " + maxBufferLength + " , minBufferLength = " + minBufferLength);
		//System.out.println("tatolFragNum = " + tatolFragNum);
		//tline.printServerAll();
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
			int decnt = 0;
			while(line!=null)
			{
				tokenizer = new StringTokenizer(line);
				
				
				
				if(cnt>=600 && cnt<750){
					ret[0][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.0;
					ret[1][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.5;
					ret[2][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.9;
					ret[3][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.5;
				}
				else if(cnt>=800 && cnt<1300){
					ret[0][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.8;
					ret[1][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.7;
					ret[2][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.9;
					ret[3][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.0;
				}
				else if(cnt>=1500 && cnt<1800){
					ret[0][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.9;
					ret[1][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.6;
					ret[2][cnt] = Double.parseDouble(tokenizer.nextToken()) * 3.0;
					ret[3][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.5;
				}
				else
				{
					ret[0][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.8;
					ret[1][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.5;
					ret[2][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.9;
					ret[3][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.5;
				}
				
				/*if(cnt>=0 && cnt<500){
					ret[0][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.8;
					ret[1][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.5;
					ret[2][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.9;
					ret[3][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.5;
				}
				else if(cnt>=500 && cnt<750){
					ret[0][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.9;
					ret[1][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.1;
					ret[2][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.7;
					ret[3][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.2;
				}
				else if(cnt>=750 && cnt<1000){
					ret[0][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.0;
					ret[1][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.5;
					ret[2][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.0;
					ret[3][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.0;
				}
				else if(cnt>=1000 && cnt<1500){
					ret[0][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.3;
					ret[1][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.7;
					ret[2][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.4;
					ret[3][cnt] = Double.parseDouble(tokenizer.nextToken()) * 1.7;
				}
				else if(cnt>=1500){
					ret[0][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.8;
					ret[1][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.5;
					ret[2][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.9;
					ret[3][cnt] = Double.parseDouble(tokenizer.nextToken()) * 2.5;
				}*/
				
				
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
		}*/
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
