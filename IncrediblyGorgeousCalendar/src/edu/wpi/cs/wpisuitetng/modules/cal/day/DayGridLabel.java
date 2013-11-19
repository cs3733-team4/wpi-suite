package edu.wpi.cs.wpisuitetng.modules.cal.day;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class DayGridLabel extends JPanel{
	
	public DayGridLabel()
	{
		this.setLayout(new GridLayout(48, 1));
		
		for(int i = 0; i<48; i++)
		{
			int rawHour = i/2;
			int displayedHour = rawHour/2==0 ? 12 : rawHour/2;
			String halfHour = rawHour*2 == i ? ":00" : ":30";
			this.add(new JLabel(new StringBuilder().append(displayedHour).append(halfHour).toString()));
		}
	}
	
}
