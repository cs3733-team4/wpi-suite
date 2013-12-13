/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.ui.tabs;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CategoryModel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.PastelColorPicker;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.RequestFocusListener;

public class CategoryManager extends JPanel {
	
	private int tabid;
	private JPanel leftCategoryList;
	private JPanel rightCategoryEdit;
	private JTextField categoryName;
	private JPanel categoryNamePanel;
	private JLabel categoryNameLabel;
	private JLabel categoryNameErrorLabel;
	private PastelColorPicker colorPicker;
	private JButton saveCategoryButton;
	private JCheckBox deleteCategory;
	private DefaultListModel<Category> JListModel;
	private JList<Category> categoriesList;
	private JPanel bottomEditPanel;
	private List<Category> allCategories;
	private boolean editCategory = false;
	private UUID selectedCategoryUUID;
	private Category selectedCategory;
	//TODO Note: When clicking off of a category on list, selectedCategory must be set to null
	// to avoid deleting unwanted categories
	
	public CategoryManager() {
		allCategories = CategoryModel.getInstance().getAllCategories();
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		leftCategoryList = new JPanel();
		leftCategoryList.setLayout(new GridLayout(1,1));
		leftCategoryList.setPreferredSize(new Dimension(350, 900));
		leftCategoryList.setMinimumSize(new Dimension(350, 900));
		leftCategoryList.setMaximumSize(new Dimension(350, 900));
		leftCategoryList.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 0));
		
		rightCategoryEdit = new JPanel();
		rightCategoryEdit.setLayout(new BoxLayout(rightCategoryEdit, BoxLayout.Y_AXIS));
		rightCategoryEdit.setBorder(new EmptyBorder(6, 6, 6, 6));
		
		/** Name Panel */
		
		// Panel
		categoryNamePanel = new JPanel();
		categoryNamePanel.setLayout(new BoxLayout(categoryNamePanel, BoxLayout.X_AXIS));
		categoryNamePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Label
		categoryNameLabel = new JLabel("Name: ");
		categoryNamePanel.add(categoryNameLabel);
		categoryNameErrorLabel = new JLabel();
		
		// Text Field
		categoryName = new JTextField();
		categoryName.setColumns(25);
		categoryName.setPreferredSize(new Dimension(200, 30));
		categoryName.setMaximumSize(new Dimension(200, 30));
		categoryName.addAncestorListener(new RequestFocusListener());
		
		categoryName.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				categoryNameErrorLabel.setVisible(!validateText(categoryName.getText(), categoryNameErrorLabel));
				saveCategoryButton.setEnabled(isSaveable());
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				categoryNameErrorLabel.setVisible(!validateText(categoryName.getText(), categoryNameErrorLabel));
				saveCategoryButton.setEnabled(isSaveable());
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				//Not triggered by plaintext fields
			}
		});
		
		categoryNameErrorLabel.setForeground(Color.RED);
		validateText(categoryName.getText(), categoryNameErrorLabel);

		categoryNamePanel.add(categoryName);
		categoryNamePanel.add(categoryNameErrorLabel);
		
		// Add to UI
		rightCategoryEdit.add(categoryNamePanel);
		
		/** Color Picker */
		
		// Panel
		colorPicker = new PastelColorPicker();
		colorPicker.setPreferredSize(new Dimension(450, 53));
		colorPicker.setMaximumSize(new Dimension(450, 53));
		colorPicker.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Add to UI
		rightCategoryEdit.add(Box.createRigidArea(new Dimension (0, 6)));
		rightCategoryEdit.add(colorPicker);
		
		/** Buttons */
		
		// Panel
		bottomEditPanel = new JPanel();
		FlowLayout fl_DescriptionLabelPane = (FlowLayout) bottomEditPanel.getLayout();
		fl_DescriptionLabelPane.setAlignment(FlowLayout.LEFT);
		bottomEditPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Buttons
		saveCategoryButton = new JButton("Save");
		deleteCategory = new JCheckBox("Delete selected");
		deleteCategory.setSelected(false);
		
		// Add to Panel
		bottomEditPanel.add(saveCategoryButton);
		//bottomEditPanel.add(deleteCategory);
		bottomEditPanel.add(Box.createHorizontalGlue());
		
		// Add to UI
		rightCategoryEdit.add(bottomEditPanel);
		
		
		/** List model for the category display */
		
		JListModel = new DefaultListModel<Category>();
		categoriesList = new JList<Category>(JListModel);
		
		/* Add click listener
		categoriesList.addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		    	
		    	// Get index of selected cell
		        JList list = (JList)evt.getSource();
		        int index = list.locationToIndex(evt.getPoint());
		    	
		        // Get category object
		    	categoriesList.setSelectedIndex(index);
		    	selectedCategory = (Category) categoriesList.getSelectedValue();
		    	
		    	// Display data from selected category object
		    	selectedCategoryUUID = selectedCategory.getUUID();
		    	categoryName.setText(selectedCategory.getName());
		    	
		    }
		}); */
		
		// Set up cell renderer
		categoriesList.setCellRenderer(new ListCellRenderer<Category>() {

			@Override
			public Component getListCellRendererComponent(
					JList<? extends Category> list, Category value, int index,
					boolean isSelected, boolean cellHasFocus) {
				JPanel bg = new JPanel();
				bg.setLayout(new BoxLayout(bg, BoxLayout.X_AXIS));
				JPanel colorBlast = new JPanel();
				colorBlast.setPreferredSize(new Dimension(12, 12));
				colorBlast.setMaximumSize(new Dimension(12, 12));
				colorBlast.setBackground(value.getColor());
				colorBlast.setBorder(BorderFactory.createLineBorder(Color.black, 1));
				bg.add(colorBlast);
				bg.add(Box.createHorizontalStrut(3));
				
				JLabel display = new JLabel();
				display.putClientProperty("html.disable", true);
				display.setText(value.getName());
				bg.add(display);
				bg.add(Box.createHorizontalGlue());
				bg.setOpaque(true);
				bg.setBackground(Colors.TABLE_BACKGROUND);
				
				if (isSelected)
					bg.setBackground(Colors.SELECTED_BACKGROUND);
				
				return bg;
			}
		});
		
		if (allCategories.size() == 0){
			JListModel.addElement(Category.DEFAULT_DISPLAY_CATEGORY);
		} else {
			if (JListModel.contains(Category.DEFAULT_DISPLAY_CATEGORY))
				JListModel.removeElement(Category.DEFAULT_DISPLAY_CATEGORY);
			
			for (int i = 0; i < allCategories.size(); i++) {
				Category temp = allCategories.get(i);
				JListModel.addElement(temp);
			}
		}
		
		JScrollPane categoriesListScrollPane = new JScrollPane(categoriesList);
		categoriesListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		categoriesListScrollPane.setBorder(BorderFactory.createLineBorder(Colors.BORDER));
		leftCategoryList.add(categoriesListScrollPane);
		
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
			mErrorLabel.setText(" * Required Field");
			return false;
		}
		
		for (Category cat : allCategories){
			if (cat.getName().equals(categoryName.getText())){
				mErrorLabel.setText("* Category name already exists");
				return false;
			}
		}
		
		return true;
	}

	/**
	 * checks if all validation tests pass
	 * @return true if all pass, else return false
	 */
	public boolean isSaveable()
	{
		return validateText(categoryName.getText(), categoryNameErrorLabel);
	}

	/**
	 * Set tab id for the edit category view
	 * @param id value to set id to
	 */
	public void setTabId(int id)
	{
		tabid = id;
	}
	
	public void attemptSave()
	{
		if (!isSaveable())
			return;
		Category c = new Category();
		c.setName(categoryName.getText().trim());
		c.setColor(colorPicker.getCurrentColorState()); // Get color from color picker

		if (editCategory){
			c.setCategoryID(selectedCategoryUUID);
			MainPanel.getInstance().updateCategory(c);
			JListModel.removeElement(selectedCategory);
			JListModel.addElement(c);
		} else {
			MainPanel.getInstance().addCategory(c);
			if (JListModel.contains(Category.DEFAULT_DISPLAY_CATEGORY))
				JListModel.removeElement(Category.DEFAULT_DISPLAY_CATEGORY);
			JListModel.addElement(c);
		}
		
		MainPanel.getInstance().refreshCategoryFilterTab(); // Update created/edited category filters
		
		categoryName.setText(""); // Clear category name text field upon addition
		selectedCategory = null; // Clear selection
		
		saveCategoryButton.setEnabled(false);
	}
	public void focusOnName()
	{
		categoryName.requestFocus();
	}
	/**
	 * Set up button listeners for the category manager
	 */
	private void setUpListeners(){
		
		// Update List Button
		saveCategoryButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				attemptSave();
			}
		});
		
		//this should be called in updateSaveable() and thus isnt necessary here
		//but error msg didn't start visible unless I called it directly
		
		saveCategoryButton.setEnabled(isSaveable());
		

	}

}
