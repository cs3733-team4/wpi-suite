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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CommitmentModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.CalendarSelector;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.GoToPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.MainCalendarNavigation;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.MiniCalendarHostIface;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.MiniCalendarPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.ViewSize;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.AddCommitmentDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.AddEventDisplay;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.day.DayCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthItem;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.year.YearCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;

/**
 * The main UI of the Calendar module. This singleton is basically the controller for everything
 * in the calendar module. It manages most resources.
 */
public class MainPanel extends JTabbedPane implements MiniCalendarHostIface {
	
	private JTabbedPane mTabbedPane;
	private MiniCalendarPanel mMiniCalendarPanel;
	private JPanel mainPaneContainer;
	private JPanel centerPanel;
	private JPanel centerPanelTop;
	private JPanel centerPanelBottom;
	private JPanel sidePanel;
	private MainCalendarNavigation mainCalendarNavigationPanel;
	private GoToPanel mGoToPanel;
	private AbstractCalendar mCalendar, monthCal, dayCal, yearCal;
	private DateTime lastTime = DateTime.now();
	private CalendarSelector mCalendarSelector;
	private JPopupMenu popup = new JPopupMenu();
	private JMenuItem closeAll = new JMenuItem("Close All Tabs");
	private int tabPosition;
	private final HashMap<Integer, JComponent> tabs = new HashMap<Integer, JComponent>();
	private int tab_id = 0;
	private EventModel events;
	private CommitmentModel commitments;
	private ViewSize view = ViewSize.Month;
	private static MainPanel instance;
	private Displayable currentSelected;
	
	//TODO: "make this better" -Patrick
	public boolean showPersonal = true;
	public boolean showTeam = false;

	/** Tabbed main panel to display in the calendar module. This pane will contain
	 *  the rest of the elements in the calendar module, including the calendar view,
	 *  add event view, add commitment view, and so on.
	 */
	public MainPanel()
	{
		if (instance != null)
			throw new RuntimeException("Trying to create more than one calendar panel!");

		instance = this; // Variable for creating new tabs in addTopLevelTab
	}
	
	@Override
	public void paint(Graphics g)
	{
		// call finish init always, it has a if to prevent multiple calls. see below comment on why
		finishInit();
		super.paint(g);
	}
	
