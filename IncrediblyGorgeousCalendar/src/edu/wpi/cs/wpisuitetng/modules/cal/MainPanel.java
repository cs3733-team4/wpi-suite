package edu.wpi.cs.wpisuitetng.modules.cal;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.wpi.cs.wpisuitetng.janeway.modules.JanewayTabModel;

public class MainPanel extends JPanel {

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
		tabPane.addTab("Calendar View", new JPanel());
		mainCalendar.setLayout(new BorderLayout());
		mainCalendar.add(tabPane, BorderLayout.CENTER);
	}
}
