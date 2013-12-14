package edu.wpi.cs.wpisuitetng.modules.cal.ui;

import static org.junit.Assert.*;

import javax.swing.JTextField;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.ReflectUtils;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.tabs.CategoryManager;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.PastelColorPicker;

public class CategoryManagerTest 
{
	private CategoryManager mCategoryManager = new CategoryManager(); 
	
	JTextField nameTextField;
	PastelColorPicker colorPicker;
	
	@Before
	public void setup() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException 
	{
		nameTextField = ReflectUtils.getFieldValue(mCategoryManager, "categoryName");
		colorPicker = ReflectUtils.getFieldValue(mCategoryManager, "colorPicker");
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
		nameTextField.setText("work");
		assertTrue("Category only needs name entered, will save with default color", mCategoryManager.isSaveable());
	}
}
