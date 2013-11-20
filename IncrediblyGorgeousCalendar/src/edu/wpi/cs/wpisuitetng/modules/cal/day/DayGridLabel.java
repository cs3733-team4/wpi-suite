package edu.wpi.cs.wpisuitetng.modules.cal.day;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;

public class DayGridLabel extends JPanel{
	
	public DayGridLabel()
	{
		this.setLayout(new GridLayout(48, 1));
		this.setBackground(Colors.TABLE_BACKGROUND);
		
		for(int i = 0; i < 48; i++)
		{
			int rawHour = i / 2;
			int displayedHour = Math.round((float) rawHour) == 0 ? 12 : rawHour;
			displayedHour = displayedHour > 12 ? displayedHour - 12 : displayedHour;
			String halfHour = rawHour * 2 == i ? ":00" : ":30";
			String padding = (displayedHour < 10) ? "     " : "    ";
			JLabel text = new JLabel(new StringBuilder().append(displayedHour)
                    									.append(halfHour)
                    									.append(padding)
                    									.toString());
			text.setBackground(Colors.TABLE_BACKGROUND);
			text.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 3, Colors.BORDER));
			this.add(text);
		}
	}
	
}
