package draw;


import   java.awt.*;      
import   java.awt.event.*;      
import   javax.swing.*;      
import   java.io.*;      

//������      
class   Axis{      

	private   int   tickX;//������      
	private   int   tickY;//������      
	private   int   tickLengthX;//�����᳤��      
	private   int   tickLengthY;//�����᳤��      
	private   int   tickCountX;//�̶ȵĸ���      
	private   int   tickCountY;//�̶ȵĸ��� 
	private   int   tickStepX;//�̶ȵĲ���      
	private   int   tickStepY;//�̶ȵĲ���     

	int   getTickX(){      
		return   tickX;      
	}      

	void   setTickX(int   tickX   ){      
		this.tickX=tickX;      
	}      
	int   getTickY(){      
		return   tickY;      
	}      
	void   setTickY(int   tickY){      
		this.tickY=tickY;      
	}

	public int getTickLengthX() {
		return tickLengthX;
	}

	public void setTickLengthX(int tickLengthX) {
		this.tickLengthX = tickLengthX;
	}

	public int getTickLengthY() {
		return tickLengthY;
	}

	public void setTickLengthY(int tickLengthY) {
		this.tickLengthY = tickLengthY;
	}

	public int getTickCountX() {
		return tickCountX;
	}

	public void setTickCountX(int tickCountX) {
		this.tickCountX = tickCountX;
	}

	public int getTickCountY() {
		return tickCountY;
	}

	public void setTickCountY(int tickCountY) {
		this.tickCountY = tickCountY;
	}

	public int getTickStepX() {
		return tickStepX;
	}

	public void setTickStepX(int tickStepX) {
		this.tickStepX = tickStepX;
	}

	public int getTickStepY() {
		return tickStepY;
	}

	public void setTickStepY(int tickStepY) {
		this.tickStepY = tickStepY;
	}      

  

}       
