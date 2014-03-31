package edu.wpi.cs.wpisuitetng.modules.cal.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

public class RepeatPicker extends JFrame
{
	
	JPanel mainGrid = new JPanel();
	
	JLabel RepeatsLabel = new JLabel("<b>repeats:</b>");
	JLabel RepeatsEveryLabel = new JLabel("repeats every:");
	JLabel StartsOnLabel = new JLabel("starts on:");
	JLabel EndsLabel = new JLabel("ends:");
	JLabel SummaryLabel = new JLabel("summary:");
	
	public RepeatPicker()
	{
		this.setPreferredSize(new Dimension(320,450));
		this.setResizable(false);
		this.add(mainGrid, BorderLayout.CENTER);
		mainGrid.setLayout(new MigLayout("", "[45px][334px,grow]",
				"[sizegroup 1line][sizegroup 1line][sizegroup 1line][sizegroup 1line][sizegroup 1line][sizegroup 1line][sizegroup 1line][sizegroup 1line][30px:n,grow][grow][25px]"));
		
		mainGrid.add(RepeatsLabel, "cell 0 1,alignx right, aligny baseline");
		mainGrid.add(RepeatsEveryLabel, "cell 0 2,alignx right, aligny baseline");
		mainGrid.add(StartsOnLabel, "cell 0 4,alignx right, aligny baseline");
		mainGrid.add(EndsLabel, "cell 0 5,alignx right, aligny baseline");
		mainGrid.add(SummaryLabel, "cell 0 6,alignx right, aligny baseline");
	
	}
	
	
	public static void main(String[] args)
	{
		RepeatPicker p = new RepeatPicker();
		p.pack();
		p.setVisible(true);
	}
	
	
}
