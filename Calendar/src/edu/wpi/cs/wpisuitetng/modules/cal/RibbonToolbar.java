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

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;

public class RibbonToolbar extends DefaultToolbarView {
	
	public EventToolbarGroup eventButtonGroup;
	public CategoryToolbarGroup categoryButtonGroup;
	public HelpToolbarGroup helpButtonGroup;
	
	public RibbonToolbar(final MainPanel mMainPanel, boolean visible) {
		helpButtonGroup = new HelpToolbarGroup();
		eventButtonGroup = new EventToolbarGroup(mMainPanel);
		categoryButtonGroup = new CategoryToolbarGroup(mMainPanel);
		this.addGroup(eventButtonGroup);
		this.addGroup(categoryButtonGroup);
		this.setFocusable(false);
		this.addGroup(helpButtonGroup);
		eventButtonGroup.disableRemoveEventButton();
	}
}
