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

import edu.wpi.cs.wpisuitetng.modules.cal.day.DayCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;
import edu.wpi.cs.wpisuitetng.modules.cal.month.MonthCalendar;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.CalendarSelector;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.MainCalendarNavigation;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.MiniCalendarPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.GoToPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.MiniCalendarHostIface;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.ViewSize;

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
	private AbstractCalendar mCalendar, monthCal, dayCal;
	private DateTime lastTime = DateTime.now();
	private CalendarSelector mCalendarSelector;
	private JPopupMenu popup = new JPopupMenu();
	private JMenuItem closeAll = new JMenuItem("Close All Tabs");
	private int tabPosition;
	private final HashMap<Integer, JComponent> tabs = new HashMap<Integer, JComponent>();
	private int tab_id = 0;
	private EventModel events;
	private ViewSize view = ViewSize.Month;
	private static MainPanel instance;
	
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
		events = new EventModel(); // used for accessing events
		
		this.mainPaneContainer = new JPanel(); // Container for the navigation and calendars
		this.sidePanel = new JPanel(); // Panel to hold the mini calendar and the goto date
		this.centerPanel = new JPanel(); // Container for top and bottom sub-panels
		this.centerPanelTop = new JPanel(); // Container for navigation and calendar selector
		this.centerPanelBottom = new JPanel(); // Container for calendar itself
		
		// Components of center panel
		this.mMiniCalendarPanel = new MiniCalendarPanel(DateTime.now(), this); // Mini calendar
		this.mCalendar = monthCal = new MonthCalendar(DateTime.now(), events); // Monthly calendar
		dayCal = new DayCalendar(DateTime.now(), events); // Day calendar (hidden)
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
				//remove all but calender
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
					System.out.println(ID);
					mTabbedPane.remove(tabs.get(ID));
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
	 * Gets the singleton instance of this panel to avoid passing it everywhere
	 * @return the instance
	 */
	public static MainPanel getInstance()
	{
		if (instance == null)
			instance = new MainPanel();
		return instance;
	}

	public void viewMonth()
	{
		view = ViewSize.Month;
		refreshView(monthCal);
	}
	
	public void viewDay()
	{
		view = ViewSize.Day;
		refreshView(dayCal);
	}
	
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
	
	public void refreshView()
	{
		mCalendar.display(lastTime);
		revalidate();
		repaint();
	}

	public ViewSize getView()
	{
		return view;
	}
	
	public void closeTab(int id){
		mTabbedPane.remove(tabs.get(id));
	}
}
