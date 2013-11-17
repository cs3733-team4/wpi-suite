/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.cs.wpisuitetng.modules.cal.month;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;


public class MonthItem extends JPanel
{
	private static final long serialVersionUID = 6475224766889058195L;
	
	JLabel time = new JLabel(), desc = new JLabel();
	private DateTime when;
	/**
	 * MonthItem Constructor
	 * @param when
	 * @param descr
	 */
	public MonthItem(DateTime when, String descr)
	{
        setBackground(Colors.TABLE_BACKGROUND);
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

	public static Component generateFrom(Event elt) {
		return new MonthItem(elt.getStartTime(), elt.getName());
	}
}
