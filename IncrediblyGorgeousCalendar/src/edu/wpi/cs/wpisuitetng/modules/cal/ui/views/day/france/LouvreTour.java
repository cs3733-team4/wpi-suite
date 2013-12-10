/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui.views.day.france;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;

/**
 * All the VanGoghPaintings are visible in the LouvreTour you saw in the day
 */
public class LouvreTour extends JPanel
{
	HashMap<Event, VanGoghPainting> guides = new HashMap<>();
	private DateTime date;
	
	public LouvreTour()
	{
		setLayout(null);
		setPreferredSize(new Dimension(1, 1440));
		setBackground(Colors.TABLE_BACKGROUND);
		this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
				
			}
		});
	}
	
	public void setEvents(List<Event> events, DateTime displayedDay)
	{
		this.date = displayedDay;
		List<VanGoghPainting> gallery = CERN.createEventsReallyNicely(events, displayedDay);
		removeAll();
		guides.clear();
		for (VanGoghPainting vanGoghPainting : gallery)
		{
			guides.put(vanGoghPainting.event, vanGoghPainting);
			add(vanGoghPainting); // priceless
		}
		revalidate();
	}
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		
		int distanceBetweenBorders = (this.getHeight()-23)/24;
		g.setColor(Colors.BORDER);
		int currentPosition = distanceBetweenBorders+1;
		
		for(int i = 0; i < 23; i++)
		{
			g.drawLine(0, currentPosition, this.getWidth(), currentPosition);
			currentPosition++;
			//g.drawLine(0, currentPosition, this.getWidth(), currentPosition);
			currentPosition += distanceBetweenBorders;
		}
		
		currentPosition = distanceBetweenBorders/2+1;
		g.setColor(Colors.BORDER.brighter());
		for(int i = 0; i < 24; i++)
		{
			g.drawLine(0, currentPosition, this.getWidth(), currentPosition);
			currentPosition++;
			//g.drawLine(0, currentPosition, this.getWidth(), currentPosition);
			currentPosition += distanceBetweenBorders;
		}
		
		
	}

	public void select(Displayable item)
	{
		for (VanGoghPainting v : guides.values())
		{
			v.setSelected(false);
		}
		if (item instanceof Event)
		{
			VanGoghPainting mona = guides.get(item);
			if(mona != null)
			{
				mona.setSelected(true);
			}
		}
		
	}
	
	private DateTime getTimeAtCursor()
	{
		Point m = MouseInfo.getPointerInfo().getLocation();
		Point p = getLocationOnScreen();
		MutableDateTime d = date.toMutableDateTime();
		d.setHourOfDay((m.y - p.y)/60);
		d.setMinuteOfDay((m.y - p.y)%60);
		return d.toDateTime();
	}
	
// //TODO: fix so that we can easily re-compute a part of the events stack	
//	public void removeEvent(Event event)
//	{
//		VanGoghPainting painting = guides.get(event);
//		remove(painting); // sell at auction in Christie's or Sotheby's
//		guides.remove(event);
//	}

	
}
