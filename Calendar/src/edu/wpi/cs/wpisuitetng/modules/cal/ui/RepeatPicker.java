/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.wpi.cs.wpisuitetng.modules.cal.utils.Range;
import net.miginfocom.swing.MigLayout;

public class RepeatPicker extends JFrame
{
	
	private JPanel mainGrid = new JPanel();
	
	private JLabel repeatsLabel = new JLabel("repeats:");
	private JLabel repeatsEveryLabel = new JLabel("repeats every:");
	private JLabel startsOnLabel = new JLabel("starts on:");
	private JLabel endsLabel = new JLabel("ends:");
	private JLabel summaryLabel = new JLabel("summary:");
	private JLabel repeatOnLabel = new JLabel("repeat on:");
	private JLabel summaryValueLabel = new JLabel("Daily");
	private JLabel repeatsEveryDescrLabel = new JLabel(RepetitionType.Daily.getSingularText());
	
	
	private DefaultComboBoxModel<Integer> repeatEverySelectorModel = new DefaultComboBoxModel<Integer>(Range.Irange(1, RepetitionType.Daily.repeatEvery()+1));
	private JComboBox<RepetitionType> repeatTypeSelector = new JComboBox<RepetitionType>(RepetitionType.values());
	private JComboBox<Integer> repeatEverySelector = new JComboBox<Integer>(repeatEverySelectorModel);
	
	
	private Font defaultFont = new Font("DejaVu Sans", Font.PLAIN, 12);
	private RepetitionType selected = RepetitionType.Daily;
	private Integer repeatEvery = 1;
	
	private StringBuilder summaryBuilder = new StringBuilder();
	private String currentOnRepeatText = " on weekends";
	
	
	public RepeatPicker()
	{
		this.setPreferredSize(new Dimension(320,320));
		//this.setResizable(false);
		this.add(mainGrid, BorderLayout.CENTER);
		mainGrid.setLayout(new MigLayout("", "[45px][334px,grow]",
				"[sizegroup 1line][sizegroup 1line][sizegroup 1line][sizegroup 1line][sizegroup 1line][40px:n,grow][grow][25px]"));
		
		mainGrid.add(repeatsLabel, "cell 0 0,alignx right, aligny baseline");
		mainGrid.add(repeatsEveryLabel, "cell 0 1,alignx right, aligny baseline");
		mainGrid.add(startsOnLabel, "cell 0 3,alignx right, aligny baseline");
		mainGrid.add(endsLabel, "cell 0 4,alignx right, aligny baseline");
		mainGrid.add(summaryLabel, "cell 0 7,alignx right, aligny baseline");
		mainGrid.add(summaryValueLabel, "cell 1 7 2 7,alignx left, aligny baseline");
		
		
		mainGrid.add(repeatTypeSelector, "cell 1 0 2 0,grow,aligny baseline");
		mainGrid.add(repeatEverySelector, "cell 1 1,grow,aligny baseline");
		mainGrid.add(repeatsEveryDescrLabel, "cell 2 1,alignx left,aligny baseline");
		
		
		
		
		repeatsLabel.setFont(defaultFont);
		repeatsLabel.setFont(defaultFont);
		repeatsEveryLabel.setFont(defaultFont);
		startsOnLabel.setFont(defaultFont);
		endsLabel.setFont(defaultFont);
		summaryLabel.setFont(defaultFont);
		repeatsEveryDescrLabel.setFont(defaultFont);
		repeatOnLabel.setFont(defaultFont);
		//summaryValueLabel.setFont(defaultFont);
		
		
		
		repeatTypeSelector.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent event)
			{
				if (event.getStateChange() == ItemEvent.SELECTED)
				{
					selected = (RepetitionType)event.getItem();
					repeatEverySelectorModel.removeAllElements();
					repeatsEveryDescrLabel.setText(selected.getSingularText());
					for(int i = 1; i <= selected.repeatEvery(); i++)
					{
						repeatEverySelectorModel.addElement(i);
					}
					if (selected == RepetitionType.Weekly)
					{
						mainGrid.add(repeatOnLabel, "cell 0 2,alignx right, aligny baseline");
					}
					else
					{
						mainGrid.remove(repeatOnLabel);
					}
					repeatEvery = 1;
					
					updateSummaryText();
				}
			}
		});
		
		repeatEverySelector.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent event)
			{
				if (event.getStateChange() == ItemEvent.SELECTED)
				{
					repeatEvery = (Integer)event.getItem();
					if (repeatEvery == 1)
					{
						repeatsEveryDescrLabel.setText(selected.getSingularText());
					}
					else
					{
						repeatsEveryDescrLabel.setText(selected.getPluralText());
					}
					
					updateSummaryText();
				}
			}
		});
		
		
	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void updateSummaryText()
	{
		summaryBuilder = new StringBuilder();
		
		if (repeatEvery == 1)
		{
			repeatsEveryDescrLabel.setText(selected.getSingularText());
			summaryBuilder.append(selected.name());
		}
		else
		{
			repeatsEveryDescrLabel.setText(selected.getPluralText());
			summaryBuilder.append("Every ")
						  .append(repeatEvery)
						  .append(" ")
						  .append(selected.getPluralText());
		}
		
		
		summaryBuilder.append(currentOnRepeatText);
		summaryValueLabel.setText(summaryBuilder.toString());
		
		mainGrid.repaint();
	}
	
	
	public static void main(String[] args)
	{
		RepeatPicker p = new RepeatPicker();
		p.pack();
		p.setVisible(true);
	}
	
	
	
	
	
	
	
	
	private enum RepetitionType
	{
		Daily("Day", "Days", false, 365), Weekly("Week", "Weeks", true, 54), Monthly("Month", "Months", false, 12);
		
		
		private String singluar;
		private String plural;
		private boolean hasExtraSelection;
		private int repeatEvery;
		private RepetitionType(String singluar, String plural, boolean hasExtraSelection, int repeatEvery)
		{
			this.singluar = singluar;
			this.plural = plural;
			this.hasExtraSelection = hasExtraSelection;
			this.repeatEvery = repeatEvery;
		}
		
		public int repeatEvery()
		{
			return this.repeatEvery;
		}
		
		public String getPluralText()
		{
			return this.plural;
		}
		
		public String getSingularText()
		{
			return this.singluar;
		}
		
		public boolean hasExtraSelection()
		{
			return this.hasExtraSelection;
		}
	}
}
