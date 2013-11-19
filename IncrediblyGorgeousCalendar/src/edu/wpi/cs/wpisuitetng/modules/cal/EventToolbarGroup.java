package edu.wpi.cs.wpisuitetng.modules.cal;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.cal.eventui.NewEventDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;

public class EventToolbarGroup extends ToolbarGroupView {
	
	private final JPanel eventContentPanel = new JPanel();
	JButton addEventButton;
	private NewEventDisplay newEvent;
	
	public EventToolbarGroup() {
		super("");
		
		this.eventContentPanel.setLayout(new BoxLayout(eventContentPanel, BoxLayout.X_AXIS));

		JButton addEventButton = new JButton("<html>Add an<br/>Event</html>");
		newEvent = new NewEventDisplay();
		addEventButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//addCalendarTab(newEvent, "New Event", true); Something like this.
			}
		});
		
		try {
		    Image img = ImageIO.read(getClass().getResource("new_itt.png"));
		    addEventButton.setIcon(new ImageIcon(img));		    
		} catch (IOException ex) {}

		JButton addEvent = new JButton();
		addEvent.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Event testEvent = new Event();
				testEvent.setName("Test Event");
				testEvent.setDescription("A Description");
				DateTime start = DateTime.now();
				DateTime end = DateTime.now().plusHours(5);
				testEvent.setStart(start);
				testEvent.setEnd(end);
				String[] categories = {"Category 1", "Category 2"};
				testEvent.setCategories(categories);
				testEvent.setParticipants("Participant1, Patricipant2");
				testEvent.setProjectEvent(true);
				EventModel model = new EventModel();
				boolean success = model.putEvent(testEvent);
				Event[] events = model.getEvents(start.minusHours(10), end.plusHours(10));
				if(events.length > 0)
					System.out.println("GOT EVENT!!!!:" +  events[0].toJSON().equals(testEvent.toJSON()));
				else
					System.out.println("No events returned.");
			}
		});
		addEvent.setLabel("Do Something");
		eventContentPanel.add(addEventButton);
		eventContentPanel.add(addEvent);
		this.setOpaque(false);
		
		this.add(eventContentPanel);
	}
	
	@Override
	public void mouseEntered() {}
	
	@Override
	public void mouseExited() {}
}
