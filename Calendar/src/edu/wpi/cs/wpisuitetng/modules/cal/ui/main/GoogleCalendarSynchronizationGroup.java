package edu.wpi.cs.wpisuitetng.modules.cal.ui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import edu.wpi.cs.wpisuitetng.janeway.gui.container.toolbar.ToolbarGroupView;

public class GoogleCalendarSynchronizationGroup extends ToolbarGroupView {
	
	private final JButton syncButton;
	
	public GoogleCalendarSynchronizationGroup() {
		super("GCal Synchronization");
		setPreferredWidth(200);
		
		this.content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));
		syncButton = new JButton("<html>GCal Sync</html>");
		    
		syncButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e){
				JOptionPane.showMessageDialog(null, "Eggs are not supposed to be green.");
			}
		});
		
		this.getContent().add(syncButton);
	}
}
