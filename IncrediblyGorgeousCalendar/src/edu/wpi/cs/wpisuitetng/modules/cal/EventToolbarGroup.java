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

public class EventToolbarGroup extends ToolbarGroupView {
	
	private final JPanel eventContentPanel = new JPanel();
	JButton addEventButton;
	
	public EventToolbarGroup() {
		super("");
		
		this.eventContentPanel.setLayout(new BoxLayout(eventContentPanel, BoxLayout.X_AXIS));

		JButton addEventButton = new JButton("<html>Add an<br/>Event</html>");
		
		addEventButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				//button action (addEvent method of some sort)
			}
		});
		
		try {
		    Image img = ImageIO.read(getClass().getResource("new_itt.png"));
		    addEventButton.setIcon(new ImageIcon(img));		    
		} catch (IOException ex) {}
		
		eventContentPanel.add(addEventButton);
		this.setOpaque(false);
		
		this.add(eventContentPanel);
	}
	
	@Override
	public void mouseEntered() {}
	
	@Override
	public void mouseExited() {}
}
