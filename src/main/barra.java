package main;

import javax.swing.*;
 
public class barra extends JProgressBar{
	private static final long serialVersionUID = 1L;
			
	public barra(){
        setMinimum(0);
        setStringPainted(true);
        setBounds(560, 15, 230, 26);
	}
	
	public void setMax(int max){
		setMaximum(max);
	}
}