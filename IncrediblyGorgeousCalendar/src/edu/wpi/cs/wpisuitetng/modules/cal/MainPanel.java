package edu.wpi.cs.wpisuitetng.modules.cal;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.month.MonthCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.MainCalendarNavigation;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.MiniCalendarPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.GoToPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.MiniCalendarHostIface;

@SuppressWarnings("serial")
public class MainPanel extends JTabbedPane implements MiniCalendarHostIface {
	
	private JTabbedPane mTabbedPane;
	private MiniCalendarPanel mMiniCalendarPanel;
	private JPanel mainPaneContainer;
	private JPanel centerPanel;
	private JPanel sidePanel;
	private JPanel mainCalendarNavigationPanel;
	private GoToPanel mGoToPanel;
	private AbstractCalendar mCalendar; 
	private int tabPosition;

	/** Tabbed main panel to display in the calendar module. This pane will contain
	 *  the rest of the elements in the calendar module, including the calendar view,
	 *  add event view, add commitment view, and so on.
	 */
	public MainPanel() {

		mTabbedPane = this; // Variable for creating new tabs in addTopLevelTab 
		
		this.mainPaneContainer = new JPanel(); // Container for the navigation and calendars
		this.sidePanel = new JPanel(); // Panel to hold the mini calendar and the goto date
		this.centerPanel = new JPanel(); // Container for center navigation bar and calendar pane
		
		// Components of center panel
		this.mMiniCalendarPanel = new MiniCalendarPanel(DateTime.now(), this); // Mini calendar
		this.mCalendar = new MonthCalendar(DateTime.now(), this); // Monthly calendar
		this.mainCalendarNavigationPanel = new MainCalendarNavigation(this, mCalendar); // Navigation bar 
		
		// Components of side panel
		this.mGoToPanel = new GoToPanel(DateTime.now(), mCalendar); // Go to date
		
 		
		// Set up side panel
		sidePanel.setPreferredSize(new Dimension(200, 1024));
		sidePanel.setLayout(new BorderLayout());
		sidePanel.add(mMiniCalendarPanel, BorderLayout.NORTH);
		sidePanel.add(mGoToPanel, BorderLayout.CENTER);
		
		
		// Add top bar and monthly calendar to center panel
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(new EmptyBorder(5, 5, 0, 0));
		centerPanel.add(mCalendar, BorderLayout.CENTER);
		centerPanel.add(mainCalendarNavigationPanel, BorderLayout.NORTH);
		
		// Set up the main panel
		mainPaneContainer.setLayout(new BorderLayout());
		mainPaneContainer.add(sidePanel, BorderLayout.WEST);
		mainPaneContainer.add(centerPanel, BorderLayout.CENTER);
		
		// Add default tabs to main panel
		addTopLevelTab(mainPaneContainer, "Calendar", false);
		addTopLevelTab(new JPanel(), "Test", true);
		
	}
	
	
	/**
	 * gives external access for adding tabs (that can be closed!)
	 * 
	 * @param component the content of the tab, usually a calendar or an event creation/editing page
	 * @param name the name of the tab
	 * @param closeable whether the tab can be closed
	 */
	
	public void addTopLevelTab(JComponent component, String name, boolean closeable)
	{
		
		if (!closeable)
		{
			mTabbedPane.addTab(name, component);
		}
		else
		{
			mTabbedPane.addTab(null, component);
			tabPosition = mTabbedPane.indexOfComponent(component);
			JPanel tabInformation = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
			JLabel tabInfoName = new JLabel(name);
			JButton tabInfoClose = new JButton("X"); // we need an icon for this eventually
			
			tabInfoName.setOpaque(false);
			tabInfoClose.setOpaque(false);
			
			tabInfoClose.setFocusable(false);
			tabInfoClose.setBorder(null);
			
			tabInformation.add(tabInfoName);
			tabInformation.add(tabInfoClose);
			
			tabInformation.setBorder(BorderFactory.createEmptyBorder(2,0,0,0));
			mTabbedPane.setTabComponentAt(tabPosition, tabInformation);
			
			ActionListener listener = new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					mTabbedPane.remove(tabPosition);
				}
			};
			
			tabInfoClose.addActionListener(listener);
		}
	}

	
	public AbstractCalendar getMOCA()
	{
		return mCalendar;
	}
	
	/**
	 * Changes the date being displayed on the calendar
	 * @param newTime time to set the calendar to
	 */
	public void display(DateTime newDate)
	{
		mCalendar.display(newDate);
		
	}

	/**
	 * Changes the date being displayed on the mini calendar
	 * @param date the date to move the mini calendar to
	 */
	public void miniMove(DateTime date) {
		mMiniCalendarPanel.display(date);
	}
	
}
