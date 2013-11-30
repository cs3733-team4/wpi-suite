/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.day;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;

public class DayGridLabel extends JPanel{
	
	private static DayGridLabel instance;
	
	public static DayGridLabel getInstance()
	{
		if(instance == null)
			instance = new DayGridLabel();
		return instance;
	}
	
	private DayGridLabel()
	{
		this.setLayout(new GridLayout(24, 1));
		this.setBackground(Colors.TABLE_BACKGROUND);
		
		for(int i = 0; i < 24; i++)
		{
			int hour = i%12==0?12:i%12;
			String padding = (hour < 10) ? "     " : "    ";
			JLabel text = new JLabel(new StringBuilder().append(hour)
														.append(":00")
                    									.append(padding)
                    									.toString());
			text.setBackground(Colors.TABLE_BACKGROUND);
			text.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 1, Colors.BORDER));
			this.add(text);
		}
	}
	
}
