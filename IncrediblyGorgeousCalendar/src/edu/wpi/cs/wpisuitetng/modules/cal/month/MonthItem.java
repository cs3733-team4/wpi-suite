/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.cs.wpisuitetng.modules.cal.month;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;


public class MonthItem extends JPanel
{
	private static final long serialVersionUID = 6475224766889058195L;
	
	JLabel time = new JLabel(), desc = new JLabel();
	private Displayable mDisplayable;
	/**
	 * MonthItem Constructor
	 * @param when
	 * @param descr
	 */
	public MonthItem(Displayable ndisp)
	{
        setBackground(Colors.TABLE_BACKGROUND);
        setMaximumSize(new java.awt.Dimension(32767, 24));
        setMinimumSize(new java.awt.Dimension(0, 0));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));

        this.mDisplayable = ndisp;
        
        time.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 12));
        time.setText(simpleTime(mDisplayable.getDate()));
        time.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 3));
        add(time);

        desc.putClientProperty("html.disable", true);
        desc.setText(mDisplayable.getName());
        desc.setFont(new java.awt.Font("DejaVu Sans", Font.PLAIN, 12));
        desc.setMinimumSize(new java.awt.Dimension(10, 15));
        add(desc);

		
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() > 1){
					MainPanel.getInstance().editSelectedDisplayable(MonthItem.this);
				} else {
					MainPanel.getInstance().updateSelectedDisplayable(MonthItem.this);
				}
				
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * Generate small version of the time
	 * @param when
	 * @return
	 */
	public String simpleTime(DateTime when)
	{
		String ret;
		boolean pm = when.getHourOfDay() >= 12;
		if (when.getHourOfDay() == 0)
			ret = "12";
		else if (when.getHourOfDay() > 12)
			ret = Integer.toString(when.getHourOfDay() - 12);
		else
			ret = Integer.toString(when.getHourOfDay());

		if (when.getMinuteOfHour() != 0)
		{
			ret += ":";
			if (when.getMinuteOfHour() < 10)
				ret += "0";
			ret += Integer.toString(when.getMinuteOfHour());
		}
		
		if (pm)
			ret += "p";

		return ret;
	}


	public static Component generateFrom(Displayable elt) {
		return new MonthItem(elt);
	}
	
	/**
	 * Get current displayable
	 * @return the selected displayable
	 */
	public Displayable getDisplayable(){
		return this.mDisplayable;
	}
}
