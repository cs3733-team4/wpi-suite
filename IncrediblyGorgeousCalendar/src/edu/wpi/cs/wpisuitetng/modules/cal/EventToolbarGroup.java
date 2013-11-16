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

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.cal.eventui.NewEventDisplay;

public class EventToolbarGroup extends ToolbarGroupView {
	
	private final JPanel eventContentPanel = new JPanel();
	JButton addEventButton;
	private NewEventDisplay newEvent;
	
	public EventToolbarGroup() {
		super("");
		
		this.eventContentPanel.setLayout(new BoxLayout(eventContentPanel, BoxLayout.X_AXIS));
		newEvent = new NewEventDisplay();
		
		JButton addEventButton = new JButton("<html>Add<br/>Event</html>");
		addEventButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//addCalendarTab(newEvent, "New Event", true); Something like this.
			}
		});
		
		JButton removeEventButton = new JButton("<html>Remove<br/>Event</html>");
		removeEventButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//addCalendarTab(newEvent, "New Event", true); Something like this.
			}
		});
		
		try {
		    Image img = ImageIO.read(getClass().getResource("add_event.png"));
		    addEventButton.setIcon(new ImageIcon(img));
		    
		    img = ImageIO.read(getClass().getResource("del_event.png"));
		    removeEventButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {}
		
		eventContentPanel.add(addEventButton);
		eventContentPanel.add(removeEventButton);
		this.setOpaque(false);
		
		this.add(eventContentPanel);
	}
	
	@Override
	public void mouseEntered() {}
	
	@Override
	public void mouseExited() {}
}
