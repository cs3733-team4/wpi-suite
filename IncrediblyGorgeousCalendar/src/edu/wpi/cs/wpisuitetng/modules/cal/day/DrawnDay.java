package edu.wpi.cs.wpisuitetng.modules.cal.day;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public class DrawnDay extends JPanel{
	
	DateTime date;
	List<Event> events = new ArrayList<Event>();
	
	//one JPanel for each hour
	Hour[] hours = new Hour[24];
	
	public DrawnDay(DateTime d)
	{
		this.date = d;
		this.setLayout(new GridLayout(24, 1));
		for(int i = 0; i < 24; i++)
		{
			hours[i] = new Hour();
			hours[i].setBackground(Colors.TABLE_BACKGROUND);
			hours[i].setBorder(BorderFactory.createMatteBorder(0, 1, 1, 1, Colors.BORDER));
			this.add(hours[i]);
		}
	}
	
	public void draw()
	{
		for(Event e : events)
		{
			Color randomEventColor = new Color((int)(Math.random()*256), (int)(Math.random()*256), (int)(Math.random()*256));
			int TitleBarHalfHour = e.getStartTime().getMinuteOfDay()/48;	
		}
	}
	
	public void addEvents(List<Event> events)
	{
		this.events.addAll(events);
		this.draw();
	}

	public void removeEvents(List<Event> events)
	{
		this.events.removeAll(events);
		this.draw();
	}
	
	private class Hour extends JPanel
	{
		ArrayList<JPanel> firstHalfHour = new ArrayList<JPanel>();
		ArrayList<JPanel> secondHalfHour = new ArrayList<JPanel>();
		
		public void addEventBody(Color c, int fos, String content)
		{
			JPanel p = new JPanel();
			p.setBackground(c);
			boolean b = ((fos==0) ? this.firstHalfHour.add(p) : this.secondHalfHour.add(p));
		}
	}
	
	
	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		f.setLayout(new GridLayout(1,1));
		f.setSize(new Dimension(200, 1000));
		
		f.add(new DrawnDay(null));
		f.setVisible(true);
	}
}
