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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CategoryModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.Colors;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.HSLColor;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.PastelColorPicker;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.RequestFocusListener;

public class CategoryManager extends JPanel {
	
	private int tabid;
	private JPanel leftCategoryList;
	private JPanel rightCategoryEdit;
	private JTextField categoryName;
	private JPanel categoryNamePanel;
	private JPanel categoryColorPanel;
	private JLabel categoryNameLabel;
	private JLabel categoryColorLabel;
	private JLabel categoryNameErrorLabel;
	private JLabel selectionChangeErrorLabel;
	private PastelColorPicker colorPicker;
	private JButton saveCategoryButton;
	private JButton deleteCategoryButton;
	private JButton cancelEditButton;
	private DefaultListModel<Category> JListModel;
	private JList<Category> categoriesList;
	private JPanel bottomEditPanel;
	private List<Category> allCategories;
	private Category selectedCategory;
	private boolean clearSelected;
	private boolean isEditing = false;
	private boolean firstEdit = false;
	
	// TODO LIST 
	// DONE -- When selecting category from list, repeated name alert shouldn't pop up
	// DONE -- First click on category doesn't allow saving
	// TODO NAME IS OK, BUT HAVE TO MAKE COLOR SELECTOR POSITION MORE ACCURATE -- Constraint moving if name or color changed on editing category
	// TODO CHOOSE BETTER LOCATION FOR ERROR MESSAGE -- Display error message if trying to select different category while in edit mode
	// DONE -- Properly refresh category list upon change
	// DONE -- Disable arrow key navigation in list
	// TODO NEED TO TEST -- Avoid deleting no category
	// DONE -- Format UI to be consistent with other panels in pROJECT
	// DONE -- Populate categories may not be getting updated list since database is not being called
	// DONE -- Update filter immediately -- DONE
	// TODO -- Clean up code and add comments
	
	/**
	 * 1 - Categories should not have repeated names. When creating a new category, save button should be disabled if name is repeated
	 * 
	 * 2 - 
	 */
	
