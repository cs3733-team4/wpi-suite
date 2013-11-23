package edu.wpi.cs.wpisuitetng.modules.cal.day;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
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
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public class DrawnDay extends JPanel{
	
	//private DateTime date;
	private List<Event> events = new ArrayList<Event>();
	
	//one JPanel for each hour
	private Hour[] hours = new Hour[24];
	private int[] collisions = new int[48];
	private int largestCollision = 0;
	private int widthDim = 0;
	
	private DateTimeFormatter dateFmt = DateTimeFormat.forPattern("h:mm a");;

	
	public DrawnDay(DateTime d, int width)
	{
		//this.date = d;
		this.setLayout(new GridLayout(24, 1));
		this.rescaleGrid(1);
		this.widthDim = width;
	}
	
	private void rescaleGrid(int width)
	{
		this.removeAll();
		for(int i = 0; i < 24; i++)
		{
			hours[i] = new Hour(width);
			hours[i].setBackground(Colors.TABLE_BACKGROUND);
			this.add(hours[i]);
		}
	}
	
	
	
	public void draw()
	{
		if (this.events != null){
			for(Event e : this.events)
			{
				int halfHour = e.getStart().getMinuteOfDay() / 30;
				int hour = halfHour/2;
				Color col = e.isProjectEvent()?new Color(125,157,227) : new Color(227,125,147);
				 
				int pos = this.hours[hour].addEventTitle(col, hour*2==halfHour, e.getName());
				int contentToDisplay = 0;
				boolean wordWrap = 84*this.largestCollision-1 > this.getWidth();
				
				String[] description = e.getDescription().split(" ");
				int descriptionCounter = 0;

				if (halfHour < 47)//need a check for short events like: && e.getEnd().getMinuteOfDay()/30-halfHour>1)
				{
					do{
						halfHour++;
						hour = halfHour/2;
						
						String message = "";
						
						//doesn't ever wrap anyways, time looked better formatted on one line and can not be squished anyways
						//if (!wordWrap) // stick time on two lines
						if(1==0)
						{
							if (contentToDisplay == 0)
							{
								message = "From: " + e.getStart().toString(dateFmt) + " ";
							}
							else if (contentToDisplay == 1)
							{
								message = "To: " + e.getEnd().toString(dateFmt) + " ";
							}
							else if (contentToDisplay > 1)
							{
								if (descriptionCounter < description.length){
									StringBuilder sb = new StringBuilder().append(description[descriptionCounter]);
									int totalWidth = description[descriptionCounter].length() * 12;
									
									descriptionCounter++;
									
									while(descriptionCounter < description.length && totalWidth + description[descriptionCounter].length() * 12 < this.widthDim / this.largestCollision)
									{
										totalWidth += description[descriptionCounter].length() * 12;
										sb.append(" ").append(description[descriptionCounter]);
										descriptionCounter++;
									}
									message = sb.toString();
								}
							}
						}
						else // stick time on one line
						{
							if (contentToDisplay == 0)
							{
								message = e.getStart().toString(dateFmt) + " - " + e.getEnd().toString(dateFmt) + " ";
							}
							if (contentToDisplay > 0)
							{
								if (descriptionCounter < description.length){
									StringBuilder sb = new StringBuilder().append(description[descriptionCounter]);
									int totalWidth = description[descriptionCounter].length() * 12;
									
									descriptionCounter++;
									
									while(descriptionCounter < description.length && totalWidth + description[descriptionCounter].length() * 12 < this.widthDim / this.largestCollision)
									{
										totalWidth += description[descriptionCounter].length() * 12;
										sb.append(" ").append(description[descriptionCounter]);
										descriptionCounter++;
									}
									message = sb.toString();
								}
							}
						}
						try{
							this.hours[hour].addEventBody(col, hour*2==halfHour, pos, message, false, contentToDisplay++<(wordWrap?2:1));
						}catch(Exception exc){
							exc.printStackTrace();
						}
					}
					while(halfHour + 1 < e.getEnd().getMinuteOfDay()/30);
					
					
					halfHour++;
					hour = halfHour/2;
					if (hour==24)
					{
						hour = 23;
						halfHour--; 
					}
					this.hours[hour].addEventBody(col, hour*2==halfHour, pos, "", true, false); 
				}
			}
		}
	}
	
	/**
	 * 
	 * @param events a list of events THIS MUST BE SORTED BY START TIME
	 * 
	 * UPDATE: events no longer need to be sorted because
	 *         the condition was being ignored anyways.
	 */
	public void addEvents(List<Event> events)
	{
		this.events.addAll(events);
		Collections.sort(this.events, new Comparator<Event>(){

			@Override
			public int compare(Event arg0, Event arg1) {
				return arg0.getStart().compareTo(arg1.getStart());
			}
			
		});
		for(Event e : events)
		{
			int startingHalfHour = e.getStart().getMinuteOfDay() / 30;
			for(int i = startingHalfHour; i <= e.getEnd().getMinuteOfDay()/30; i++)
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
			rescaleGrid(newLargestCollision);
			this.largestCollision = newLargestCollision;
		}
		
		this.draw();
	}
	
	
	

	public void removeEvents(List<Event> events)
	{
		this.events.removeAll(events);
		this.draw();
	}
	
	
	
	
	
	
	
	private class Hour extends JPanel
	{
		JPanel[][] subsections = new JPanel[2][];
		int topPosition = 0;
		int bottomPosition = 0;
		
		public Hour(int width)
		{
			this.setLayout(new GridLayout(2, width));
			this.subsections[0] = new JPanel[width];
			this.subsections[1] = new JPanel[width];
			for(int r = 0; r < 2; r++) 
			{
				for(int i = 0; i < width; i++)
				{
					this.subsections[r][i] = new JPanel();
					this.add(this.subsections[r][i]);
					this.subsections[r][i].setBackground(Colors.TABLE_BACKGROUND);
					this.subsections[r][i].setBorder(null);
				}
			}
			this.setBackground(Colors.TABLE_BACKGROUND);
			
		}
		
		/**
		 * 
		 * @param c this Event's color (this will be depreciated and will eentually pull the color from the event itself
		 * @param fos the first or second half hour. (true=first, false=second)
		 * @param pos the position in the selected half hour. should be the same for all event bodies
		 * @param content the content from the event that should be put here
		 * @param bottom whether this is the last event (and so should have a bottom border drawn)
		 */
		public void addEventBody(Color c, boolean fos, int pos, String content, boolean bottom, boolean time)
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
			
			JLabel contentHolder = new JLabel(content);
			contentHolder.setFont(new java.awt.Font("DejaVu Sans", time?Font.ITALIC:Font.PLAIN, 12));
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
		
		/**
		 * 
		 * @param c the color of the event. like in add body, this will be depreciated
		 * @param fos whether this is the first or second half hour segment
		 * @param content the title of the event
		 * @return the horizontal position that all body sections of this event should be in
		 */
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
			try{
			this.subsections[topBot][pos].setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, border));
			this.subsections[topBot][pos].setLayout(new GridLayout(1,1));
			this.subsections[topBot][pos].add(contentHolder);
			this.subsections[topBot][pos].setBackground(body);
			}catch(Exception e){}// TODO: fix
			return pos;
		}
	}
	
	
	public static void main(String[] args)
	{
		JFrame f = new JFrame();
		f.setLayout(new GridLayout(1,1));
		f.setSize(new Dimension(200, 800));
		
		List<Event> ev = new ArrayList<Event>();
		ev.add(new Event().addDescription("this is a descriptionas askdjashdlkjashdlkasjhdlkasjdh").addStartTime(new DateTime(2013, 11, 18, 3, 50)).addEndTime(new DateTime(2013, 11, 18, 4, 50)));
		ev.add(new Event().addDescription("this is a descriptionas askdjashdlkjashdlkasjhdlkasjdh").addStartTime(new DateTime(2013, 11, 18, 3, 50)).addEndTime(new DateTime(2013, 11, 18, 6, 20)));
		
		ev.add(new Event().addDescription("this is a descriptionas askdjashdlkjashdlkasjhdlkasjdh").addStartTime(new DateTime(2013, 11, 18, 13, 50)).addEndTime(new DateTime(2013, 11, 18, 17, 50)));
		ev.add(new Event().addDescription("this is a descriptionas askdjashdlkjashdlkasjhdlkasjdh").addStartTime(new DateTime(2013, 11, 18, 13, 20)).addEndTime(new DateTime(2013, 11, 18, 19, 50)));

		ev.add(new Event().addDescription("this is a descriptionas askdjashdlkjashdlkasjhdlkasjdh").addStartTime(new DateTime(2013, 11, 18, 14, 20)).addEndTime(new DateTime(2013, 11, 18, 16, 50)));
		
		
		Collections.sort(ev, new Comparator<Event>(){
			@Override
			public int compare(Event e, Event e2)
			{
				return e.getStart().compareTo(e2.getStart());
			}
		});
		
		DrawnDay d = new DrawnDay(DateTime.now(), 2134);
		//d.addEvents(ev);
		
		
		f.add(d);
		f.setVisible(true);
		
		d.addEvents(ev);
	}
	
}
