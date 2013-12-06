/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.AbstractCalendar;

public class MainCalendarNavigation extends JPanel {

	private JButton nextButton = new JButton(">");
	private JButton previousButton = new JButton("<");
	private JButton todayButton = new JButton("Today");
	private JPanel navigationButtonPanel = new JPanel();
	private AbstractCalendar currentCalendar;

	public MainCalendarNavigation(JComponent parent, final AbstractCalendar mAbstractCalendar) {
		
		navigationButtonPanel.setLayout(new BorderLayout());
		navigationButtonPanel.add(nextButton, BorderLayout.EAST);
		navigationButtonPanel.add(todayButton, BorderLayout.CENTER);
		navigationButtonPanel.add(previousButton, BorderLayout.WEST);
		
		// Listens for arrow keyboard input
		this.setFocusable(true);
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub	
			}

			@Override
			public void keyTyped(KeyEvent e) {
				int pressedKey = e.getKeyCode();
				// 37 = left, 39 = right
				if (pressedKey == 37) {
					currentCalendar.previous();
				}
				else if (pressedKey == 39) {
					currentCalendar.next();
				}
			}
		});
		
		// Set current calendar
		this.currentCalendar = mAbstractCalendar;
		
		nextButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				currentCalendar.next();
			}
		});
		previousButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				currentCalendar.previous();
				
			}
		});
		todayButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				currentCalendar.display(DateTime.now());
			}
		});
		
		this.setLayout(new BorderLayout());
		this.add(navigationButtonPanel, BorderLayout.WEST);
	}
	
	public void updateCalendar (AbstractCalendar newCalendar){
		this.currentCalendar = newCalendar;
	}
}
