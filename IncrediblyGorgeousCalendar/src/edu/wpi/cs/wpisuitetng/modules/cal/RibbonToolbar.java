package edu.wpi.cs.wpisuitetng.modules.cal;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.DefaultToolbarView;

public class RibbonToolbar extends DefaultToolbarView {
	
	public EventToolbarGroup eventButtonGroup = new EventToolbarGroup();
	
	public RibbonToolbar(boolean visible) {
		
		this.addGroup(eventButtonGroup);
		
	}
}
