package edu.wpi.cs.wpisuitetng.modules.cal.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class RepeatButton extends JButton
{
	public RepeatButton()
	{
		super("never");
		this.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void update(RepeatPicker rp)
	{
		
	}
}
