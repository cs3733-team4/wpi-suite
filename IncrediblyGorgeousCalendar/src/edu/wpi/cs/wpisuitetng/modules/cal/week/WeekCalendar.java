package edu.wpi.cs.wpisuitetng.modules.cal.week;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.AbstractCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.day.DrawnDay;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Months;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public class WeekCalendar extends AbstractCalendar {
	

	// does this make sense? if not it should be pretty easy to change, just a skeleton for now.
	//DrawnDay[] calendar = new DrawnDay[7];
	
	JPanel title = new JPanel(),
		   body  = new JPanel();
	
	DateTime time;
	
	public WeekCalendar(DateTime time)
	{
		this.time = time;
		this.body.setLayout(new GridLayout(1,7));
	}
	
	public void update()
	{
		MutableDateTime mdt = new MutableDateTime(time);
		mdt.addDays(1 - time.getDayOfWeek());
		DateTime startOfWeek = mdt.toDateTime();
		
		this.removeAll();
		
		for(int i = 0; i < 7; i++)
		{
			JPanel p = new JPanel();
			p.add(new JLabel(startOfWeek.dayOfWeek().toString()), BorderLayout.NORTH);
			p.add(new DrawnDay(startOfWeek,0));
			this.add(p);
			
			startOfWeek = Months.nextDay(startOfWeek);
		}
		
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
	public void updateEvents(Event events, boolean addOrRemove) {
		// TODO Auto-generated method stub
		
	}
	
	
	
}
