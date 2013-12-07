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
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

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
	JLabel time = new JLabel(), desc = new JLabel(), lastArrow = new JLabel();
	DateTime currentTime;
	
	private Displayable mDisplayable;
	
	
	public MonthItem(Displayable ndisp, MonthDay parent)
	{
		this(ndisp, DateTime.now(), parent);
	}
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
	/**
	 * MonthItem Constructor
	 * @param when
	 * @param descr
	 */
	public MonthItem(Displayable ndisp, DateTime day, final MonthDay parent)
	{
		currentTime = day;
		
        setBackground(Colors.TABLE_BACKGROUND);
        setMaximumSize(new java.awt.Dimension(32767, 24));
        setMinimumSize(new java.awt.Dimension(0, 0));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));

        this.mDisplayable = ndisp;
        
        if (ndisp instanceof Commitment)
        {

            time.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 14));
        	time.setForeground(Colors.COMMITMENT_NOTIFICATION);
        	time.setText("\u2762");
        }
        else if (ndisp instanceof Event)
        {

        	lastArrow.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 12));
            time.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 12));
        	if (isStartBeforeCurrent(day, ((Event)ndisp).getStart()) && isEndAfterCurrent(day, ((Event)ndisp).getEnd()))
        	{
        		time.setText("\u2190");//the event goes before and after
        		lastArrow.setText(" \u2192");
        	}
        	else if (isStartBeforeCurrent(day, ((Event)ndisp).getStart()))
        	{
        		lastArrow.setText(null);
        		time.setText("\u2190");
        	}
        	else if(isEndAfterCurrent(day, ((Event)ndisp).getEnd()))
        	{
        		time.setText(simpleTime(mDisplayable.getDate()));
        		lastArrow.setText(" \u2192");
        	}
        	else
        	{
        		lastArrow.setText(null);
        		time.setText(simpleTime(mDisplayable.getDate()));
        	}
        }
        lastArrow.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 3));
        time.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 3));
        add(time);


        desc.putClientProperty("html.disable", true);
        desc.setText(mDisplayable.getName());
        desc.setFont(new java.awt.Font("DejaVu Sans", Font.PLAIN, 12));
        desc.setMinimumSize(new java.awt.Dimension(10, 15));
        add(desc);
        
        if (lastArrow.getText()!=null)
        	add(lastArrow);
		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				MainPanel.getInstance().setSelectedDay(currentTime);
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
		
		addMouseMotionListener(new MouseMotionListener(){

			@Override
			public void mouseDragged(MouseEvent e) {
				parent.dispatchEvent(e);
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				parent.dispatchEvent(e);
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


	public static Component generateFrom(Displayable elt, Displayable selected, DateTime day, MonthDay parent)
	{
		MonthItem mi = new MonthItem(elt, day, parent);
		mi.setSelected(selected == elt);
		return mi;
	}
	
	/**
	 * Get current displayable
	 * @return the selected displayable
	 */
	public Displayable getDisplayable(){
		return this.mDisplayable;
	}
	
	public void setSelected(boolean select){
		if(select){
			this.setBackground(Colors.SELECTED_BACKGROUND);
		}
		else this.setBackground(Colors.TABLE_BACKGROUND);
	}
}

