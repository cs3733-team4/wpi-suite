/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.day.france;

import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

import javax.swing.JLabel;
import javax.swing.BoxLayout;

import java.awt.Font;
import java.awt.FontMetrics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.SwingConstants;

/**
 * Beautiful images of what is in the days.
 */
public class VanGoghPainting extends JPanel
{
	private final long millisInDay = 86400000;
	private int lastWidth = 0;
	private Rational Width;
	private Rational X;
	Event event;
	private JLabel lblEventTitle, lblTimeInfo;
	private JLabel lblStarryNightdutch;
	private HashMap<String, Integer> wordLengths = new HashMap<>();
	private List<String> description;
	private int spaceLength;
	private List<Rational> lineLengths;
	private boolean firstDraw = true;
	private int height;
	private static final int FIXED_HEIGHT = 1440;
	private TimeTraveller traveller;
	public VanGoghPainting(TimeTraveller traveller)
	{
		this.traveller = traveller;
		height = 25;
		event = traveller.getEvent();
		Color bg = event.getColor();
		setBorder(new CompoundBorder(new LineBorder(Colors.TABLE_BACKGROUND), new CompoundBorder(new LineBorder(bg.darker()), new EmptyBorder(6, 6, 6, 6))));
		setBackground(bg);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		lblEventTitle = new JLabel(event.getName());
		add(lblEventTitle);
		lblEventTitle.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTimeInfo = new JLabel(formatTime(event.getStart()) + " - " + formatTime(event.getEnd()));
		lblTimeInfo.setBorder(new EmptyBorder(0,0,3,0));
		lblTimeInfo.setMaximumSize(new Dimension(32767, 20));
		lblTimeInfo.setFont(new Font("Tahoma", Font.ITALIC, 14));
		add(lblTimeInfo);
		lblStarryNightdutch = new JLabel();
		add(lblStarryNightdutch);
		lblStarryNightdutch.setVerticalAlignment(SwingConstants.TOP);
		lblStarryNightdutch.setBackground(bg);
		lblStarryNightdutch.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblStarryNightdutch.setMinimumSize(new Dimension(0,0));
		Width = new Rational(((traveller.getCollisions() > 1) ? 2 : 1), 1 + traveller.getCollisions());
		X = traveller.getXpos();
		description = Arrays.asList(traveller.getEvent().getDescription().split(" "));
		if (X.toInt(10000) + Width.toInt(10000) > 10000)
			Width = Width.multiply(new Rational(1,2));
		recalcBounds(200, FIXED_HEIGHT);
	}
	
	@Override
	public void paint(Graphics g)
	{
		this.doLayout();
		super.paint(g);
	}
	
	@Override
	public void doLayout()
	{
		if(firstDraw)
		{
			height = (int) map(new Interval(event.getStart(), event.getEnd()).toDurationMillis(), this.getParent().getHeight());
			recalcBounds(getParent().getWidth(), getParent().getHeight());
			FontMetrics descriptionMetrics = getGraphics().getFontMetrics(lblStarryNightdutch.getFont());
			for(String word : description)
			{
				wordLengths.put(word, new Integer(descriptionMetrics.stringWidth(word)));
			}
			int totalHeight = 6 + 2 + getFontMetrics(lblEventTitle.getFont()).getHeight() + getFontMetrics(lblTimeInfo.getFont()).getHeight() + 6;
			lblEventTitle.validate();
			lblTimeInfo.validate();
			System.out.println("falculated to be " + lblEventTitle.getHeight() + lblTimeInfo.getHeight());
			spaceLength = descriptionMetrics.stringWidth(" ");
			lineLengths = infest(descriptionMetrics.getHeight(), totalHeight);
			firstDraw = false;
		}
		int parentWidth = this.getParent().getWidth();
		if (recalcBounds(parentWidth, getParent().getHeight()))
			super.doLayout();
	}
	
	private boolean recalcBounds(int parentWidth, int parentHeight)
	{
		if (parentWidth != lastWidth)
		{
			lastWidth = parentWidth;
		}
		else
		{
			return false;
		}
		lblStarryNightdutch.setMaximumSize(new Dimension(Width.toInt(parentWidth), height-20));
		int outWidth = Width.toInt(parentWidth);
		this.setBounds(X.toInt(parentWidth), (int) map(event.getStart().getMillisOfDay(), parentHeight), outWidth, height);
		if(!firstDraw)
			wrapDescription(outWidth-16);
		return true;
	}
		
	private long map(long num, long high)
	{
		return (long)(num/(double)millisInDay * high);
	}
	
	private int pxToMs(int px)
	{
		return px * 60000;
	}
	
	private void wrapDescription(int myWidth)
	{
		System.out.println("We are " + myWidth + " for event " + event);
		int line = 0;
		int lengthRemaining = lineLengths.size() > 0 ? lineLengths.get(0).toInt(myWidth) : 0;
		String formattedDescription = "<html>";
		for(String word : description)
		{
			if(wordLengths.get(word).intValue() < lengthRemaining)
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
				System.out.println("New line! rat wtih" + lineLengths.get(line).toString() + lengthRemaining);
			}
			lengthRemaining -= wordLengths.get(word).intValue() + spaceLength;
			formattedDescription += " ";
			
		}
		formattedDescription += "</html>";
		System.out.println(formattedDescription);
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
		System.out.println("heretics! " + lineheight + ", " + hdesc);
		int headerHeight = height > hdesc ? hdesc : 0;
		int zero = traveller.getEvent().getStart().getMillisOfDay() + pxToMs(hdesc);
		double erowsInter = (traveller.getEvent().getEnd().getMillisOfDay() - zero) / (double) lineheight;
		int emax = (int)Math.floor(erowsInter);
		int rows = (int)Math.ceil(erowsInter);
		System.out.println("we are " + traveller + " and have " + emax + ", " + rows + ", " + zero + "  x " + headerHeight + ", " + lineheightp);
		ArrayList<Rational> ratpack = new ArrayList<>(rows);
		for (int i = 0; i <= rows; i++) // <= for extra buffer row
		{
			ratpack.add(new Rational(1, 1));
		}
		for (TimeTraveller who : traveller.getOverlappedEvents())
		{
			System.out.println("" + who + " was in" + traveller);
			if (who.getXpos().toInt(10000) < traveller.getXpos().toInt(10000))
				continue;
			System.out.println("and is good");
			//TODO: does not support multi day events
			int from = (int)Math.floor((who.getEvent().getStart().getMillisOfDay() - zero) / (double) lineheight);
			int to = (int)Math.ceil((who.getEvent().getEnd().getMillisOfDay() - zero) / (double) lineheight);
			from = Math.max(0, from);
			to = Math.min(emax, to);
			System.out.println(rows + " of from " + from + " to " + to);
			for (int i = from; i <= to; i++)
			{
				// the vermin
				Rational redRat = ratpack.get(i);
				Rational blackRat = who.getXpos().subtract(traveller.getXpos()).divide(Width);
				System.out.println(" Comparing rats["+i+"] " + redRat + " vs " +blackRat + " (generated from " + traveller.getXpos() + ", " + who.getXpos() + ", " + Width + ")");
				ratpack.set(i, redRat.toInt(10000) < blackRat.toInt(10000) ? redRat : blackRat);
			}
		}
		// remote buffer overflow
//		ratpack.remove(rows);
		ratpack.remove(rows - 1);
		System.out.println("ratpack is");
		for (Rational rat : ratpack)
		{
			System.out.println(" " + rat + " => " + rat.toInt(100) + "%");
		}
		System.out.println("ratpack end");
		
		return ratpack;
	}
	
}
