/*******************************************************************************
 * Copyright (c) 2013 -- WPI Suite
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Team YOCO
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;

public class CategoryManager extends JPanel {
	
	private int tabid;
	private JPanel leftCategoryList;
	private JPanel rightCategoryEdit;
	private JTextField categoryName;
	private JPanel colorPicker;
	private JButton updateList;
	private JCheckBox deleteCategory;
	private JScrollPane categoryList;
	private JPanel bottomEditPanel;
	
	public CategoryManager() {
		this.setLayout(new BorderLayout());
		leftCategoryList = new JPanel();
		leftCategoryList.setLayout(new BorderLayout());
		rightCategoryEdit = new JPanel();
		rightCategoryEdit.setLayout(new BorderLayout());
		
		categoryName = new JTextField();
		rightCategoryEdit.add(categoryName, BorderLayout.NORTH);
		
		colorPicker = new JPanel();
		rightCategoryEdit.add(colorPicker, BorderLayout.CENTER);
		
		bottomEditPanel = new JPanel();
		updateList = new JButton("Update List");
		bottomEditPanel.add(updateList, BorderLayout.WEST);
		deleteCategory = new JCheckBox("Delete selected");
		bottomEditPanel.add(deleteCategory, BorderLayout.EAST);
		
		rightCategoryEdit.add(bottomEditPanel, BorderLayout.SOUTH);
		
		categoryList = new JScrollPane();
		categoryList.setBorder(null);
		
		leftCategoryList.add(categoryList, BorderLayout.NORTH);
		
		this.add(leftCategoryList, BorderLayout.WEST);
		this.add(rightCategoryEdit, BorderLayout.EAST);
	}
	
	/**
	 * Set tab id for the created event view
	 * @param id value to set id to
	 */
	public void setTabId(int id)
	{
		tabid = id;
	}

}
