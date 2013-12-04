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
import edu.wpi.cs.wpisuitetng.modules.cal.ui.CategoryManager;

public class CategoryToolbarGroup extends ToolbarGroupView {
	
	private final JButton editCategory;
	
	public CategoryToolbarGroup(final MainPanel mMainPanel) {
		super("Categories");
		setPreferredWidth(150);
		
		editCategory = new JButton("<html>Manage<br/>Categories</html>");
		editCategory.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				CategoryManager cat = new CategoryManager();
				cat.setTabId(mMainPanel.addTopLevelTab(cat, "Manage Categories", true));
				
				//TODO: use selected times. ned.display(DateTime.now());
			}
		});
		
		
		try {
		    Image img = ImageIO.read(getClass().getResource("category.png"));
		    editCategory.setIcon(new ImageIcon(img));
		} catch (IOException ex) {}

		this.getContent().add(editCategory);
	}
}
