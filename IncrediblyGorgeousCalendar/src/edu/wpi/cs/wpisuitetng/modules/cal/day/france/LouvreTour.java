package edu.wpi.cs.wpisuitetng.modules.cal.day.france;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;


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
	}
	
// //TODO: fix so that we can easily re-compute a part of the events stack	
//	public void removeEvent(Event event)
//	{
//		VanGoghPainting painting = guides.get(event);
//		remove(painting); // sell at auction in Christie's or Sotheby's
//		guides.remove(event);
//	}

	
}