	/**
	 * This is called AFTER login, because for some reason janeway inits all the panels
	 * before the network (aka login) is setup. If we initialize before then, we crash as 
	 * there is no network session.
	 */
	void finishInit()
	{
		if (mTabbedPane == this)
			return;
		mTabbedPane = this;
		this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		
		events = new EventModel(); // used for accessing events
		commitments= new CommitmentModel();
		this.mainPaneContainer = new JPanel(); // Container for the navigation and calendars
		this.sidePanel = new JPanel(); // Panel to hold the mini calendar and the goto date
		this.centerPanel = new JPanel(); // Container for top and bottom sub-panels
		this.centerPanelTop = new JPanel(); // Container for navigation and calendar selector
		this.centerPanelBottom = new JPanel(); // Container for calendar itself
		
		// Components of center panel
		this.mMiniCalendarPanel = new MiniCalendarPanel(DateTime.now(), this); // Mini calendar
		this.mCalendar = monthCal = new MonthCalendar(DateTime.now(), events, commitments); // Monthly calendar
		this.dayCal = new DayCalendar(DateTime.now(), events); // Day calendar (hidden)
		this.yearCal = new YearCalendar(DateTime.now(), events); // Year calendar (hidden)
		
		this.mainCalendarNavigationPanel = new MainCalendarNavigation(this, mCalendar); // Navigation bar 
		
		// Components of side panel
		this.mGoToPanel = new GoToPanel(DateTime.now()); // Go to date
		
		// Calendar selector
		this.mCalendarSelector = new CalendarSelector();
		
 		
		// Set up side panel
		sidePanel.setPreferredSize(new Dimension(200, 1024));
		sidePanel.setLayout(new BorderLayout());
		sidePanel.setBorder(new EmptyBorder(5, 5, 0, 0));
		sidePanel.add(mMiniCalendarPanel, BorderLayout.NORTH);
		sidePanel.add(mGoToPanel, BorderLayout.CENTER);
		
		// Set up center panel elements
		centerPanelTop.setLayout(new BorderLayout());
		centerPanelTop.add(mainCalendarNavigationPanel, BorderLayout.WEST);
		centerPanelTop.add(mCalendarSelector, BorderLayout.EAST);
		
		centerPanelBottom.setLayout(new BorderLayout());
		centerPanelBottom.add(mCalendar, BorderLayout.CENTER);
		
		
		// Add top bar and monthly calendar to center panel
		centerPanel.setLayout(new BorderLayout());
		centerPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		centerPanel.add(centerPanelTop, BorderLayout.NORTH);
		centerPanel.add(centerPanelBottom, BorderLayout.CENTER);
		
		// Set up the main panel
		mainPaneContainer.setLayout(new BorderLayout());
		mainPaneContainer.add(sidePanel, BorderLayout.WEST);
		mainPaneContainer.add(centerPanel, BorderLayout.CENTER);
		
		// Add default tabs to main panel
		addTopLevelTab(mainPaneContainer, "Calendar", false);
	
		// add context menu
		this.addMouseListener(new MouseAdapter()
		{
			@Override
			public void mousePressed(MouseEvent e)
			{
				if(e.isPopupTrigger() && indexAtLocation(e.getX(), e.getY()) != -1) popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		popup.add(closeAll);
		closeAll.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				//remove all but calendar
				while (getTabCount() > 1)
				{
					removeTabAt(1);
				}
			}
		});
		
		
		
	}
	
	
	/**
	 * gives external access for adding tabs (that can be closed!)
	 * 
	 * @param component the content of the tab, usually a calendar or an event creation/editing page
	 * @param name the name of the tab
	 * @param closeable whether the tab can be closed
	 */
	
	public int addTopLevelTab(JComponent component, String name, boolean closeable)
	{
		
		if (!closeable)
		{
			mTabbedPane.addTab(name, component);
			return -1;
		}
		else
		{
			class Title extends JButton {
				public final int ID;
				public Title(String name, int ID){
					super(name);
					this.ID = ID;
				}
			}
			mTabbedPane.addTab(null, component);
			tabPosition = mTabbedPane.indexOfComponent(component);
			JPanel tabInformation = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
			JLabel tabInfoName = new JLabel(name);
			Title tabInfoClose = new Title("\u2716", tab_id++); // we need an icon for this eventually
			tabInfoClose.setFont(tabInfoClose.getFont().deriveFont((float) 8));
			tabInfoClose.setMargin(new Insets(0, 0, 0, 0));
			tabInfoClose.setPreferredSize(new Dimension(20,17));
			
			tabInformation.setOpaque(false);
			
			tabInfoClose.setFocusable(false);
			tabInfoClose.setBorder(null);
			
			tabInformation.add(tabInfoName);
			tabInformation.add(tabInfoClose);
			
			tabInformation.setBorder(BorderFactory.createEmptyBorder(2,0,0,0));
			mTabbedPane.setTabComponentAt(tabPosition, tabInformation);
			
			tabs.put(tabInfoClose.ID, component);
			
			ActionListener listener = new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					int ID = ((Title)e.getSource()).ID;
					mTabbedPane.remove(tabs.get(ID));
					tabs.remove(ID);
				}
			};
			
