package edu.wpi.cs.wpisuitetng.modules.cal.ui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.tabs.GoogleCalendarSyncAuthenticateDisplay;

public class GoogleCalendarSynchronizationGroup extends ToolbarGroupView {
	
	private final JButton syncButton;
	
	/**
	 * make the menu for syncing with gcal
	 */
	public GoogleCalendarSynchronizationGroup() {
		super("GCal Synchronization");
		setPreferredWidth(200);
		
		this.content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
		syncButton = new JButton("<html>GCal Sync</html>");
		    
		syncButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				GoogleCalendarSyncAuthenticateDisplay ned = new GoogleCalendarSyncAuthenticateDisplay();
				MainPanel.getInstance().addTopLevelTab(ned, "Log in with Google", true);
			}
		});
		
		this.getContent().add(syncButton);
	}
}
