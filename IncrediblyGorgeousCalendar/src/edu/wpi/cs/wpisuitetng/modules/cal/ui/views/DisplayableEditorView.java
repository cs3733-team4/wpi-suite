/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.Box.Filler;

import net.miginfocom.swing.MigLayout;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CategoryModel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.DatePicker;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.RequestFocusListener;

/**
 * The UI for AddEvent
 */
public class DisplayableEditorView extends JPanel
{
	protected JTextField nameTextField, participantsTextField;
	protected final ButtonGroup buttonGroup = new ButtonGroup();
	protected JLabel nameLabel, nameErrorLabel, dateAndTimeLabel, lblUntil, dateErrorLabel, participantsLabel,
	lblCategory, lblCalendar, descriptionLabel;
	protected JRadioButton rdbtnTeam, rdbtnPersonal;
	protected JTextArea descriptionTextArea;
	protected DatePicker startTimeDatePicker, endTimeDatePicker;
	protected JComboBox<Category> eventCategoryPicker;
	protected JButton cancelButton, saveButton;

	public DisplayableEditorView(boolean showEnd)
	{
		nameTextField = new JTextField();
		nameTextField.setColumns(30);
		nameTextField.addAncestorListener(new RequestFocusListener());

		nameLabel = new JLabel("Name:");
		this.setLayout(new MigLayout("", "[45px][334px,grow]",
				"[sizegroup 1line][sizegroup 1line][sizegroup 1line][sizegroup 1line][sizegroup 1line][50px:n,grow][grow][]"));
		this.add(nameLabel, "cell 0 0,alignx right,growy");
		this.add(nameTextField, "cell 1 0,alignx left,aligny baseline");

		nameErrorLabel = new JLabel("Please name this event");
		nameErrorLabel.setForeground(Color.RED);
		nameErrorLabel.setVisible(false);
		this.add(nameErrorLabel, "cell 1 0");

		dateAndTimeLabel = new JLabel("When:");
		this.add(dateAndTimeLabel, "cell 0 1,alignx right,aligny baseline");

		endTimeDatePicker = new DatePicker(true, null);
		startTimeDatePicker = new DatePicker(true, showEnd ? endTimeDatePicker : null);
		this.add(startTimeDatePicker, "flowx,cell 1 1,alignx left,growy");
		
		if (showEnd)
		{
			lblUntil = new JLabel("until");
			this.add(lblUntil, "flowx,cell 1 1,alignx left,growy");
	
			this.add(endTimeDatePicker, "flowx,cell 1 1,alignx left,growy");
		}

		dateErrorLabel = new JLabel("Event can't start after it ends");
		dateErrorLabel.setForeground(Color.RED);
		dateErrorLabel.setVisible(false);
		this.add(dateErrorLabel, "flowx,cell 1 1,alignx left,growy");

		participantsLabel = new JLabel("Participants:");
		this.add(participantsLabel, "cell 0 2,alignx right,aligny baseline");

		participantsTextField = new JTextField();
		this.add(participantsTextField, "cell 1 2,alignx left,aligny baseline");
		participantsTextField.setColumns(40);

		lblCategory = new JLabel("Category:");
		this.add(lblCategory, "cell 0 3,alignx right,aligny baseline");

		eventCategoryPicker = new JComboBox<>();
		eventCategoryPicker.setRenderer(new CategoryComboBoxRenderer());
		this.eventCategoryPicker.addItem(Category.DEFUALT_CATEGORY);
		for(Category c : CategoryModel.getInstance().getAllCategories())
		{
			this.eventCategoryPicker.addItem(c);
		}

		this.add(eventCategoryPicker, "cell 1 3,alignx left,aligny baseline");

		lblCalendar = new JLabel("Calendar:");
		this.add(lblCalendar, "cell 0 4,alignx right,aligny baseline");

		rdbtnPersonal = new JRadioButton("Personal");
		buttonGroup.add(rdbtnPersonal);
		this.add(rdbtnPersonal, "flowx,cell 1 4,alignx left,growy");

		rdbtnTeam = new JRadioButton("Team");
		rdbtnTeam.setSelected(true);
		buttonGroup.add(rdbtnTeam);
		this.add(rdbtnTeam, "cell 1 4");

		descriptionLabel = new JLabel("Description:");
		this.add(descriptionLabel, "cell 0 5,alignx right,aligny top");

		descriptionTextArea = new JTextArea();
		descriptionTextArea.setBorder(nameTextField.getBorder());
		this.add(descriptionTextArea, "cell 1 5,grow");

		cancelButton = new JButton("Cancel");
		cancelButton.setMinimumSize(new Dimension(80, 0));
		this.add(cancelButton, "flowx,cell 1 7,alignx right,aligny bottom,tag cancel");

		saveButton = new JButton("Save");
		saveButton.setMinimumSize(new Dimension(80, 0));
		this.add(saveButton, "cell 1 7,alignx right,aligny bottom,tag ok");
	}

	private class CategoryComboBoxRenderer implements ListCellRenderer<Category>
	{
		@Override
		public Component getListCellRendererComponent(JList<? extends Category> list, Category value, int index, boolean isSelected, boolean cellHasFocus)
		{
			JPanel jPanel1 = new javax.swing.JPanel();
			JPanel jPanel2 = new javax.swing.JPanel();
			Filler filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(6, 0), new java.awt.Dimension(6, 0), new java.awt.Dimension(6, 0));
			JLabel jLabel1 = new javax.swing.JLabel();
			jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 6, 3, 0));
			jPanel1.setAlignmentX(0.0F);
			jPanel1.setAlignmentY(0.0F);
			jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.X_AXIS));

			jPanel2.setBackground(value.getColor());
			jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(Colors.BORDER));
			jPanel2.setMaximumSize(new java.awt.Dimension(15, 15));
			jPanel2.setMinimumSize(new java.awt.Dimension(15, 15));
			jPanel2.setName(""); // NOI18N
			jPanel2.setPreferredSize(new java.awt.Dimension(15, 15));

			jPanel1.add(jPanel2);
			jPanel1.add(filler1);
			jLabel1.putClientProperty("html.disable", true);
			jLabel1.setText(value.getName());
			jLabel1.setMaximumSize(new java.awt.Dimension(32767, 15));
			jPanel1.add(jLabel1);

			jPanel1.setName("ComboBox.listRenderer");
			if (isSelected)
			{
				jPanel1.setBackground(list.getSelectionBackground());
				jPanel1.setForeground(list.getSelectionForeground());
			}
			else
			{
				jPanel1.setBackground(list.getBackground());
				jPanel1.setForeground(list.getForeground());
			}

			jLabel1.setFont(list.getFont());

			return jPanel1;
		}
	}
}