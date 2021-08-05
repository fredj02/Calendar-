package calendar;

import java.awt.Color;

import javax.swing.JButton;

public class LightColor implements ButtonColor{
	
	public void changeColor(JButton b)
	{
		b.setBackground(Color.WHITE);
		b.setForeground(Color.BLACK);
	}

}
