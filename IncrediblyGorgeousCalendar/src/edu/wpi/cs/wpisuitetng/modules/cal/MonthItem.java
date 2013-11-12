/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.cs.wpisuitetng.modules.cal;

import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import org.joda.time.DateTime;

/**
 *
 * @author patrick
 */
public class MonthItem extends JPanel
{
	JLabel time = new JLabel(), desc = new JLabel();
	private DateTime when;
	public MonthItem(DateTime when, String descr)
	{
        setBackground(UIManager.getDefaults().getColor("Table.background"));
        setMaximumSize(new java.awt.Dimension(32767, 24));
        setMinimumSize(new java.awt.Dimension(0, 0));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.X_AXIS));

        time.setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 12));
        time.setText(simpleTime(when));
        time.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 3));
        add(time);

        desc.setText(descr);
        desc.setFont(new java.awt.Font("DejaVu Sans", Font.PLAIN, 12));
        desc.setMinimumSize(new java.awt.Dimension(10, 15));
        add(desc);

		setWhen(when);
	}

	/**
	 * Generate small version of the time
	 * @param when
	 * @return
	 */
	String simpleTime(DateTime when)
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
			ret += ":" + Integer.toString(when.getMinuteOfHour());

		if (pm)
			ret += "p";

		return ret;
	}

	public DateTime getWhen()
	{
		return when;
	}

	public void setWhen(DateTime when)
	{
		this.when = when;
	}
}
