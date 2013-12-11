package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ToolTipManager;

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
		ToolTipManager.sharedInstance().setEnabled(true);
		System.out.println("mouse in, turning on...");
		
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
