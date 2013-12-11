/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.utils;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.UIManager;

/**
 * Place for consistent UI colors. Compensates for platform issues. Supports all LAF's
 */
public class Colors
{
	public static final Color 
			TABLE_BACKGROUND = UIManager.getDefaults().getColor("Table.background"),
			SELECTED_BACKGROUND = UIManager.getDefaults().getColor("textHighlight"),
			SELECTED_TEXT = UIManager.getDefaults().getColor("textHighlightText"),
			BORDER = UIManager.getDefaults().getColor("Separator.foreground"),
			TABLE_GRAY_TEXT = UIManager.getDefaults().getColor("Table.focusCellForeground"),
			COMMITMENT_NOTIFICATION = Color.RED,
			TABLE_TEXT = UIManager.getDefaults().getColor("Label.foreground");
	
	
	public static final Color TABLE_GRAY_HEADER = UIManager.getColor("Panel.background");  // For same color as default panel bg
	// public static final Color TABLE_GRAY_HEADER =getTableGrayHeader();	// For headers that are darker than default panel bg
	
	/**
	 * Computes the proper color for the gray header. Annoying on Mac OS X native & Nimbus LAF.
	 */
	private static Color getTableGrayHeader()
	{
		Color grayit = UIManager.getDefaults().getColor("Table.focusCellBackground");
		Color bg = TABLE_BACKGROUND;
		if (bg.equals(grayit))
		{
			if ((bg.getBlue() + bg.getGreen() + bg.getRed()) / 3.0 > 255.0 / 2) // light
				grayit = grayit.darker();
			else
				grayit = grayit.brighter();
		};
		if (grayit.equals(Color.black))
			grayit = new Color(230, 230, 230);
		return grayit;
	}
}
