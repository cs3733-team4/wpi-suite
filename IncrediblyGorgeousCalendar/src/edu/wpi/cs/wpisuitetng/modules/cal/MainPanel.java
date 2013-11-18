package edu.wpi.cs.wpisuitetng.modules.cal;

import java.awt.BorderLayout;
import java.awt.Component;
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.eventui.NewEventDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.month.MonthCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.MainCalendarNavigation;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.MiniCalendarHostIface;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.CalendarNavigationPanel;

@SuppressWarnings("serial")
public class MainPanel extends JTabbedPane implements MiniCalendarHostIface {

	/** Tabbed main panel to display in the calendar module. This pane will contain
	 *  the rest of the elements in the calendar module, including the calendar view,
	 *  add event view, add commitment view, and so on.
	 */
	
	private AbstractCalendar mCalendar; 
	JTabbedPane mTabbedPane;
	private CalendarNavigationPanel mCalendarNavigationPanel;
	private NewEventDisplay mEventCreator;

	public MainPanel() {

		JPanel mainPaneContainer = new JPanel(); // Container for the navigation and calendars
		JPanel miniCalendar = new JPanel(); // Mini calendar
		JPanel centerPanel = new JPanel(); // Container for center navigation bar and calendar pane
		
		mTabbedPane = this; // Variable for creating new tabs in addCalendarTab
		
		mainPaneContainer.setLayout(new BorderLayout());
		
		// Set up mini calendar
		miniCalendar.setPreferredSize(new Dimension(200, 1024));
		mCalendarNavigationPanel = new CalendarNavigationPanel(DateTime.now(), this);
		miniCalendar.add(mCalendarNavigationPanel);
		
		// Set up monthly calendar
		mCalendar = new MonthCalendar(DateTime.now(), this);
		
		//Set up top bar panel
		JPanel mainCalendarNavigationPanel = new MainCalendarNavigation(this, mCalendar); // Navigation bar above calendar
		
		// Add top bar and monthly calendar to center panel
		centerPanel.setLayout(new BorderLayout());
		centerPanel.add(mCalendar, BorderLayout.CENTER);
		centerPanel.add(mainCalendarNavigationPanel, BorderLayout.NORTH);
		
		// Add mini calendar and center panel to the main pane
		mainPaneContainer.add(miniCalendar, BorderLayout.WEST);
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
	public void addTopLevelTab(final JComponent component, String name, boolean closeable)
	{
		
		if (!closeable)
		{
			mTabbedPane.addTab(name, component);
		}
		else
		{
			mTabbedPane.addTab(null, component);
			int tabPosition = mTabbedPane.indexOfComponent(component);
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
					mTabbedPane.remove(indexOfComponent(component));
				}
			};
			
			mTabbedPane.addChangeListener(new ChangeListener() {
				
				@Override
				public void stateChanged(ChangeEvent e) {
					/* this code generates null pointer exceptions
					if(! mTabbedPane.getTitleAt(mTabbedPane.getSelectedIndex()).equals("New Event")) {
						mEventCreator.display(DateTime.now());
					}
					*/
				}
			});
			
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
		mCalendarNavigationPanel.display(date);
	}
}
