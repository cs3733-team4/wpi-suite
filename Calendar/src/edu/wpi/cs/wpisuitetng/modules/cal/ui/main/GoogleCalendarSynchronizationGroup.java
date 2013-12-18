package edu.wpi.cs.wpisuitetng.modules.cal.ui.main;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.tabs.GoogleCalendarSyncAuthenticateDisplay;

public class GoogleCalendarSynchronizationGroup extends ToolbarGroupView {
	
	private final JButton syncButton;
	
	/**
	 * make the menu for syncing with gcal
	 */
	public GoogleCalendarSynchronizationGroup() {
		super("Import from Google");
		//setPreferredWidth(200);
		
		this.content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
		syncButton = new JButton("<html>Import from Google</html>");
		    
		syncButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{	
				GoogleCalendarSyncAuthenticateDisplay ned = new GoogleCalendarSyncAuthenticateDisplay();
				MainPanel.getInstance().addGoogleLoginPage(ned);
			}
		});
		
		try
		{
		    Image img = ImageIO.read(getClass().getResource("/edu/wpi/cs/wpisuitetng/modules/cal/img/go-down.png"));
		    syncButton.setIcon(new ImageIcon(img));
		} 
		catch (IOException ex) {}
		
		this.getContent().add(syncButton);
	}
}
