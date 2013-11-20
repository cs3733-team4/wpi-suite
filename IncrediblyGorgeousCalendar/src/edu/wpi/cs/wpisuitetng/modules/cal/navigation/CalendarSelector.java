package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.Box.Filler;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;

public class CalendarSelector extends JPanel
{
	public CalendarSelector()
	{
		// create components
		JToggleButton personalCalendar = new JToggleButton("Personal");
		JToggleButton teamCalendar = new JToggleButton("Team"),
				bothCalendar = new JToggleButton("Both"),
				month = new JToggleButton("Month"),
				day = new JToggleButton("Day");
		Filler filler1 = new Filler(new Dimension(6, 0), new Dimension(6, 0), new Dimension(6, 32767));

		// build button groups
		ButtonGroup view = new ButtonGroup();
		view.add(month);
		view.add(day);
		month.setSelected(true);
		ButtonGroup cal = new ButtonGroup();
		cal.add(personalCalendar);
		cal.add(teamCalendar);
		cal.add(bothCalendar);
		personalCalendar.setSelected(true);

		// layout
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		add(month);
		add(day);
		add(filler1);
		this.add(personalCalendar);
		this.add(teamCalendar);
		this.add(bothCalendar);
		
		//"logic"

		day.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{MainPanel.getInstance().viewDay();}
		});
		month.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e)
			{MainPanel.getInstance().viewMonth();}
		});
		
		personalCalendar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0){
				MainPanel.getInstance().showPersonal=true;
				MainPanel.getInstance().showTeam=false;
			}
		});
		teamCalendar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0){
				MainPanel.getInstance().showTeam=true;
				MainPanel.getInstance().showPersonal=false;
			}
		});
		bothCalendar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				MainPanel.getInstance().showPersonal=true;
				MainPanel.getInstance().showTeam=true;
			}
		});
	}

}