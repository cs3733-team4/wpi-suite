package edu.wpi.cs.wpisuitetng.modules.cal.day;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;

public class DayGridLabel extends JPanel{
	
	public DayGridLabel()
	{
		this.setLayout(new GridLayout(24, 1));
		this.setBackground(Colors.TABLE_BACKGROUND);
		
		for(int i = 0; i < 24; i++)
		{
			int hour = i%12==0?12:i%12;
			String padding = (hour < 10) ? "     " : "    ";
			JLabel text = new JLabel(new StringBuilder().append(hour)
														.append(":00")
                    									.append(padding)
                    									.toString());
			text.setBackground(Colors.TABLE_BACKGROUND);
			text.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Colors.BORDER));
			this.add(text);
		}
	}
	
}
