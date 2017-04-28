package main;

import javax.swing.*;

public class dialog extends JDialog{
	private static final long serialVersionUID = 1L;

	public dialog(){
		super(interfaz.getFrames()[0], true);
		
		setLayout(null);
		setSize(625, 80);
		setLocationRelativeTo(null);
		setLocation( getY()+17, getX()-170);
		setResizable(false);
	}
	
	public void removeAd(int frame){
		if(frame > 4)
			setTitle("Reglas");
		else
			setTitle("ABC periodicos");
		
		remove(interfaz.delete);
		remove(interfaz.cajaABC);
		remove(interfaz.insert);
		remove(interfaz.nuevoLink);
		remove(interfaz.panelAbc);
		remove(interfaz.panelInsert);
		remove(interfaz.sorter);
		
		addAd(frame);
	}
	
	public void addAd(int frame){
		interfaz.cajaABC.setLocation(490, 10);
		interfaz.panelAbc.setLocation(10, 50);
		interfaz.insert.setLocation(490, 10);
		setSize(625, 325);
		
		switch(frame){
		case 1: interfaz.nuevoLink.setSize(340, 30);
				add(interfaz.nuevoLink);
				interfaz.cajaABC.setLocation(360, 10);
				add(interfaz.cajaABC);
				add(interfaz.insert);
				setSize(625, 78);
				break;
		case 2: add(interfaz.sorter);
				add(interfaz.cajaABC);
				add(interfaz.panelAbc);
				add(interfaz.delete);
				break;
		case 3: add(interfaz.sorter);
				add(interfaz.cajaABC);
				add(interfaz.panelAbc);
				break;
		case 4: add(interfaz.cajaABC);
				interfaz.insert.setLocation(490, 258);
				add(interfaz.insert);
				add(interfaz.panelInsert);				
				break;
		case 5: interfaz.nuevoLink.setSize(470, 30);
				add(interfaz.nuevoLink);
				add(interfaz.insert);
				setSize(625, 78);
				break;
		case 6: interfaz.panelAbc.setLocation(10, 10);
				add(interfaz.panelAbc);
				setSize(495, 285);
				break;
		default: break;
		}
		
		setVisible(true);
	}
}
