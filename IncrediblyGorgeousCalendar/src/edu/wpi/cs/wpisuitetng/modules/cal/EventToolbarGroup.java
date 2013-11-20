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
import edu.wpi.cs.wpisuitetng.modules.cal.eventui.AddEventDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;

public class EventToolbarGroup extends ToolbarGroupView {
	
	private final JPanel eventContentPanel = new JPanel();
	private final JButton addEventButton, removeEventButton;
	
	public EventToolbarGroup(final MainPanel mMainPanel) {
		super("Events");
		
		this.eventContentPanel.setLayout(new BoxLayout(eventContentPanel, BoxLayout.X_AXIS));
		
		addEventButton = new JButton("<html>Add<br/>Event</html>");
		addEventButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				AddEventDisplay ned = new AddEventDisplay();
				mMainPanel.addTopLevelTab(ned, "New Event", true);
				//TODO: use selected times. ned.display(DateTime.now());
			}
		});
		
		removeEventButton = new JButton("<html>Remove<br/>Event</html>");
		removeEventButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				mMainPanel.addTopLevelTab(new JPanel(), "test", true);
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
		
		this.add(eventContentPanel);
	}
	
	public JButton getRemoveEventButton(){
		return removeEventButton;
	}
}
