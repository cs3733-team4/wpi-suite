/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.cal.eventui.AddCommitmentDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.eventui.AddEventDisplay;

public class EventToolbarGroup extends ToolbarGroupView {
	
	private final JPanel eventContentPanel = new JPanel();
	private final JButton addEventButton, removeEventButton, addCommitmentButton;
	
	public EventToolbarGroup(final MainPanel mMainPanel) {
		super("Events");
		
		//this.eventContentPanel.setLayout(new BoxLayout(eventContentPanel, BoxLayout.X_AXIS));
		
		addEventButton = new JButton("<html>Add<br/>Event</html>");
		addEventButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				AddEventDisplay ned = new AddEventDisplay();
				ned.setTabId(mMainPanel.addTopLevelTab(ned, "New Event", true));
				
				//TODO: use selected times. ned.display(DateTime.now());
			}
		});
		
		addCommitmentButton = new JButton("<html>Add<br/>Commitment</html>");
		addCommitmentButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				AddCommitmentDisplay newCommitment = new AddCommitmentDisplay();
				newCommitment.setTabId(mMainPanel.addTopLevelTab(newCommitment, "New Commitment", true));
				
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
		    addCommitmentButton.setIcon(new ImageIcon(img));
		    
		    img = ImageIO.read(getClass().getResource("del_event.png"));
		    removeEventButton.setIcon(new ImageIcon(img));
		} catch (IOException ex) {}

		//eventContentPanel.add(addEventButton);
		//eventContentPanel.add(removeEventButton);
		this.getContent().add(addEventButton);
		this.getContent().add(addCommitmentButton);
	}
	
	public void disableRemoveEventButton(){
		removeEventButton.setEnabled(false);
	}
}
