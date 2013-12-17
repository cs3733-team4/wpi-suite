/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ToolTipManager;

import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;

public class ToolTipListener implements MouseListener {
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {
		if (true)
			ToolTipManager.sharedInstance().setEnabled(true);
		else
			ToolTipManager.sharedInstance().setEnabled(false);
			
		ToolTipManager.sharedInstance().setDismissDelay(500);
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
