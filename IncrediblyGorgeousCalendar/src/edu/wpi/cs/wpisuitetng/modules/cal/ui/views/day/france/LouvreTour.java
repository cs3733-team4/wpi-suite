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
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;

/**
 * All the VanGoghPaintings are visible in the LouvreTour you saw in the day
 */
public class LouvreTour extends JPanel
{
	HashMap<Event, VanGoghPainting> guides = new HashMap<>();
	public LouvreTour()
	{
		setLayout(null);
		setPreferredSize(new Dimension(1, 1440));
		setBackground(Colors.TABLE_BACKGROUND);
	}
	
	public void setEvents(List<Event> events)
	{
		List<VanGoghPainting> gallery = CERN.createEventsReallyNicely(events);
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

	public void select(Displayable item) {
		if(item instanceof Event){
			VanGoghPainting mona = guides.get(item);
			if(mona != null){
				mona.setSelected(true);
			}
		}
		
	}
	
// //TODO: fix so that we can easily re-compute a part of the events stack	
//	public void removeEvent(Event event)
//	{
//		VanGoghPainting painting = guides.get(event);
//		remove(painting); // sell at auction in Christie's or Sotheby's
//		guides.remove(event);
//	}

	
}
