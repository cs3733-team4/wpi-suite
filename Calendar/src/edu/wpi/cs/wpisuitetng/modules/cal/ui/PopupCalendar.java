/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.ui.navigation.MiniCalendarHostIface;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.navigation.MiniCalendarPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;

/**
 * MiniMonth-based popup calendar used for DatePicker.
 */
public class PopupCalendar extends JFrame {
	public PopupCalendar(DateTime date, final MiniCalendarHostIface mc) {
		MiniCalendarPanel cal = new MiniCalendarPanel(date, mc, true);
		this.add(cal);
		final JFrame me = this;
		
		((JComponent) getContentPane()).setBorder(BorderFactory.createLineBorder(Colors.BORDER));
		
		this.addWindowListener(new WindowListener(){

			@Override
			public void windowActivated(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosed(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowClosing(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowDeactivated(WindowEvent arg0) {
				((DatePicker)mc).miniCalendarInstance(false);
				me.dispose();
				
			}

			@Override
			public void windowDeiconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowIconified(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void windowOpened(WindowEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}	
}
