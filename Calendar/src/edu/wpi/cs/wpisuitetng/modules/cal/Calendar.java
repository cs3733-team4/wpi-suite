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
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.JobPrioritySupported;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule;
import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.RibbonToolbar;

/**
 * Calendar class. This is the main Janeway entry point for our calendar.
 */
public class Calendar implements IJanewayModule
{
	/** The tabs used by this module. We only have one */
	private ArrayList<JanewayTabModel> tabs;

	/**
	 * Construct a new Calendar Tab Module
	 */
	public Calendar()
	{
		MainPanel mMainPanel = new MainPanel();

		// Setup button panel
		RibbonToolbar buttonPanel = new RibbonToolbar(mMainPanel, true);
		buttonPanel.setFloatable(false);

		tabs = new ArrayList<JanewayTabModel>();
		JanewayTabModel tab = new JanewayTabModel(getName(), new ImageIcon(),
				buttonPanel, mMainPanel);

		tabs.add(tab);
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getName()
	 */
	@Override
	public String getName()
	{
		return "Calendar";
	}

	/**
	 * @see edu.wpi.cs.wpisuitetng.janeway.modules.IJanewayModule#getTabs()
	 */
	@Override
	public List<JanewayTabModel> getTabs()
	{
		return tabs;

	}

}
