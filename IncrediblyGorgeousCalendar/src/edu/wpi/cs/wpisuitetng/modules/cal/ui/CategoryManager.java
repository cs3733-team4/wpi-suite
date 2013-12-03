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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CategoryModel;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.PastelColorPicker;

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
	private JPanel categoryNamePanel;
	private JLabel categoryNameLabel;
	private JLabel categoryErrorLabel;
	private PastelColorPicker colorPicker;
	private JButton updateList;
	private JCheckBox deleteCategory;
	private DefaultListModel<Category> JListModel;
	private JList<Category> categoriesList;
	private JPanel bottomEditPanel;
	private List<Category> allCategories;
	private CategoryModel categories;
	private JLabel errorText;
	protected boolean editCategory;
	
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
		rightCategoryEdit.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		/** Name Panel */
		
		// Panel
		categoryNamePanel = new JPanel();
		categoryNamePanel.setLayout(new BoxLayout(categoryNamePanel, BoxLayout.X_AXIS));
		
		// Label
		categoryNameLabel = new JLabel("Name: ");
		categoryNamePanel.add(categoryNameLabel);
		categoryErrorLabel = new JLabel();
		
		// Text Field
		categoryName = new JTextField();
		categoryName.setColumns(25);
		categoryName.setPreferredSize(new Dimension(300, 20));
		categoryName.setMaximumSize(new Dimension(300, 20));
		
		categoryName.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				categoryErrorLabel.setVisible(!validateText(categoryName.getText(), categoryErrorLabel));
				updateList.setEnabled(isSaveable());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				categoryErrorLabel.setVisible(!validateText(categoryName.getText(), categoryErrorLabel));
				updateList.setEnabled(isSaveable());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				//Not triggered by plaintext fields
			}
		});
		
		categoryErrorLabel.setForeground(Color.RED);
		validateText(categoryName.getText(), categoryErrorLabel);

		categoryNamePanel.add(categoryName);
		categoryNamePanel.add(categoryErrorLabel);
		
		// Add to UI
		rightCategoryEdit.add(categoryNamePanel);
		
		/** Color Picker */
		
		// Panel
		colorPicker = new PastelColorPicker();
		colorPicker.setPreferredSize(new Dimension(360, 50));
		
		// Add to UI
		rightCategoryEdit.add(colorPicker);
		
		/** Buttons */
		
		// Panel
		bottomEditPanel = new JPanel();
		
		// Buttons
		updateList = new JButton("Update List");
		deleteCategory = new JCheckBox("Delete selected");
		deleteCategory.setSelected(false);
		
		// Error label
		this.errorText = new JLabel();
		errorText.setForeground(Color.RED);

		// Add to Panel
		bottomEditPanel.add(updateList);
		bottomEditPanel.add(deleteCategory);
		bottomEditPanel.add(errorText);
		
		// Add to UI
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
		
		setUpListeners();
	}
	
	/**
	 * 
	 * @param mText text to be validated
	 * @param mErrorLabel JLabel to display resulting error message
	 * @return true if all pass, else return true
	 */
	private boolean validateText(String mText, JLabel mErrorLabel)
	{
		if(mText==null || mText.trim().length()==0)
		{
			mErrorLabel.setText("* Required Field");
			return false;
		/*will be handled when parsed
		}else if(mText.matches("^.*[^a-zA-Z0-9.,()$ ].*$"))
		{
			
			mErrorLabel.setText("* Invalid Name/Characters");
		*/
		}
		return true;
	}

	/**
	 * checks if all validation tests pass
	 * @return true if all pass, else return false
	 */
	public boolean isSaveable()
	{
		return validateText(categoryName.getText(), categoryErrorLabel);
	}

	/**
	 * Set tab id for the edit category view
	 * @param id value to set id to
	 */
	public void setTabId(int id)
	{
		tabid = id;
	}
	
	private void setUpListeners(){
		
		// Update List Button
		
		updateList.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try
				{
					errorText.setVisible(true);
					
					if (categoryName.getText() == null || categoryName.getText().trim().length() == 0)
					{
						errorText.setText("* Please enter a category title");
					}
					else
					{
						errorText.setVisible(false);
						Category c = new Category();
						c.setName(categoryName.getText().trim());
						//c.setColor(colorPicker.); // Get color from color picker

						System.out.println("I made it here! Woo!");
						
						categories.putCategory(c);
						
						/* Need to figure out how to distinguish between editing an event or adding
						if (editCategory){
							e.setEventID(existingEventID);
							MainPanel.getInstance().updateEvent(e);
						} else {
							MainPanel.getInstance().addEvent(e);
						}
						*/
						
						updateList.setEnabled(false);
						updateList.setText("Saved!");
						MainPanel.getInstance().closeTab(tabid);
						MainPanel.getInstance().refreshView();
					}
				}
				catch (IllegalArgumentException exception)
				{
					errorText.setText("* Invalid Date/Time");
					errorText.setVisible(true);
				}
			}
		});
		
		//this should be called in updateSaveable() and thus isnt necessary here
		//but error msg didn't start visible unless I called it directly
		
		updateList.setEnabled(isSaveable());
	}

}
