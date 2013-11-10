package edu.wpi.cs.wpisuitetng.modules.cal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

public class MainPanel extends JPanel {

	private MonthCalendar moca;

	public MainPanel() {
		this.setLayout(new BorderLayout());

		JPanel miniCalendar = new JPanel();
		JPanel mainCalendar = new JPanel();
		miniCalendar.setBackground(Color.red);
		miniCalendar.setPreferredSize(new Dimension(150, 1024));
		mainCalendar.setBackground(Color.green);

		this.add(miniCalendar, BorderLayout.WEST);
		this.add(mainCalendar, BorderLayout.CENTER);

		JTabbedPane tabPane = new JTabbedPane();
		moca = new MonthCalendar(DateTime.now());
		tabPane.addTab("Calendar View", moca);
		mainCalendar.setLayout(new BorderLayout());
		mainCalendar.add(tabPane, BorderLayout.CENTER);
	}
}
