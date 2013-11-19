package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import java.awt.BorderLayout;

import javax.swing.JCheckBox;
import javax.swing.JPanel;


public class CalendarSelector extends JPanel {
	
	private JCheckBox personalCalendar;
	private JCheckBox teamCalendar;
	
	
	public CalendarSelector(){
		
		this.personalCalendar = new JCheckBox("Personal");
		this.teamCalendar = new JCheckBox("Team");
	
		this.setLayout(new BorderLayout());
		
		this.add(personalCalendar, BorderLayout.WEST);
		this.add(teamCalendar, BorderLayout.EAST);
		
	}

}
