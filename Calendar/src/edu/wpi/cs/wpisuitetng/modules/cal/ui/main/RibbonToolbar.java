/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui.main;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;

public class RibbonToolbar extends DefaultToolbarView {
	
	public DisplayableToolbarGroup eventButtonGroup;
	public CategoryToolbarGroup categoryButtonGroup;
	public HelpToolbarGroup helpButtonGroup;
	public GoogleCalendarSynchronizationGroup syncGroup;
	
	public RibbonToolbar(final MainPanel mMainPanel, boolean visible) {
		helpButtonGroup = new HelpToolbarGroup();
		eventButtonGroup = new DisplayableToolbarGroup(mMainPanel);
		categoryButtonGroup = new CategoryToolbarGroup(mMainPanel);
		syncGroup = new GoogleCalendarSynchronizationGroup();
		
		this.setFocusable(false);
		
		this.addGroup(eventButtonGroup);
		this.addGroup(categoryButtonGroup);
		this.addGroup(helpButtonGroup);
		this.addGroup(syncGroup);
		
		eventButtonGroup.disableRemoveEventButton();
	}
}
