package edu.wpi.cs.wpisuitetng.modules.cal.week;

import java.util.List;

import javax.swing.JPanel;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.CalendarInterface;
import edu.wpi.cs.wpisuitetng.modules.cal.day.DrawnDay;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public class WeekCalendar extends JPanel implements CalendarInterface {
	
	private static final long serialVersionUID = -1865495619015734948L;
	
	// does this make sense? if not it should be pretty easy to change, just a skeleton for now.
	DrawnDay[] calendar = new DrawnDay[7];
	
	
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