	public CategoryManager() {
		
		// Get categories
		allCategories = CategoryModel.getInstance().getAllCategories();

		
		/** Set up UI **/
		
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		leftCategoryList = new JPanel();
		leftCategoryList.setLayout(new BoxLayout(leftCategoryList,BoxLayout.Y_AXIS));
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
		selectionChangeErrorLabel = new JLabel("");
		selectionChangeErrorLabel.setLayout(new BorderLayout());
		selectionChangeErrorLabel.setPreferredSize(new Dimension(350, 20));
		selectionChangeErrorLabel.setMinimumSize(new Dimension(350, 20));
		selectionChangeErrorLabel.setMaximumSize(new Dimension(350, 20));
		
		// Text Field
		categoryName = new JTextField();
		categoryName.setColumns(25);
		categoryName.setPreferredSize(new Dimension(200, 30));
		categoryName.setMaximumSize(new Dimension(200, 30));
		categoryName.addAncestorListener(new RequestFocusListener());
		
		selectionChangeErrorLabel.setForeground(Color.RED);
		categoryNameErrorLabel.setForeground(Color.RED);

		categoryNamePanel.add(categoryName);
		categoryNamePanel.add(categoryNameErrorLabel);
		
		// Add to UI
		rightCategoryEdit.add(categoryNamePanel);
		rightCategoryEdit.add(Box.createVerticalStrut(10));
		
		/** Color Picker */
		
		// Panel
		categoryColorPanel = new JPanel();
		categoryColorPanel.setLayout(new BoxLayout(categoryColorPanel, BoxLayout.X_AXIS));
		categoryColorPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Label
		categoryColorLabel = new JLabel("Color:  ");
		categoryColorPanel.add(categoryColorLabel);
		
		// Picker
		colorPicker = new PastelColorPicker();
		colorPicker.setPreferredSize(new Dimension(450, 53));
		colorPicker.setMaximumSize(new Dimension(450, 53));
		colorPicker.setAlignmentX(Component.LEFT_ALIGNMENT);
		categoryColorPanel.add(Box.createRigidArea(new Dimension (0, 6)));
		categoryColorPanel.add(colorPicker);
		
		// Add to UI
		rightCategoryEdit.add(categoryColorPanel);
		rightCategoryEdit.add(Box.createVerticalStrut(10));
		
		/** Buttons */
		
		// Panel
		bottomEditPanel = new JPanel();
		FlowLayout fl_DescriptionLabelPane = (FlowLayout) bottomEditPanel.getLayout();
		fl_DescriptionLabelPane.setAlignment(FlowLayout.LEFT);
		bottomEditPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		// Buttons
		saveCategoryButton = new JButton("Save");
		deleteCategoryButton = new JButton("Delete");
		deleteCategoryButton.setVisible(false);
		cancelEditButton = new JButton("Cancel");
		cancelEditButton.setVisible(false);
		
		// Add to Panel
		bottomEditPanel.add(saveCategoryButton);
		bottomEditPanel.add(deleteCategoryButton);
		bottomEditPanel.add(cancelEditButton);
		bottomEditPanel.add(Box.createHorizontalGlue());
		
		// Add to UI
		rightCategoryEdit.add(bottomEditPanel);
		
		
		/** List model for the category display */
		
		JListModel = new DefaultListModel<Category>();
		categoriesList = new JList<Category>(JListModel);

		categoriesList.getInputMap().getParent().clear();// Disable keyboard listeners
		
		categoriesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
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
		
		JScrollPane categoriesListScrollPane = new JScrollPane(categoriesList);
		categoriesListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		categoriesListScrollPane.setBorder(BorderFactory.createLineBorder(Colors.BORDER));
		leftCategoryList.add(categoriesListScrollPane);
		leftCategoryList.add(selectionChangeErrorLabel);
		
		this.add(leftCategoryList);
		this.add(rightCategoryEdit);
		
		// Set up listeners and add data to panel
		
		setUpListeners();
		populateCategories(JListModel);
		
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
                	if (isEditing && selectedCategory.getName().equals(cat.getName()))
                		return true;
                	else {
                        mErrorLabel.setText("* Category name already exists");
                        return false;
                	}
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
	
	/**
	 * Check if category can be saved. If so, save category
	 */
	public void attemptSave()
	{
		
		if (!isSaveable())
			return;
		Category c = new Category();
		c.setName(categoryName.getText().trim());
		c.setColor(colorPicker.getCurrentColorState()); // Get color from color picker

		if (isEditing)
		{
			c.setCategoryID(selectedCategory.getCategoryID());
			MainPanel.getInstance().updateCategory(c);
		} else 
		{
			MainPanel.getInstance().addCategory(c);
		}
		
		MainPanel.getInstance().refreshCategoryFilterTab(); // Update created/edited category filters
		
		clearSelectedCategory();
		
		categoriesList.revalidate();
		categoriesList.repaint();
		
		populateCategories(JListModel);
		
		saveCategoryButton.setEnabled(false);
		
		
	}
	
	/**
	 * Changes the category of events related to categories being deleted
	 * @param categoryID the category to fetch events by
	 */
	private void changeEventOnDelete(UUID categoryID) 
	{
		List<Event> affectedEvents = EventModel.getInstance().getEvents(categoryID);
		
		for(Event e: affectedEvents)
		{
			e.setCategory(Category.DEFAULT_CATEGORY.getCategoryID());
			MainPanel.getInstance().updateEvent(e);
			MainPanel.getInstance().refreshView();
		}
	}
	
	/**
	 * Focus on the name field
	 */
	public void focusOnName()
	{
		categoryName.grabFocus();
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
				MainPanel.getInstance().refreshView();
			}
		});
		
		deleteCategoryButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				if (!isEditing) {
					System.out.println("Tried to delete while not editing");
					return;
				}
				
				changeEventOnDelete(selectedCategory.getCategoryID());
				removeCategory(selectedCategory);
				
				clearSelectedCategory();
				
				MainPanel.getInstance().refreshView();
				MainPanel.getInstance().refreshCategoryFilterTab();
			}
			
		});
		
		cancelEditButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				clearSelectedCategory();
			}
			
		});
		
		categoryName.getDocument().addDocumentListener(new DocumentListener() {
			
			@Override
			public void removeUpdate(DocumentEvent e) {
				categoryNameErrorLabel.setVisible(!validateText(categoryName.getText(), categoryNameErrorLabel));
				saveCategoryButton.setEnabled(isSaveable());
				System.out.println("Remove update, and firstEdit is" + firstEdit);
					
			}
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				categoryNameErrorLabel.setVisible(!validateText(categoryName.getText(), categoryNameErrorLabel));	
				saveCategoryButton.setEnabled(isSaveable());
				System.out.println("Insert update, and firstEdit is" + firstEdit);
				if (firstEdit){
					saveCategoryButton.setEnabled(false);
					firstEdit = false;
				}
			}
			
			@Override
			public void changedUpdate(DocumentEvent e) {
				//Not triggered by plaintext fields
			}
		});
		
		categoriesList.setSelectionModel(new DefaultListSelectionModel(){			
			@Override
			public void setSelectionInterval(int index0, int index1)
			{
				if (canChangeSelection())
				{
					selectionChangeErrorLabel.setText("");
					isEditing = true;
					firstEdit = true;
					System.out.println("Selection model, and firstEdit is" + firstEdit);
					super.setSelectionInterval(index0, index1);
				}
				else
				{
					selectionChangeErrorLabel.setText("* Cannot change selected category while editing");
					return;
				}
			}
		});
		
		categoriesList.addListSelectionListener(new ListSelectionListener(){

              @Override
              public void valueChanged(ListSelectionEvent e) {
                              
                      if (clearSelected || !isEditing) {
                              clearSelected = false;
                              return;
                      } else
                      {
	                      if (!e.getValueIsAdjusting()){
	                              selectedCategory = categoriesList.getSelectedValue();
	                              showEditionButtons();
	                          
	                              // Display data from selected category object
	                          categoryName.setText(selectedCategory.getName());
	                          colorPicker.moveColorSelector(selectedCategory.getColor());
	                      }
                      }
                      
                      System.out.println("Leaving selection listener, and firstEdit is" + firstEdit);
              }
		});

		saveCategoryButton.setEnabled(isSaveable());
		

	}
	
	/**
	 * Remove category from database and UI
	 * @param category the category to remove
	 */
	public void removeCategory (Category category){
		MainPanel.getInstance().deleteCategory(category);
		populateCategories(JListModel);
	}
	
	/**
	 * Populate the provided list with the categories
	 * @param listModel the model of the list to populate
	 */
	private void populateCategories(DefaultListModel<Category> listModel)
	{	
		clearSelected = true;
		listModel.clear();
		
		allCategories = CategoryModel.getInstance().getAllCategories();
		
		for (int i = 0; i < allCategories.size(); i++) {
			Category temp = allCategories.get(i);
			clearSelected = true;
			listModel.addElement(temp);
		}
	}
	
	
	/**
	 * Determines whether the selected category can be changed based on whether it's being edited or not
	 * @return boolean indicating whether the selection can be changed or not
	 */
	private boolean canChangeSelection(){
		
		float prev, curr;
		
		if (!isEditing)
			return true;
		else 
		{
			prev = new HSLColor(selectedCategory.getColor()).getHue();
			curr = new HSLColor(colorPicker.getCurrentColorState()).getHue();
			return (selectedCategory.getName().trim().equals(categoryName.getText().trim()))
					&& ((prev - 1) < curr && curr < (prev + 1)); // Tolerate a different of 1 to account for conversion error
		}
	}
	
	/**
	 * Hides edition buttons
	 */
	private void hideEditionButtons()
	{
		deleteCategoryButton.setVisible(false);
		cancelEditButton.setVisible(false);
	}
	
	/**
	 * Shows edition buttons
	 */
	private void showEditionButtons()
	{
		deleteCategoryButton.setVisible(true);
		cancelEditButton.setVisible(true);
	}
	
	/** 
	 * Clears the selected category
	 */
	public void clearSelectedCategory()
	{
		isEditing = false;
		clearSelected = true;
		categoriesList.clearSelection();
		categoryName.setText("");
		colorPicker.moveColorSelector(PastelColorPicker.DEFAULT_SELECTOR_LOCATION);
		hideEditionButtons();
		selectionChangeErrorLabel.setText("");
		saveCategoryButton.setEnabled(false);
	}

}
