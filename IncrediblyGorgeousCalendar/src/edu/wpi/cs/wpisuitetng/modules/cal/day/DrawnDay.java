package edu.wpi.cs.wpisuitetng.modules.cal.day;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public class DrawnDay extends JPanel{
	
	private DateTime date;
	private List<Event> events = new ArrayList<Event>();
	
	//one JPanel for each hour
	private Hour[] hours = new Hour[24];
	private int[] collisions = new int[48];
	private int largestCollision = 0;
	
	public DrawnDay(DateTime d)
	{
		this.date = d;
		this.setLayout(new GridLayout(24, 1));
		this.rescaleGrid();
	}
	
	private void rescaleGrid()
	{
		this.removeAll();
		for(int i = 0; i < 24; i++)
		{
			int[] collisions = {this.collisions[2*i], this.collisions[2*i+1]};
			System.out.println(collisions[0]+", "+collisions[1]);
			hours[i] = new Hour(collisions);
			hours[i].setBackground(Colors.TABLE_BACKGROUND);
			this.add(hours[i]);
		}
	}
	
	public void draw()
	{
		 for(Event e : this.events)
		 {
			 int halfHour = e.getStartTime().getMinuteOfDay() / 30;
			 int hour = halfHour/2;
			 Color rand = new Color((int)(Math.random()*256),(int)(Math.random()*256),(int)(Math.random()*256));
			 
			 
			 int pos = this.hours[hour].addEventTitle(rand, hour*2==halfHour, "TEST "+halfHour+" : "+hour);
			 
			 do{
				 halfHour++;
				 hour = halfHour/2;
				 this.hours[hour].addEventBody(rand, hour*2==halfHour, pos, "TEST "+halfHour+" : "+hour, false);
			 }
			 while(halfHour < e.getEndTime().getMinuteOfDay()/30);
			 halfHour++;
			 hour = halfHour/2;
			 try{this.hours[hour].addEventBody(rand, hour*2==halfHour, pos, "TEST "+halfHour+" : "+hour, true);}catch(Exception sa){}
			 
		 }
	}
	
	/**
	 * 
	 * @param events a list of events THIS MUST BE SORTED BY START TIME
	 */
	public void addEvents(List<Event> events)
	{
		this.events.addAll(events);
		for(Event e : events)
		{
			int startingHalfHour = e.getStartTime().getMinuteOfDay() / 30;
			for(int i = startingHalfHour; i <= e.getEndTime().getMinuteOfDay()/30; i++)
			{
				this.collisions[i]++;
			}
		}
		int newLargestCollision = this.collisions[0];
		for(int i = 1; i < this.collisions.length; i++)
		{
			if (this.collisions[i] > newLargestCollision)
			{
				newLargestCollision = this.collisions[i];
			}
		}
		if (this.largestCollision < newLargestCollision)
		{
			rescaleGrid();
			this.largestCollision = newLargestCollision;
		}
		
		this.draw();
	}
	
	
	

	
	
	
	
	
	
	
	private class Hour extends JPanel
	{
		JPanel[][] subsections = new JPanel[2][];
		int topPosition = 0;
		int bottomPosition = 0;
		
		public Hour(int[] widths)
		{
			this.setLayout(new GridLayout(2, 1));
			JPanel top = new JPanel();
			JPanel bot = new JPanel();
			this.add(top);
			this.add(bot);
			top.setLayout(new GridLayout(1, widths[0]));
			bot.setLayout(new GridLayout(1, widths[1]));
			JPanel[] all = {top, bot};
			
			this.subsections[0] = new JPanel[widths[0]];
			this.subsections[1] = new JPanel[widths[1]];
			
			for(int r = 0; r < 2; r++) 
			{
				for(int i = 0; i < widths[r]; i++)
				{
					this.subsections[r][i] = new JPanel();
					all[r].add(this.subsections[r][i]);
					this.subsections[r][i].setBorder(null);
				}
			}
			
		}
		
		/**
		 * adds a non-bottom colored portion of an event
		 * 
		 * @param c the Color of the event
		 * @param fos the half hour (false = first half hour, true = second half hour)
		 * @param content the string context that is in this position
		 */
		public void addEventBody(Color c, boolean fos, int pos, String content, boolean bottom)
		{
			Color body = c.brighter();
			Color border = c.darker().darker();
			
			if (fos)
			{
				this.topPosition++;
			}
			else
			{
				this.bottomPosition++;
			}
			
			JLabel contentHolder = new JLabel(pos+"");
			contentHolder.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 12));
			contentHolder.setBackground(body);
			if (body.getBlue()+body.getRed()+body.getGreen() > 400)
			{
				contentHolder.setForeground(Color.BLACK);
			}
			else
			{
				contentHolder.setForeground(Color.WHITE);
			}
			int topBot = fos?0:1;
			this.subsections[topBot][pos].setBorder(BorderFactory.createMatteBorder(0, 1, bottom?1:0, 1, border));
			this.subsections[topBot][pos].setLayout(new GridLayout(1,1));
			this.subsections[topBot][pos].add(contentHolder);
			this.subsections[topBot][pos].setBackground(body);
		}
		
		
		public int addEventTitle(Color c, boolean fos, String content)
		{
			Color body = c.darker();
			Color border = c.darker().darker();
			
			JLabel contentHolder = new JLabel(content);
			contentHolder.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 12));
			contentHolder.setBackground(body);
			
			int colorValue = body.getBlue()+body.getRed()+body.getGreen();
			if (colorValue > 384)
			{
				contentHolder.setForeground(Color.BLACK);
			}
			else
			{
				contentHolder.setForeground(Color.WHITE);
			}
			int topBot = fos?0:1;
			int pos = (!fos?(this.bottomPosition++):(this.topPosition++));
			contentHolder.setText(pos+"");
			this.subsections[topBot][pos].setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, border));
			this.subsections[topBot][pos].setLayout(new GridLayout(1,1));
			this.subsections[topBot][pos].add(contentHolder);
			this.subsections[topBot][pos].setBackground(body);
			return pos;
		}
	}
	
	
	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		f.setLayout(new GridLayout(1,1));
		f.setSize(new Dimension(200, 1000));
		
		List<Event> ev = new ArrayList<Event>();
		ev.add(new Event().addStartTime(new DateTime(2013, 11, 18, 3, 50)).addEndTime(new DateTime(2013, 11, 18, 4, 50)));
		ev.add(new Event().addStartTime(DateTime.now()).addEndTime(new DateTime(2013, 11, 18, 6, 20)));
		
		ev.add(new Event().addStartTime(new DateTime(2013, 11, 18, 13, 50)).addEndTime(new DateTime(2013, 11, 18, 17, 50)));
		ev.add(new Event().addStartTime(new DateTime(2013, 11, 18, 13, 20)).addEndTime(new DateTime(2013, 11, 18, 19, 50)));
		ev.add(new Event().addStartTime(new DateTime(2013, 11, 18, 14, 20)).addEndTime(new DateTime(2013, 11, 18, 16, 50)));
		
		
		Collections.sort(ev, new Comparator<Event>(){
			@Override
			public int compare(Event e, Event e2)
			{
				return e.getStartTime().compareTo(e2.getStartTime());
			}
		});
		
		DrawnDay d = new DrawnDay(DateTime.now());
		d.addEvents(ev);
		
		
		f.add(d);
		f.setVisible(true);
	}
}
