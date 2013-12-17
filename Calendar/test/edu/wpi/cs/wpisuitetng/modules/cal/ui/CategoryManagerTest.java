package edu.wpi.cs.wpisuitetng.modules.cal.ui;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.ReflectUtils;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.tabs.CategoryManager;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.PastelColorPicker;

public class CategoryManagerTest 
{
	private CategoryManager mCategoryManager = new CategoryManager(); 
	
	JTextField nameTextField;
	PastelColorPicker colorPicker;
	DefaultListModel<Category> JListModel;
	List<Category> allCategories;
	
	
	@Before
	public void setup() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException 
	{
		nameTextField = ReflectUtils.getFieldValue(mCategoryManager, "categoryName");
		colorPicker = ReflectUtils.getFieldValue(mCategoryManager, "colorPicker");
		JListModel = ReflectUtils.getFieldValue(mCategoryManager, "JListModel");
		allCategories = ReflectUtils.getFieldValue(mCategoryManager, "allCategories");
	}
	
	@Test
	public void testIfCategoryManagerIsNotNull() 
	{
		assertNotNull("Make sure it actually runs", mCategoryManager);
	}
	
	@Test
	public void testIfSaveButtonStartsDisabled()
	{
		assertFalse("Category is not saveable by default", mCategoryManager.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsEnabledWithProperInput()
	{
		nameTextField.setText("test");
		assertTrue("Category only needs name entered, will save with default color", mCategoryManager.isSaveable());
	}
	
	@Test
	public void testIfSaveButtonIsDisabledWithImproperInput()
	{
		nameTextField.setText("    ");
		assertFalse("Category will not be saveable with only spaces for name", mCategoryManager.isSaveable());
	}
	
	@Test
	public void testSavingCategory()
	{					
		assertEquals("No categories should be in JListModel", 0, JListModel.getSize());

		nameTextField.setText("hello");
		mCategoryManager.attemptSave();
		
		assertEquals("Category should now be saved and displayed in the JListModel", 1, JListModel.getSize());
	}
	
	@Test
	public void testErrorMessageOnNameAlreadyExists()
	{
		Category c = new Category().addName("work");
		allCategories.add(c);
		
		nameTextField.setText("work");
		assertFalse("Category will not be saveable with only spaces for name", mCategoryManager.isSaveable());
	}
}
