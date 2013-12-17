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

import static org.junit.Assert.*;

import java.awt.Component;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;

import javax.swing.JComboBox;

import org.junit.Before;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.cal.MockData;
import edu.wpi.cs.wpisuitetng.modules.cal.ReflectUtils;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CategoryEntityManager;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.tabs.AddEventDisplay;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;


public class CategoryRefreshingTest {
	MockData db = new MockData(new HashSet<Object>());
	AddEventDisplay aed = new AddEventDisplay();
	
	Project p = new Project("p","26");
    User u1 = new User("User1", "U1", null, 0);
    Session ses1 = new Session(u1, p, "26");
    
    JComboBox<Category> eventCategoryPicker;
    
    @Before
    public void setup() throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException
    {
    	eventCategoryPicker = ReflectUtils.getFieldValue(aed, "eventCategoryPicker");
    }
	
	@Test
	public void AddEventTabCategoryRefresh() throws WPISuiteException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Category c1 = new Category();
		Category c2 = new Category();
		CategoryEntityManager cem = new CategoryEntityManager(db);
		cem.save(ses1, c1);
		aed.refreshCategories();
		assertEquals(1, eventCategoryPicker.getItemCount());
	}

}
