package draw;

import java.awt.Graphics;

import javax.swing.JFrame;

public   class   DrawAxis   extends   JFrame   {      
	public   DrawAxis()   {        
		super("MyAxis");      
		setSize(600,   600);      
		setVisible(true);        
	}      
	public   void   paint(Graphics   g){      
		//由外部用户给出参数      
		Axis   axisX=new   Axis();      
		axisX.setTickX(50);      
		axisX.setTickY(150);      
		axisX.setTickCountX(25);     
		axisX.setTickCountY(5);  
		axisX.setTickLengthX(500);    
		axisX.setTickLengthY(100);  
		axisX.setTickStepX(20);   
		axisX.setTickStepY(20);   

		int   XtickX=axisX.getTickX();      
		System.out.println("sss"+XtickX);      
		int   XtickY=axisX.getTickY();      
		int   XtickLength=axisX.getTickLengthX();      
		int   XtickCount=axisX.getTickCountX();      
		int   XtickStep=axisX.getTickStepX();      
		int   XtickLengthY=axisX.getTickLengthY();      
		int   XtickCountY=axisX.getTickCountY();      
		int   XtickStepY=axisX.getTickStepY();   
		int   tickHeight=5;      
		//画横轴      
		g.drawLine(XtickX,   XtickY,XtickX+XtickLength,   XtickY);      
		for(int   i=0;i<XtickCount;i++){      
			g.drawLine(XtickX+i*XtickStep,XtickY,XtickX+i*XtickStep,XtickY-tickHeight);      
		}      

		//画纵轴      
		g.drawLine(XtickX,   XtickY,XtickX,XtickY-XtickLengthY);          
		for(int   i=0;i<XtickCountY;i++){      
			g.drawLine(XtickX,XtickY-i*XtickStepY,XtickX+tickHeight,XtickY-i*XtickStepY)   ;                                  
		}      


		int[]   x   =   new   int[]   {   100,200   };                
		int[]   y   =   new   int[]   {   100,50   };      

		g.drawPolyline(x,y,x.length);      
		//画曲线      

	}      
	public   static   void   main(String   []   args){      
		DrawAxis   a=new   DrawAxis();      
	}      
}