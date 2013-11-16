package edu.wpi.cs.wpisuitetng.modules.cal.day;

import java.util.List;

<<<<<<< HEAD
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
=======
import javax.swing.JLabel;
>>>>>>> 4db583b5171ca279103f0dd7a1c07c8f05ab3b79

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.CalendarInterface;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public class DayCalendar implements CalendarInterface {

	private static final long serialVersionUID = 234513472387324L;

	private JPanel inside                = new JPanel(), 
			       top                   = new JPanel(), 
			       mainCalendarView      = new JPanel(), 
			       navigationPanel       = new JPanel(), 
			       navigationButtonPanel = new JPanel();
	
	private JButton nextButton   = new JButton(">"), 
	        previousButton       = new JButton("<"), 
	        todayButton          = new JButton("Today");
	
	private JLabel monthLabel = new JLabel();
	private DateTime time;
	private MainPanel mainPanel;
	
	public DayCalendar(DateTime on, MainPanel mainPanel)
	{
		this.mainPanel = mainPanel;
		this.time = on;
		
		
	}
	@Override
	public void next() {
		// TODO Auto-generated method stub

	}

	@Override
	public void previous() {
		// TODO Auto-generated method stub

	}

	@Override
	public void display(DateTime newTime) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addEvent(Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addEvents(List<Event> eventList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeEvent(Event event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeEvents(List<Event> event) {
		// TODO Auto-generated method stub

	}

}
