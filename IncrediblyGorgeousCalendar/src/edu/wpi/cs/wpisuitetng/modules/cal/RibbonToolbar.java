package edu.wpi.cs.wpisuitetng.modules.cal;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;

public class RibbonToolbar extends DefaultToolbarView {
	
	public EventToolbarGroup eventButtonGroup;
	
	public RibbonToolbar(final MainPanel mMainPanel, boolean visible) {
		
		eventButtonGroup = new EventToolbarGroup(mMainPanel);
		this.addGroup(eventButtonGroup);
		eventButtonGroup.getRemoveEventButton().setEnabled(false);
		
	}
}
