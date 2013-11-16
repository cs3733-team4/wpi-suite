package edu.wpi.cs.wpisuitetng.modules.cal.us;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.Calendar;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

@SuppressWarnings("serial")
public class Visualiser extends JFrame{
	
	public static void main(String[] args)
	{	
		new Visualiser();
	}
	
	
	public static void popupateTest()
	{
		
	}
	

	public Visualiser()
	{
		JComponent calendar = new Calendar().getTabs().get(0).getMainComponent();
		JPanel p = new JPanel();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e) {
			System.out.println("Error setting UI manager to cross-platform!");
			e.printStackTrace();
		}
		
		p.setLayout(new GridLayout(1,1));
		this.setLayout(new GridLayout(1,1));
		this.add(p);
		p.add(calendar);
		this.setSize(new Dimension(1200, 900));
		this.setVisible(true);


		List<Event> gni = gne();
		((MainPanel) calendar).getMOCA().addEvents(gni);;
		//((MainPanel) calendar).getMOCA().removeEvents(gni);

		//System.out.println(calendar.getClass());
	}
	
	public List<Event> gne()
	{
		List<Event> events = new ArrayList<Event>();
		events.add(new Event("kill people", "killing time!", new DateTime(2013, 11, 14, 18, 0), new DateTime(2013, 11, 14, 18, 50), false, new Project("null", "null"), 0));
		events.add(new Event("rehab", "killing time!", new DateTime(2013, 11, 14, 20, 0), new DateTime(2013, 11, 14, 20, 50), false, new Project("null", "null"), 0));
		events.add(new Event("cook meth", "get $$$", new DateTime(2013, 11, 14, 21, 0), new DateTime(2013, 11, 14, 21, 50), false, new Project("null", "null"), 0));
		events.add(new Event("sleep", "die", new DateTime(2013, 11, 14, 22, 0), new DateTime(2013, 11, 14, 22, 50), false, new Project("null", "null"), 0));

		//DateTime dt = new DateTime(2013, 11, 14, 18, 0);
		return events;
	}
}
