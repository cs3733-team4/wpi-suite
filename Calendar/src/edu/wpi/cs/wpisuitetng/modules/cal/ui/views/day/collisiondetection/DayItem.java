/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui.views.day.collisiondetection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import edu.wpi.cs.wpisuitetng.modules.cal.models.CommitmentStatus;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;


/**
 * Beautiful images of what is in the days.
 */
public class DayItem extends JPanel
{
	public static final int FIXED_HEIGHT = 1440;
	public final long millisInDay = 86400000;
	
	private Rational width;
	private Rational x;
	private Displayable displayable;
	private JLabel lblEventTitle, lblTimeInfo;
	private JLabel lblStarryNightdutch;
	private HashMap<String, Integer> wordLengths = new HashMap<>();
	private List<String> description;
	private int spaceLength;
	private List<Rational> lineLengths;
	private boolean firstDraw = true;
	private int height;
	private OverlappedDisplayable eventPositionalInformation;
	private DateTime displayedDay;
	private Interval length;
	private boolean isBeingDragged;
	
	/**
	 * Creates a DayItem (drawable) based on an overlapping Displayable (Information) on the given day
	 * 
	 * @param eventPositionalInformation the positional information for the Displayable
	 * @param displayedDay the day to display this panel on
	 */
	public DayItem(OverlappedDisplayable eventPositionalInformation, DateTime displayedDay)
	{
		isBeingDragged = false;
		this.displayedDay=displayedDay;
		this.eventPositionalInformation = eventPositionalInformation;
		height = 25;
		displayable = eventPositionalInformation.getEvent();
		length = new Interval(displayable.getStart(), displayable.getEnd());
		
		Color bg = Colors.TABLE_GRAY_HEADER;
		
		if(displayable instanceof Event)
		{
			bg = displayable.getColor();
			setBorder(new CompoundBorder(new LineBorder(Colors.TABLE_BACKGROUND), new CompoundBorder(new LineBorder(bg.darker()), new EmptyBorder(6, 6, 6, 6))));
		}else if(displayable instanceof Commitment)
			setBorder(new CompoundBorder(new LineBorder(Colors.TABLE_BACKGROUND), new CompoundBorder(new MatteBorder(1, 0, 0, 0, ((Commitment) displayable).getStatusColor()), new CompoundBorder(new LineBorder(bg.darker()), new EmptyBorder(6, 6, 6, 6)))));
		
		setBackground(bg);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		lblEventTitle = new JLabel();
		add(lblEventTitle);
		lblEventTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEventTitle.putClientProperty("html.disable", true); //prevents html parsing
		lblEventTitle.setText(displayable.getName());		
		lblTimeInfo = new JLabel();
		putTimeOn();
		lblTimeInfo.setBorder(new EmptyBorder(0,0,3,0));
		lblTimeInfo.setMaximumSize(new Dimension(32767, 20));
		lblTimeInfo.setFont(new Font("DejaVu Sans", Font.ITALIC, 14));
		add(lblTimeInfo);
		lblStarryNightdutch = new JLabel();
		add(lblStarryNightdutch);
		lblStarryNightdutch.setVerticalAlignment(SwingConstants.TOP);
		lblStarryNightdutch.setBackground(bg);
		lblStarryNightdutch.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblStarryNightdutch.setMinimumSize(new Dimension(0,0));
		
		addMouseListener(new MouseListener()
		{
			@Override
			public void mouseReleased(MouseEvent e)
			{
				displayable.update();
				getParent().dispatchEvent(e);
			}
			
			@Override
			public void mousePressed(MouseEvent e)
			{
				if (e.getClickCount() > 1)
				{
					MainPanel.getInstance().editSelectedDisplayable(displayable);
				} else {
					MainPanel.getInstance().updateSelectedDisplayable(displayable);
				}
			}
			
			@Override
			public void mouseExited(MouseEvent e)
			{
				getParent().dispatchEvent(e);
			}
			
			@Override
			public void mouseEntered(MouseEvent e)
			{
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mouseClicked(MouseEvent e)
			{
				// TODO Auto-generated method stub	
			}
		});
		
		this.addMouseMotionListener(new MouseMotionListener()
		{
			@Override
			public void mouseMoved(MouseEvent e)
			{
				getParent().dispatchEvent(e);				
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0)
			{
				isBeingDragged = true;
				getParent().dispatchEvent(arg0);
			}
		});
		
		width = new Rational(((eventPositionalInformation.getCollisions() > 1) ? 2 : 1), 1 + eventPositionalInformation.getCollisions());
		x = eventPositionalInformation.getXpos();
		description = Arrays.asList(eventPositionalInformation.getEvent().getDescription().split(" "));
		if (x.toInt(10000) + width.toInt(10000) > 10000)
			width = width.multiply(new Rational(1,2));
		recalcBounds(200, FIXED_HEIGHT);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		this.doLayout();
		super.paintComponent(g);
	}
	
	@Override
	public void doLayout()
	{
		if(isBeingDragged)
		{
			width = new Rational(1,1);
			x = new Rational(0,1);
			this.setBackground(new Color(getBackground().getRed(), getBackground().getGreen(),getBackground().getBlue(), 150));
			int parentWidth = this.getParent().getWidth();
			recalcBounds(parentWidth, getParent().getHeight());
			super.doLayout();
			lblEventTitle.revalidate();
			lblTimeInfo.revalidate();
			
			return;
		}
		if(firstDraw)
		{
			height = (int) map(new Interval(displayable.getStartTimeOnDay(displayedDay), displayable.getEndTimeOnDay(displayedDay)).toDurationMillis(), this.getParent().getHeight());
			height = Math.max(height, 45);
			recalcBounds(getParent().getWidth(), getParent().getHeight());
			FontMetrics descriptionMetrics = getGraphics().getFontMetrics(lblStarryNightdutch.getFont());
			for(String word : description)
			{
				wordLengths.put(word, new Integer(descriptionMetrics.stringWidth(word)));
			}
			int totalHeight = 8 + getFontMetrics(lblEventTitle.getFont()).getHeight() + getFontMetrics(lblTimeInfo.getFont()).getHeight() + 6;
			lblEventTitle.validate();
			lblTimeInfo.validate();
			spaceLength = descriptionMetrics.stringWidth(" ");
			lineLengths = infest(descriptionMetrics.getHeight(), totalHeight);
			firstDraw = false;
		}
		int parentWidth = this.getParent().getWidth();
		recalcBounds(parentWidth, getParent().getHeight());
		super.doLayout();

	}
	
	/**
	 * 
	 * @param parentWidth the width of the DayView
	 * @param parentHeight the height of the DayView
	 */
	private void recalcBounds(int parentWidth, int parentHeight)
	{
		lblStarryNightdutch.setMaximumSize(new Dimension(width.toInt(parentWidth), height-20));
		int outWidth = width.toInt(parentWidth);
		this.setBounds(x.toInt(parentWidth), (int) map(displayable.getStartTimeOnDay(displayedDay).getMillisOfDay(), parentHeight), outWidth, height);
		if(!firstDraw)
		{
			wrapDescription(outWidth-16);
		}
	}
	
	/**
	 * converts a time to a pixel
	 * 
	 * @param num the time in millis
	 * @param high the high value of the day (pixels)
	 * @return the pixel value
	 */
	private long map(long num, long high)
	{
		return (long)(num/(double)millisInDay * high);
	}
	
	/**
	 * converts pixels to milliseconds
	 * 
	 * @param px the pixel value of the day
	 * @return the milliseconds that the pixel represents
	 */
	private int pxToMs(int px)
	{
		return px * 60000;
	}
	
	/**
	 * 
	 * @param myWidth
	 */
	private void wrapDescription(int myWidth)
	{
		//System.out.println("We are " + myWidth + " for Displayable " + Displayable);
		int line = 0;
		int lengthRemaining = lineLengths.size() > 0 ? lineLengths.get(0).toInt(myWidth) : 0;
		String formattedDescription = "<html>";
		for(String tword : description)
		{
			String word = tword.replaceAll("&", "&amp;");
			word = word.replaceAll("\"", "&quot;");
			word = word.replaceAll("<", "&lt;");
			word = word.replaceAll("<", "&gt;");
			if(wordLengths.get(tword).intValue() < lengthRemaining)
				formattedDescription += word;
			else
			{
				formattedDescription += "<br>"+word;
				if(++line == lineLengths.size())
				{
					formattedDescription += "...";
					break;
				}
				lengthRemaining = lineLengths.get(line).toInt(myWidth);
				//System.out.println("New line! rat wtih" + lineLengths.get(line).toString() + lengthRemaining);
			}
			lengthRemaining -= wordLengths.get(tword).intValue() + spaceLength;
			formattedDescription += " ";
			
		}
		formattedDescription += "</html>";
		//System.out.println(formattedDescription);
		lblStarryNightdutch.setText(formattedDescription);
	}
	
	
	public String formatTime(DateTime when)
	{
		String ret;
		boolean pm = when.getHourOfDay() >= 12;
		if (when.getHourOfDay() == 0)
			ret = "12";
		else if (when.getHourOfDay() > 12)
			ret = Integer.toString(when.getHourOfDay() - 12);
		else
			ret = Integer.toString(when.getHourOfDay());

		if (when.getMinuteOfHour() != 0)
		{
			ret += ":";
			if (when.getMinuteOfHour() < 10)
				ret += "0";
			ret += Integer.toString(when.getMinuteOfHour());
		}
		
		if (pm)
			ret += "p";

		return ret;
	}
	
	/**
	 * Rats might infest your house, be careful of the bubonic plague.
	 * @param lineheightp
	 * @param hdesc
	 * @return
	 */
	private List<Rational> infest(int lineheightp, int hdesc)
	{
		int lineheight = pxToMs(lineheightp);
		int headerHeight = height > hdesc ? hdesc : 0;
		int zero = eventPositionalInformation.getEvent().getStartTimeOnDay(displayedDay).getMillisOfDay() + pxToMs(headerHeight);
		
		double erowsInter = (eventPositionalInformation.getEvent().getEndTimeOnDay(displayedDay).getMillisOfDay() - zero) / (double) lineheight;
		
		int emax = (int)Math.floor(erowsInter);
		int rows = (int)Math.ceil(erowsInter);
		rows=rows<0? 0: rows;
		ArrayList<Rational> ratpack = new ArrayList<>(rows);
		for (int i = 0; i <= rows; i++) // <= for extra buffer row
		{
			ratpack.add(new Rational(1, 1));
		}
		for (OverlappedDisplayable who : eventPositionalInformation.getOverlappedEvents())
		{
			//System.out.println("" + who + " was in" + traveller);
			if (who.getXpos().toInt(10000) < eventPositionalInformation.getXpos().toInt(10000))
				continue;
			//System.out.println("and is good");
			int from = (int)Math.floor((who.getEvent().getStartTimeOnDay(displayedDay).getMillisOfDay() - zero) / (double) lineheight);
			int to = (int)Math.ceil((who.getEvent().getEndTimeOnDay(displayedDay).getMillisOfDay() - zero) / (double) lineheight);
			from = Math.max(0, from);
			to = Math.min(emax, to);
			//System.out.println(rows + " of from " + from + " to " + to);
			for (int i = from; i <= to; i++)
			{
				// the vermin
				Rational redRat = ratpack.get(i);
				Rational blackRat = who.getXpos().subtract(eventPositionalInformation.getXpos()).divide(width);
				//System.out.println(" Comparing rats["+i+"] " + redRat + " vs " +blackRat + " (generated from " + traveller.getXpos() + ", " + who.getXpos() + ", " + Width + ")");
				ratpack.set(i, redRat.toInt(10000) < blackRat.toInt(10000) ? redRat : blackRat);
			}
		}
		// remote buffer overflow
		// ratpack.remove(rows);
		ratpack.remove(rows - 1);
		
		
		return ratpack;
	}
	

	/**
	 * Set borders depending on selected status
	 * @param b selected status of the item
	 */
	public void setSelected(boolean b)
	{
		//lblEventTitle.setForeground(b?Color.WHITE:Color.BLACK);
		//TODO: Fix the paint order or revert to old selection method
		if(displayable instanceof Event)
			setBorder(b ? new CompoundBorder(new LineBorder(displayable.getColor().darker()), new CompoundBorder(new LineBorder(displayable.getColor().darker()), new EmptyBorder(6, 6, 6, 6)))
						: new CompoundBorder(new LineBorder(Colors.TABLE_BACKGROUND), new CompoundBorder(new LineBorder(displayable.getColor().darker()), new EmptyBorder(6, 6, 6, 6))));
		else if(displayable instanceof Commitment)
			setBorder(b ? new CompoundBorder(new LineBorder(displayable.getColor().darker()), new CompoundBorder(new MatteBorder(1, 0, 0, 0, ((Commitment) displayable).getStatusColor()), new CompoundBorder(new LineBorder(Colors.TABLE_GRAY_HEADER.darker()), new EmptyBorder(6, 6, 6, 6))))
						: new CompoundBorder(new LineBorder(Colors.TABLE_BACKGROUND), new CompoundBorder(new MatteBorder(1, 0, 0, 0, ((Commitment) displayable).getStatusColor()), new CompoundBorder(new LineBorder(Colors.TABLE_GRAY_HEADER.darker()), new EmptyBorder(6, 6, 6, 6)))));
	}

	public Displayable getEvent() {
		return displayable;
	}
	
	public void updateTime(DateTime t)
	{
		if(!this.displayable.getStart().equals(t))
		{
			this.displayable.setStart(t);
			if(this.displayable instanceof Event)
				((Event) this.displayable).setEnd(t.plus(this.length.toDuration()));
			putTimeOn();
		}
	}
	
	private void putTimeOn()
	{
		if(displayable instanceof Event)
		{
			if(((Event) displayable).isMultiDayEvent())
			{
				Event eDisplayable = (Event) displayable;
				if (eDisplayable.getStart().compareTo(eDisplayable.getStartTimeOnDay(displayedDay))==0)//if their the same time, its the first day
					lblTimeInfo.setText(formatTime(eDisplayable.getStart()) + " \u2192");
				else if (eDisplayable.getEnd().compareTo(eDisplayable.getEndTimeOnDay(displayedDay))==0)
					lblTimeInfo.setText("\u2190 " + formatTime(eDisplayable.getEnd()));
				else
					lblTimeInfo.setText("\u2190 \u2192");
			}else
				lblTimeInfo.setText(formatTime(displayable.getStart()) + " - " + formatTime(displayable.getEnd()));
		}else if(displayable instanceof Commitment)
		{
			URL imgurl = getClass().getResource("/edu/wpi/cs/wpisuitetng/modules/cal/img/commitment_unstarted.png");
			String imgtag = "<img src='" + imgurl +"'/>";
        	// Get the appropriate image based on the commitment's status and put it on the label.
        	if (((Commitment) displayable).getStatus() != null)
        	{
        		// Set the appropriate url source based on the status.
        		if (((Commitment) displayable).getStatus() == CommitmentStatus.InProgress)
        			imgurl = getClass().getResource("/edu/wpi/cs/wpisuitetng/modules/cal/img/commitment_in_progress.png");
        		else if (((Commitment) displayable).getStatus() == CommitmentStatus.Complete)
        			imgurl = getClass().getResource("/edu/wpi/cs/wpisuitetng/modules/cal/img/commitment_complete.png");
        		imgtag = "<img src='" + imgurl +"'/>";
        	}
			lblTimeInfo.setText("<html></i><b><font face = \"DejaVu Sans\""
					 + "color=\"rgb(" + Colors.COMMITMENT_NOT_STARTED.getRed() + ","
										+ Colors.COMMITMENT_NOT_STARTED.getGreen() + "," 
										+ Colors.COMMITMENT_NOT_STARTED.getBlue() 
										+ "\">" + imgtag + "</font></b>" 
										+ formatTime(displayable.getStart()) + "</html>");
		}
	}
}
