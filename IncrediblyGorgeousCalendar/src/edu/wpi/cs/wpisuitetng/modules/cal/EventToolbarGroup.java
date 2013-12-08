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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.print.Doc;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolTip;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.cal.documentation.DocumentMainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.AddCommitmentDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.AddEventDisplay;

public class EventToolbarGroup extends ToolbarGroupView {
	
	private final JButton addEventButton, removeEventButton, addCommitmentButton;
	public EventToolbarGroup(final MainPanel mMainPanel) {
		super("Events & Commitments");
		setPreferredWidth(600);
		
		this.content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
		
		addEventButton = new JButton("<html>Add<br/>Event</html>"){
			JToolTip toolTip;  
			@Override  
		    public JToolTip createToolTip() {  
				if (toolTip == null) {  
					JPanel panel = new JPanel(new GridLayout(0, 1));  
					JLabel label = new JLabel("<html> <tab>Lets you make a new event in<br>the calendar</html>");
			        JButton button = new JButton("Get Help");  
			        button.addActionListener(new ActionListener() {  
			        	public void actionPerformed(ActionEvent e) {  
			        		DocumentMainPanel.getInstance().setVisible(!DocumentMainPanel.getInstance().isVisible());
			        		DocumentMainPanel.getInstance().goToPage("CreateanEvent.html");  
			            }  
			        });  
			        panel.add(label);
			        panel.add(button);  
			        
			  
			        toolTip = super.createToolTip();  
			        toolTip.setLayout(new BorderLayout());  
			        Insets insets = toolTip.getInsets();  
			        Dimension panelSize = panel.getPreferredSize();  
			        panelSize.width += insets.left + insets.right+5;  
			        panelSize.height += insets.top + insets.bottom;  
			        toolTip.setPreferredSize(panelSize);  
			        toolTip.setBackground(super.createToolTip().getBackground());
			        label.setBackground(toolTip.getBackground());
			        toolTip.add(panel);  
		        }  
		        return toolTip;  
			} 
			@Override
			public Point getToolTipLocation(MouseEvent e)
			{
				return new Point(95, 40);
			}
		};
		addEventButton.setToolTipText("h");
		    
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

		this.getContent().add(addEventButton);
		this.getContent().add(addCommitmentButton);
	}
	
	public void disableRemoveEventButton(){
		removeEventButton.setEnabled(false);
	}
}
