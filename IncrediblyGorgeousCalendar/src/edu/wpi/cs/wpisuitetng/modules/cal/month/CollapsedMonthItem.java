package edu.wpi.cs.wpisuitetng.modules.cal.month;

import java.awt.Font;

import javax.swing.JLabel;

import edu.wpi.cs.wpisuitetng.modules.cal.formulae.Colors;

public class CollapsedMonthItem extends JLabel
{
	private static final long serialVersionUID = 7964804454006900134L;

	public CollapsedMonthItem(int more)
	{
        setBackground(Colors.TABLE_BACKGROUND);
        setMaximumSize(new java.awt.Dimension(32767, 24));

        setFont(new java.awt.Font("DejaVu Sans", Font.BOLD, 12));

        setText(Integer.toString(more) + " more items...");
        setMinimumSize(new java.awt.Dimension(10, 15));
	}
}
