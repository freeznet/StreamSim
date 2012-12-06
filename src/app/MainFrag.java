package app;

import java.io.BufferedReader;
import com.panayotis.gnuplot.JavaPlot;
import com.panayotis.gnuplot.plot.DataSetPlot;

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


import fragOnly.*;

public class MainFrag {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DecimalFormat dcmFmt = new DecimalFormat("0.00");
		int tatolFragNum = 100;
		int maxLengthBlock = 10;
		double playBackTime = 5;
		double maxPlayBackTime = 1000;
		double qmin = 15;
		double qmax = 50;

		//init video list
		int[] r = {300,700,1500,2500,3500};
		VideoRateList rateList = new VideoRateList(5,r,playBackTime);
		
		//init server list
		ServerList slist = new ServerList(loadBandwidth());

		List<Fragment> fragList = new ArrayList<Fragment>();

		Buffer buffer = new Buffer(fragList);
		
		int nowPlay = 0;
		int nowRate = 0;
		double timedownloadDur = 0;
		
		int fragCnt = 0;
		//Fragment nowFrag = new Fragment(fragCnt, buffer, slist, rateList, fragList, downBy, nowRate);
		for(int i=0;i<50;i++)
		{
			Server s = slist.getAvailableServer();
			Fragment nowFrag = new Fragment(fragCnt, buffer, slist, rateList, fragList, s, nowRate);
			fragCnt++;
			nowFrag.setQmax(qmax);
			nowFrag.setQmin(qmin);
			fragList.add(nowFrag);
			nowFrag.getDownloadDur();
		}
		
		for(Fragment f : fragList)
		{
			f.printLog();
		}
		
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
				ret[0][cnt] = Double.parseDouble(tokenizer.nextToken()) * 4;
				ret[1][cnt] = Double.parseDouble(tokenizer.nextToken()) * 4;
				ret[2][cnt] = Double.parseDouble(tokenizer.nextToken()) * 4;
				ret[3][cnt] = Double.parseDouble(tokenizer.nextToken()) * 4;
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
