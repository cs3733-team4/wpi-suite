package edu.wpi.cs.wpisuitetng.modules.cal.year;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MiniMonth extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 153498539485L;
	
	public DayButton[] months;
	
	
	public MiniMonth(int monthNumber, int yearNumber)
	{
		int startingDay =  (37 + (yearNumber-1) + (yearNumber-1)/4 + (yearNumber-1)/400 - (yearNumber-1)/100) % 7;
		startingDay = startingDay==0?7:startingDay;
		//do something
	}
	
	
	
	
	
	
	
	
	private interface DayButton{}
	
	private class ActiveDayButton extends JButton implements DayButton
	{
		public ActiveDayButton(int day)
		{
			this.setEnabled(true);
			this.setText(new StringBuilder(day).toString());
			
			this.setBackground(Color.white);
			this.setForeground(Color.black);
			this.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(5, 15, 5, 15)));
			this.setText(new StringBuilder(day).toString());
		}
	}
	
	private class InactiveDayButton extends JButton implements DayButton
	{
		public InactiveDayButton(int day)
		{
			this.setEnabled(false);
			this.setText(new StringBuilder(day).toString());
			
			this.setBackground(Color.gray);
			this.setForeground(Color.black);
			this.setBorder(new CompoundBorder(new LineBorder(Color.black), new EmptyBorder(5, 15, 5, 15)));
			this.setText(new StringBuilder(day).toString());
		}
	}

}
