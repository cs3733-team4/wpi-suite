/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;


public class Calendar implements IJanewayModule {


	/** The tabs used by this module */
	private ArrayList<JanewayTabModel> tabs;
	
	/**
	 * Construct a new Calendar Tab Module
	 */
	public Calendar() {
		
		// Setup button panel
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(new JButton("Add Event"));
		buttonPanel.add(new JButton("Edit Event Event"));
		buttonPanel.add(new JButton(" "));
		buttonPanel.add(new JButton("Day"));
		buttonPanel.add(new JButton("Month"));
		buttonPanel.add(new JButton("Year"));
		buttonPanel.setMaximumSize(new Dimension(1920, 50));
		
		JPanel mainPanel = new MainPanel();
		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel("Calendar", new ImageIcon(), buttonPanel, mainPanel);
		
		tabs.add(tab);
		
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName() {
		return "Calendar";
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 */
	@Override
	public List<JanewayTabModel> getTabs() {
		return tabs;
		
	}

}

