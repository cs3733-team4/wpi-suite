package edu.wpi.cs.wpisuitetng.modules.cal;

import java.awt.BorderLayout;
import java.awt.Color;
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

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.year.YearCalendarHolder;

public class MainPanel extends JPanel {

	private MonthCalendar moca;
	JTabbedPane calendarsAndEvents;

	public MainPanel() {
		this.setLayout(new BorderLayout());

		JPanel miniCalendar = new JPanel();
		JPanel mainCalendar = new JPanel();
		miniCalendar.setBackground(Color.red);
		miniCalendar.setPreferredSize(new Dimension(200, 1024));
		miniCalendar.add(new YearCalendarHolder(DateTime.now()));
		mainCalendar.setBackground(Color.green);

		this.add(miniCalendar, BorderLayout.WEST);
		this.add(mainCalendar, BorderLayout.CENTER);

		calendarsAndEvents = new JTabbedPane();
		moca = new MonthCalendar(DateTime.now());
		addCalendarTab(moca, "Calendar", false);
		addCalendarTab(new JLabel("hi there!"), "test", true);
		mainCalendar.setLayout(new BorderLayout());
		mainCalendar.add(calendarsAndEvents, BorderLayout.CENTER);
	}
	
	
	/**
	 * gives external access for adding tabs (that can be closed!)
	 * 
	 * @param moca the body of the tab, usually a calendar or an event creation/editing page
	 * @param name the name of the tab
	 * @param closeable whether the tab can be closed
	 */
	public void addCalendarTab(JComponent moca, String name, boolean closeable)
	{
		if (!closeable)
		{
			calendarsAndEvents.addTab(name, moca);
		}
		else
		{
			calendarsAndEvents.addTab(null, moca);
			final int tabPosition = calendarsAndEvents.indexOfComponent(moca);
			JPanel tabInformation = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
			JLabel tabInfoName = new JLabel(name);
			JButton tabInfoClose = new JButton("X"); // we need an icon for this eventually
			
			tabInfoClose.setFocusable(false);
			tabInfoClose.setBorder(null);
			
			tabInformation.add(tabInfoName);
			tabInformation.add(tabInfoClose);
			
			tabInformation.setBorder(BorderFactory.createEmptyBorder(2,0,0,0));
			calendarsAndEvents.setTabComponentAt(tabPosition, tabInformation);
			
			ActionListener listener = new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					calendarsAndEvents.remove(tabPosition);
				}
			};
			
			tabInfoClose.addActionListener(listener);
		}
	}
}
