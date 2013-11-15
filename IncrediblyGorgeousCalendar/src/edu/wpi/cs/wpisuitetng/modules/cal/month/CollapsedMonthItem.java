package edu.wpi.cs.wpisuitetng.modules.cal.month;

import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.UIManager;

public class CollapsedMonthItem extends JLabel
{
	public CollapsedMonthItem(int more)
	{
        setBackground(UIManager.getDefaults().getColor("Table.background"));
        setMaximumSize(new java.awt.Dimension(32767, 24));

        setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 12));

        setText(Integer.toString(more) + " more items...");
        setMinimumSize(new java.awt.Dimension(10, 15));
	}
}
