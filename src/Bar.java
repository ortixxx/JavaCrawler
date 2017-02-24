import javax.swing.*;
 
public class Bar extends JProgressBar{
	private static final long serialVersionUID = 1L;
			
	public Bar(){
        setMinimum(0);
        setStringPainted(true);
        setBounds(560, 5, 230, 26);
	}
	
	public void setMax(int max){
		setMaximum(max);
	}
}