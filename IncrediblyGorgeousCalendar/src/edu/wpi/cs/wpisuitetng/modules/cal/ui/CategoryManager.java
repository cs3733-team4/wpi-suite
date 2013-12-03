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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CategoryModel;

/**
 * Victor: Left pretty, left default (no categories), left clickable (populate right fields), save/update
 * Alex: Right pretty, delete
 *
 * Add new ribbon bar group
 */
public class CategoryManager extends JPanel {
	
	private int tabid;
	private JPanel leftCategoryList;
	private JPanel rightCategoryEdit;
	private JTextField categoryName;
	private JPanel colorPicker;
	private JButton updateList;
	private JCheckBox deleteCategory;
	private DefaultListModel<Category> JListModel;
	private JList<Category> categoriesList;
	private JPanel bottomEditPanel;
	private List<Category> allCategories;
	private CategoryModel categories;
	
	public CategoryManager() {
				
		categories = new CategoryModel();
		allCategories = categories.getAllCategories();
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		leftCategoryList = new JPanel();
		leftCategoryList.setLayout(new GridLayout(1,1));
		leftCategoryList.setPreferredSize(new Dimension(350, 900));
		leftCategoryList.setMinimumSize(new Dimension(350, 900));
		rightCategoryEdit = new JPanel();
		rightCategoryEdit.setLayout(new BoxLayout(rightCategoryEdit, BoxLayout.Y_AXIS));
		
		categoryName = new JTextField();
		categoryName.setPreferredSize(new Dimension(Integer.MAX_VALUE, 100));
		rightCategoryEdit.add(categoryName);
		
		colorPicker = new JPanel();
		rightCategoryEdit.add(colorPicker);
		
		bottomEditPanel = new JPanel();
		updateList = new JButton("Update List");
		bottomEditPanel.add(updateList);
		deleteCategory = new JCheckBox("Delete selected");
		bottomEditPanel.add(deleteCategory);
		rightCategoryEdit.add(bottomEditPanel);
		
		JListModel = new DefaultListModel<Category>();
		
		categoriesList = new JList<Category>(JListModel);
		categoriesList.setCellRenderer(new ListCellRenderer<Category>() {

			@Override
			public Component getListCellRendererComponent(
					JList<? extends Category> list, Category value, int index,
					boolean isSelected, boolean cellHasFocus) {
				return new JLabel(value.getName());
			}
		});
		
		if (allCategories.size() == 0){
			System.out.println("A&V : Called");
			Category test = new Category();
			test.setName("whee");
			JListModel.addElement(test);
			test = new Category();
			test.setName("wyayayae");
			JListModel.addElement(test);
		} else {
			//JListModel.removeElement(noCategoryLabel);
			for (int i = 0; i < allCategories.size(); i++) {
				Category temp = allCategories.get(i);
				JListModel.addElement(temp);
			}
		}
		
		leftCategoryList.add(categoriesList);
		
		this.add(leftCategoryList);
		this.add(rightCategoryEdit);
	}
	
	/**
	 * Set tab id for the edit category view
	 * @param id value to set id to
	 */
	public void setTabId(int id)
	{
		tabid = id;
	}

}
