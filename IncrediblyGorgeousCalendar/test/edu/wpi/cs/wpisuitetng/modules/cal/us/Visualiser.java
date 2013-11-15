package edu.wpi.cs.wpisuitetng.modules.cal.us;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import edu.wpi.cs.wpisuitetng.modules.cal.Calendar;

@SuppressWarnings("serial")
public class Visualiser extends JFrame{
	
	public static void main(String[] args)
	{	
		new Visualiser();
	}
	
	
	public Visualiser()
	{
		JComponent calendar = new Calendar().getTabs().get(0).getMainComponent();
		JPanel p = new JPanel();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} 
		catch (Exception e) {
			System.out.println("Error setting UI manager to cross-platform!");
			e.printStackTrace();
		}
		
		p.setLayout(new GridLayout(1,1));
		this.setLayout(new GridLayout(1,1));
		this.add(p);
		p.add(calendar);
		this.setSize(new Dimension(1200, 900));
		this.setVisible(true);
	}
}
