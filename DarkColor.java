package calendar;

import java.awt.Color;

import javax.swing.JButton;

public class DarkColor implements ButtonColor {
	
	public void changeColor(JButton b)
	{
		b.setBackground(Color.BLACK);
		b.setForeground(Color.WHITE);
	}

}
