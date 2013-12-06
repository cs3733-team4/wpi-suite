/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;

/**
 * UI for displaying individual events/commitments in MonthDays. These are collapsed
 * into CollapsedMonthItem's when there is not enough space
 */
public class MonthItem extends JPanel
{	
	private JLabel time = new JLabel(), name = new JLabel(), arrow = new JLabel("");
	private JPanel categoryColor = new JPanel();
	private DateTime currentTime;
	private Displayable mDisplayable;
	
	/**
	 * Month Item constructor. When called without time, set time to current time
	 * @param ndisp displayable to show on month item
	 */
	public MonthItem(Displayable ndisp)
	{
		this(ndisp, DateTime.now());
	}
	
	/**
	 * MonthItem Constructor
	 * @param ndisp displayable to display
	 * @param day time of month item
	 */
	public MonthItem(Displayable ndisp, DateTime day)
	{
		currentTime = day;
        this.mDisplayable = ndisp;
        
        // Set up Month Item Layout
        setBackground(Colors.TABLE_BACKGROUND);
        setMaximumSize(new java.awt.Dimension(32767, 24));
        setMinimumSize(new java.awt.Dimension(0, 0));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));

        // Time label
        time.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 12));
        time.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 3));
        
        // Name label
        name.putClientProperty("html.disable", true);
        name.setText(mDisplayable.getName() + " ");
        name.setFont(new java.awt.Font("DejaVu Sans", Font.PLAIN, 12));
        name.setMinimumSize(new java.awt.Dimension(10, 15));
        
        // Arrow label (for multi-day events)
        arrow.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 12));
        arrow.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 3));
    	arrow.setAlignmentY(CENTER_ALIGNMENT);
    	arrow.setAlignmentX(CENTER_ALIGNMENT);
        
        // Box for category color and arrow label
        categoryColor.setPreferredSize(new Dimension(16, 15));
    	categoryColor.setMaximumSize(new Dimension(16, 15));
    	categoryColor.setMinimumSize(new Dimension(16, 15));
    	categoryColor.setLayout(new GridLayout(1,1));
        
    	/** Display displayable based on whether it is an event or a commitment*/
    	
        // If displayable is commitment, show commitment sign and name
        if (ndisp instanceof Commitment)
        {
        	arrow.setForeground(Colors.COMMITMENT_NOTIFICATION);
        	arrow.setText("\uFF01");
        	categoryColor.setBackground((Colors.TABLE_BACKGROUND));
	    	categoryColor.setBorder(new EmptyBorder(0, 0, 0, 0));
        }
        else if (ndisp instanceof Event) // Else, show date, name, category, and whether event is multiple day
        {
        	categoryColor.setBackground(((Event) ndisp).getColor());
        	categoryColor.setBorder(new EmptyBorder(0, 0, 0, 0));
        	
        	if (isStartBeforeCurrent(day, ((Event)ndisp).getStart()) && isEndAfterCurrent(day, ((Event)ndisp).getEnd()))
        	{
        		arrow.setText("\u2194");//the event goes before and after
        	}
        	else if (isStartBeforeCurrent(day, ((Event)ndisp).getStart()))
        	{
        		arrow.setText("\u2190");
        	}
        	else if(isEndAfterCurrent(day, ((Event)ndisp).getEnd()))
        	{
        		time.setText(simpleTime(mDisplayable.getDate()));
        		arrow.setText("\u2192");
        	}
        	else
        	{
        		time.setText(simpleTime(mDisplayable.getDate()));
        	}
        }

        // Add elements to UI
        categoryColor.add(arrow);
        add(categoryColor);
        add(time);
        add(name);

		// Set up click listener
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				MainPanel.getInstance().display(currentTime);
				if (e.getClickCount() > 1){
					MainPanel.getInstance().editSelectedDisplayable(mDisplayable);
				} else {
					MainPanel.getInstance().updateSelectedDisplayable(mDisplayable);
				}
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {				
			}
		});
	}

	/**
	 * Generate small version of the time
	 * @param when
	 * @return
	 */
	public String simpleTime(DateTime when)
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
	 * Generate month item
	 * @param elt displayable to display
	 * @param selected selected displayable by Main Panel
	 * @param day current date
	 * @return month item
	 */
	public static Component generateFrom(Displayable elt, Displayable selected, DateTime day)
	{
		MonthItem mi = new MonthItem(elt, day);
		mi.setSelected(elt, selected);
		return mi;
	}
	
	/**
	 * Get current displayable
	 * @return the selected displayable
	 */
	public Displayable getDisplayable(){
		return this.mDisplayable;
	}
	
	/**
	 * Sets selected month item in month view
	 * @param elt displayable to check
	 * @param item selected displayable
	 */
	public void setSelected(Displayable elt, Displayable item){
		if(elt == item)
			this.setBackground(Colors.SELECTED_BACKGROUND);
		else
			this.setBackground(Colors.TABLE_BACKGROUND);
	}
	
	
	/**
	 * Check if start date of event is before the specified day
	 * @param currentDay day to compare event time with
	 * @param eventStart event time
	 * @return boolean determining whether the event start date is before current day
	 */
	private boolean isStartBeforeCurrent(DateTime currentDay, DateTime eventStart)
	{
		if (eventStart.getYear()<currentDay.getYear())//if the year is less than its always true
			return true;
		else if (eventStart.getYear()>currentDay.getYear())
			return false;
		else if (eventStart.getDayOfYear()<currentDay.getDayOfYear())//year is the same, so only day matters
			return true;
		return false;
	}
	
	/**
	 * Check if end date of event is after the specified day
	 * @param currentDay day to compare event time with
	 * @param eventEnd event time
	 * @return boolean determining whether the event end date is after current day
	 */
	private boolean isEndAfterCurrent(DateTime currentDay, DateTime eventEnd)
	{
		if (eventEnd.getYear()>currentDay.getYear())//if the year is less than its always true
			return true;
		else if (eventEnd.getYear()<currentDay.getYear())
			return false;
		else if (eventEnd.getDayOfYear()>currentDay.getDayOfYear())//year is the same, so only day matters
			return true;
		return false;
	}
}

