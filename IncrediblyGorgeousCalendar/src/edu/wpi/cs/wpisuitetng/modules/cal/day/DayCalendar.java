package edu.wpi.cs.wpisuitetng.modules.cal.day;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.AbstractCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public class DayCalendar extends AbstractCalendar {

	private JPanel inside                = new JPanel(), 
			       top                   = new JPanel(), 
			       navigationPanel       = new JPanel(),
				   title                 = new JPanel();
	
	private JButton nextButton           = new JButton(">"), 
	        		previousButton       = new JButton("<"), 
	        		todayButton          = new JButton("Today");
	
	private DateTime time;
	private MainPanel mainPanel;
	private DrawnDay current;
	
	private List<Event> events;
	
	public DayCalendar(DateTime on, MainPanel mainPanel)
	{
		this.mainPanel = mainPanel;
		this.time = on;
		this.setLayout(new BorderLayout());
		
		this.navigationPanel.add(this.previousButton);
		this.navigationPanel.add(this.todayButton);
		this.navigationPanel.add(this.nextButton);
		
		this.title.add(new JLabel(String.valueOf(time.getDayOfMonth())));
		
		this.top.add(this.navigationPanel);
		this.top.add(this.title);
		
		this.add(top, BorderLayout.NORTH);
		this.add(this.inside, BorderLayout.CENTER);
		
		//this.generateDay();
	}
	
	private void generateDay()
	{
		this.current = new DrawnDay(this.time);
		this.current.addEvents(events);
		
		this.inside.removeAll();
		

		this.inside.add(new DayGridLabel(), BorderLayout.WEST);
		this.inside.add(this.current, BorderLayout.CENTER);
	}
	
	@Override
	public void next()
	{
		this.time = Months.nextDay(this.time);
		this.generateDay();
	}

	@Override
	public void previous() {
		this.time = Months.prevDay(this.time);
		this.generateDay();

	}

	@Override
	public void display(DateTime newTime) {
		this.time = newTime;
		this.generateDay();
	}

	@Override
	public void updateEvents(List<Event> events, boolean addOrRemove) {
		if (addOrRemove)
		{
			this.events.addAll(events);
		}
		else
		{
			this.events.removeAll(events);
		}
		this.generateDay();
	}
}
