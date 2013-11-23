package edu.wpi.cs.wpisuitetng.modules.cal.formulae;

import java.awt.Color;

import javax.swing.UIManager;

public class Colors
{
	public static final Color TABLE_BACKGROUND = UIManager.getDefaults().getColor("Table.background"),
			SELECTED_BACKGROUND = UIManager.getDefaults().getColor("textHighlight"),
			SELECTED_TEXT = UIManager.getDefaults().getColor("textHighlightText"),
			BORDER = UIManager.getDefaults().getColor("Separator.foreground"),
			TABLE_GRAY_TEXT = UIManager.getDefaults().getColor("Table.focusCellForeground"),
			TABLE_TEXT = UIManager.getDefaults().getColor("Label.foreground");
	
	public static final Color TABLE_GRAY_HEADER = getTableGrayHeader();
	
	
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
