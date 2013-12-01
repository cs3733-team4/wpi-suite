package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.eventui.AddCommitmentDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.eventui.AddEventDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public class SidebarTabbedPane extends JTabbedPane{
	
	private JPanel detailTab;
	private JTextArea detailTextArea;
	private JScrollPane detailScrollPane;
	private JPanel detailButtonPane;
	private JButton detailEditButton;
	private JButton detailCancelButton;
	private JTextArea commitmentTab;
	private DateTimeFormatter timeFormatter = DateTimeFormat.forPattern("hh:mm aa");
	private DateTimeFormatter dateFormatter = DateTimeFormat.forPattern("MM/dd/yy");
	private Displayable currentDisplayable;
	
	/**
	 * Tabbed panel in the navigation sidebar to hold additional details of selected items
	 */
	public SidebarTabbedPane() {
		
		//setup tab policy
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		setupDetailTab();
	    
		commitmentTab = new JTextArea();
		
		//add tabs
		this.addTab("Details", detailScrollPane);
		this.addTab("Commitments", commitmentTab);
	}
	
	/**
	 * initializes all the components of the details tab
	 */
	private void setupDetailTab()
	{
		// setup container panel
		detailTab = new JPanel();
		detailTab.setLayout(new BorderLayout());
		
		// setup text area
		detailTextArea = new JTextArea();
		detailTextArea.setEditable(false);
		detailTextArea.setLineWrap(true);
		detailTextArea.setCursor(null);  
		detailTextArea.setOpaque(false);  
		detailTextArea.setFocusable(false);
	    detailTextArea.setWrapStyleWord(true);
	    detailTextArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
	    detailTextArea.putClientProperty("html.disable", true);
	    
	    // setup buttons and listeners
	    detailEditButton = new JButton("Edit");
	    detailEditButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				MainPanel instance = MainPanel.getInstance();
				
				if (currentDisplayable instanceof Event) {
					AddEventDisplay mAddEventDisplay = new AddEventDisplay((Event) currentDisplayable);
					mAddEventDisplay.setTabId(instance.addTopLevelTab(mAddEventDisplay, "Edit Event", true));
				}
				else if (currentDisplayable instanceof Commitment) {
					AddCommitmentDisplay mAddCommitmentDisplay = new AddCommitmentDisplay((Commitment) currentDisplayable);
					mAddCommitmentDisplay.setTabId(instance.addTopLevelTab(mAddCommitmentDisplay, "Edit Commitment", true));
				}				
			}
		});
	    
	    detailCancelButton = new JButton("Cancel");
	    detailCancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});

	    // buttons disabled by default
	    setButtonsEnabled(false);
	    
	    // add buttons to button container
	    detailButtonPane = new JPanel();
	    detailButtonPane.setLayout(new FlowLayout());
	    detailButtonPane.add(detailEditButton);
	    detailButtonPane.add(detailCancelButton);
	    
	    // add text area and button container to detail tab
	    detailTab.add(detailTextArea, BorderLayout.CENTER);
	    detailTab.add(detailButtonPane, BorderLayout.SOUTH);
	    
	    // put entire tab into a scroll pane
	    detailScrollPane = new JScrollPane(detailTab);
	    detailScrollPane.setBorder( new EmptyBorder(5,5,5,5));
	}
	
	/**
	 * Sets the enabled status of the edit and cancel buttons
	 * @param enabled flag to set enabled status
	 */
	private void setButtonsEnabled(boolean enabled) {
		detailEditButton.setEnabled(enabled);
		detailCancelButton.setEnabled(enabled);
	}
	
	/**
	 * Updates the text area to display information about a displayable
	 * @param mDisplayable the displayable to show
	 */
	public void showDetails(Displayable mDisplayable)
	{
		currentDisplayable = mDisplayable;
		
		if (mDisplayable instanceof Event)
		{
			detailTextArea.setText(mDisplayable.getName() + "\n" + 
					((Event) mDisplayable).getStart().toString(timeFormatter) + " - " +
					((Event) mDisplayable).getEnd().toString(timeFormatter) + "\n" + 
					mDisplayable.getDescription());
			setButtonsEnabled(true);
		}else if (mDisplayable instanceof Commitment)
		{
			detailTextArea.setText(mDisplayable.getName() + "\n" + 
					((Commitment) mDisplayable).getDate().toString(dateFormatter) + "\n" +
					mDisplayable.getDescription());
			setButtonsEnabled(true);
		}
	}

	/**
	 * clears the text area of any details
	 */
	public void clearDetails() {
		detailTextArea.setText("");
		setButtonsEnabled(false);
	}
}