			tabInfoClose.addActionListener(listener);
			mTabbedPane.setSelectedIndex(tabPosition);
			return tabInfoClose.ID;
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
		refreshView();
		lastTime = newDate;
	}

	/**
	 * Changes the date being displayed on the mini calendar
	 * @param date the date to move the mini calendar to
	 */
	public void miniMove(DateTime date)
	{
		mMiniCalendarPanel.display(date);
		lastTime = date;
	}
	
	/**
	 * Adds a new event to the database and refreshes the UI
	 * @param newEvent The event to add
	 */
	public void addEvent(Event newEvent)
	{
		events.putEvent(newEvent);
		mCalendar.updateEvents(newEvent, true);
	}
	
	/**
	 * Updates an event as long as it retains its ID
	 * @param updateEvent event to update
	 */
	public void updateEvent(Event updateEvent){
		events.updateEvent(updateEvent);
	}
	
	/**
	 * Adds a new commitment to the database and refreshes the UI
	 * @param newCommitment The commitment to add
	 */
	public void addCommitment(Commitment newCommitment)
	{
		commitments.putCommitment(newCommitment);
	}
	/**
	 * Updates a commitment as long both commitments have the same ID
	 * @param updateCommitment
	 */
	public void updateCommitment(Commitment updateCommitment)
	{
		commitments.updateCommitment(updateCommitment);
	}

	/**
	 * Gets the singleton instance of this panel to avoid passing it everywhere
	 * @return the instance
	 */
	public static MainPanel getInstance()
	{
		if (instance == null)
			instance = new MainPanel();
		return instance;
	}

	/**
	 * Toggle monthly calendar view
	 */
	public void viewMonth()
	{
		view = ViewSize.Month;
		refreshView(monthCal);
	}
	
	/**
	 * Toggle daily calendar view
	 */
	public void viewDay()
	{
		view = ViewSize.Day;
		refreshView(dayCal);
	}
	
	public void viewYear()
	{
		view = ViewSize.Month;
		refreshView(yearCal);
	}
	
	/**
	 * Updates calendar in view and sets navigation panel to act on the active view
	 * @param monthCal2
	 */
	private void refreshView(AbstractCalendar monthCal2)
	{
		centerPanelBottom.remove(mCalendar);
		mCalendar = monthCal2;
		mainCalendarNavigationPanel.updateCalendar(mCalendar);
		centerPanelBottom.add(mCalendar, BorderLayout.CENTER);
		mCalendar.display(lastTime);
		revalidate();
		repaint();
	}
	
	/**
	 * Refresh the view to properly show additions and navigation
	 */
	public void refreshView()
	{
		mCalendar.display(lastTime);
		revalidate();
		repaint();
	}

	/**
	 * @return current view
	 */
	public ViewSize getView()
	{
		return view;
	}
	
	/**
	 * Close specified tab
	 * @param id
	 */
	public void closeTab(int id)
	{
		mTabbedPane.remove(tabs.get(id));
	}
	
	/**
	 * Highlights the  selected monthItem on the calendar
	 * @param Item the month item to highlight
	 */
	public void updateSelectedDisplayable(Displayable item)
	{
		mCalendar.select(item);
		this.currentSelected = item;	
	}
	
	/**
	 * Edits the selected displayable
	 * @param Item the month item containing the displayable to edit
	 */
	public void editSelectedDisplayable(Displayable item)
	{
		updateSelectedDisplayable(item);
		
		if (item instanceof Event) {
			AddEventDisplay mAddEventDisplay = new AddEventDisplay((Event) item);
			boolean openNewTab = true;
			JComponent tabToOpen = null;
			
			for(JComponent c : tabs.values())
			{
				if (openNewTab && c instanceof AddEventDisplay)
				{
					openNewTab = !((AddEventDisplay) c).matchingEvent(mAddEventDisplay);
					tabToOpen = c;
				}
			}
			if (openNewTab)
			{
				mAddEventDisplay.setTabId(instance.addTopLevelTab(mAddEventDisplay, "Edit Event", true));
			}
			else if (tabToOpen != null)
			{
				this.mTabbedPane.setSelectedComponent(tabToOpen);
			}
			
		}
		else if (item instanceof Commitment) {
			AddCommitmentDisplay mAddCommitmentDisplay = new AddCommitmentDisplay((Commitment) item);
			mAddCommitmentDisplay.setTabId(instance.addTopLevelTab(mAddCommitmentDisplay, "Edit Commitment", true));
		}
	}
	
	/**
	 * Clears selected MonthItem from calendar
	 */
	public void clearSelected()
	{
		updateSelectedDisplayable(null);
	}
}
