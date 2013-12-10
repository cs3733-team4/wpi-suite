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
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.cal.documentation.DocumentMainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.AddEventDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.CategoryManager;

public class HelpToolbarGroup extends ToolbarGroupView {
	
	private final JButton showHelp;
	
	public HelpToolbarGroup() {
		super("Help");
		this.content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
		
		showHelp = new JButton("<html>Get<br/>Help</html>");
		showHelp.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
        		DocumentMainPanel.getInstance().setVisible(true);
        		DocumentMainPanel.getInstance().requestFocus();
        		DocumentMainPanel.getInstance().goToPage("Introduction.html");
				
				
				//TODO: use selected times. ned.display(DateTime.now());
			}
		});
		
		
		try {
		    Image img = ImageIO.read(getClass().getResource("gethelp.png"));
		    showHelp.setIcon(new ImageIcon(img));
		} catch (IOException ex) {}

		this.getContent().add(showHelp);
	}
}
