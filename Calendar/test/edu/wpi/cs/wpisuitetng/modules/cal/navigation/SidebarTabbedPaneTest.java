/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import static org.junit.Assert.*;

import java.awt.Color;
import java.lang.reflect.Field;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;

import org.junit.Test;
import org.junit.BeforeClass;

import edu.wpi.cs.wpisuitetng.modules.cal.models.client.CategoryClient;
import edu.wpi.cs.wpisuitetng.modules.cal.models.client.CommitmentClient;
import edu.wpi.cs.wpisuitetng.modules.cal.models.client.EventClient;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.navigation.SidebarTabbedPane;
import edu.wpi.cs.wpisuitetng.modules.cal.MockNetwork;
import edu.wpi.cs.wpisuitetng.modules.cal.ReflectUtils;
import edu.wpi.cs.wpisuitetng.network.Network;

/**
 * Tests for SidebarTabbedPane class
 *
 */

public class SidebarTabbedPaneTest {

	private EventClient dummyModel = EventClient.getInstance();
	private CommitmentClient dummyModel2 = CommitmentClient.getInstance();
	
	@BeforeClass
	public static void setUp() throws Exception
	{
		((MockNetwork)Network.getInstance()).clearCache();
	}
	
	
	@Test
	public void testInitializeSidebar() {
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		
		assertNotNull("sidebar exists", sidebar);
	}
	
	@Test
	public void testButtonsStartDisabled() {
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		assertNotNull("sidebar exists", sidebar);
        try{
        	Class<? extends SidebarTabbedPane> sidebarClass = sidebar.getClass();
        	Field sidebarEditButtonField = sidebarClass.getDeclaredField("detailEditButton");
        	sidebarEditButtonField.setAccessible(true);
        	JButton sidebarEditButton = ((JButton)sidebarEditButtonField.get(sidebar));
        	
        	Field sidebarDeleteButtonField = sidebarClass.getDeclaredField("detailDeleteButton");
        	sidebarDeleteButtonField.setAccessible(true);
        	JButton sidebarDeleteButton = ((JButton)sidebarDeleteButtonField.get(sidebar));
        	
        	assertFalse(sidebarEditButton.isEnabled());
        	assertFalse(sidebarDeleteButton.isEnabled());
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
	
	@Test
	public void testButtonsBecomeEnabledOnEventDisplay() {
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		assertNotNull("sidebar exists", sidebar);
        try{
        	Class<? extends SidebarTabbedPane> sidebarClass = sidebar.getClass();
        	Field sidebarEditButtonField = sidebarClass.getDeclaredField("detailEditButton");
        	sidebarEditButtonField.setAccessible(true);
        	JButton sidebarEditButton = ((JButton) sidebarEditButtonField.get(sidebar));
        	
        	Field sidebarDeleteButtonField = sidebarClass.getDeclaredField("detailDeleteButton");
        	sidebarDeleteButtonField.setAccessible(true);
        	JButton sidebarDeleteButton = ((JButton) sidebarDeleteButtonField.get(sidebar));
        	
        	assertFalse(sidebarEditButton.isEnabled());
        	assertFalse(sidebarDeleteButton.isEnabled());
        
        	sidebar.showDetails(new Event());
        
        	assertTrue(sidebarEditButton.isEnabled());
        	assertTrue(sidebarDeleteButton.isEnabled());
    	
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
	
	@Test
	public void testButtonsBecomeEnabledOnCommitmentDisplay() {
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		assertNotNull("sidebar exists", sidebar);
        try{
        	Class<? extends SidebarTabbedPane> sidebarClass = sidebar.getClass();
        	Field sidebarEditButtonField = sidebarClass.getDeclaredField("detailEditButton");
        	sidebarEditButtonField.setAccessible(true);
        	JButton sidebarEditButton = ((JButton) sidebarEditButtonField.get(sidebar));
        	
        	Field sidebarDeleteButtonField = sidebarClass.getDeclaredField("detailDeleteButton");
        	sidebarDeleteButtonField.setAccessible(true);
        	JButton sidebarDeleteButton = ((JButton) sidebarDeleteButtonField.get(sidebar));
        	
        	assertFalse(sidebarEditButton.isEnabled());
        	assertFalse(sidebarDeleteButton.isEnabled());
        
        	sidebar.showDetails(new Commitment());
        
        	assertTrue(sidebarEditButton.isEnabled());
        	assertTrue(sidebarDeleteButton.isEnabled());
    	
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
	}

	@Test
	public void testButtonsBecomeDisabledAfterDisplayIsCleared() {
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		assertNotNull("sidebar exists", sidebar);
        try{
        	Class<? extends SidebarTabbedPane> sidebarClass = sidebar.getClass();
        	Field sidebarEditButtonField = sidebarClass.getDeclaredField("detailEditButton");
        	sidebarEditButtonField.setAccessible(true);
        	JButton sidebarEditButton = ((JButton) sidebarEditButtonField.get(sidebar));
        	
        	Field sidebarDeleteButtonField = sidebarClass.getDeclaredField("detailDeleteButton");
        	sidebarDeleteButtonField.setAccessible(true);
        	JButton sidebarDeleteButton = ((JButton) sidebarDeleteButtonField.get(sidebar));
        	
        	assertFalse(sidebarEditButton.isEnabled());
        	assertFalse(sidebarDeleteButton.isEnabled());
        
        	sidebar.showDetails(new Event());
        
        	assertTrue(sidebarEditButton.isEnabled());
        	assertTrue(sidebarDeleteButton.isEnabled());
        	
        	sidebar.clearDetails();
        	
        	assertFalse(sidebarEditButton.isEnabled());
        	assertFalse(sidebarDeleteButton.isEnabled());
    	
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
	
	@Test
	public void testInitializeSidebarWithFilteringTab() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		((MockNetwork)Network.getInstance()).clearCache();
		
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		
		assertNotNull("sidebar exists", sidebar);
		
		Field filTab = sidebar.getClass().getDeclaredField("categoryFilterTab");
		filTab.setAccessible(true);
		
		assertNotNull("Filtering tab exists", filTab.get(sidebar));
		
		Field catList = sidebar.getClass().getDeclaredField("categoryList");
		catList.setAccessible(true);
		
		assertNotNull("Filtering tab list exists", catList.get(sidebar));
		// note getSelectedCategories is a list of category UUIDs that correspond to each category
		for( UUID c : sidebar.getSelectedCategories())
			System.out.println(CategoryClient.getInstance().getCategoryByUUID(c));
		assertEquals("The filtering tab list starts with only one categorty to filter: uncategorized", 4, sidebar.getSelectedCategories().size());
		// Should only be one? no categories added
		assertNull("The filtering tab list starts with only one categorty to filter: uncategorized", CategoryClient.getInstance().getCategoryByUUID((UUID) sidebar.getSelectedCategories().toArray()[0]));
		// Why doesn't this work? At this point it should return an out of bounds error, but it has the blue/red categories from below tests? Cache isn't cleared?
		
		// show commitments is also a member of the list, but isn't included in the list of getSelectedCategories. It should always be on the list, can be turned off / on like
		// a normal category checkbox, and is turned off / on when select / deselect all is clicked
		assertTrue("showCommitments is by default true", sidebar.showCommitments());
	}
	
	@Test
	public void testAddingCategoriesToTheList() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		((MockNetwork)Network.getInstance()).clearCache();
		
		Category blue=new Category();
		blue.setName("blue");
		blue.setColor(Color.blue);
		CategoryClient.getInstance().put(blue);
		
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		
		assertNotNull("sidebar exists", sidebar);
		
		assertEquals("The filtering tab list starts with all added categories as well as an uncategorized checkbox", 5, sidebar.getSelectedCategories().size());
	}
	
	@Test
	public void testRefreshAfterAdding() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		((MockNetwork)Network.getInstance()).clearCache();
		
		Category blue=new Category();
		blue.setName("blue");
		blue.setColor(Color.blue);
		CategoryClient.getInstance().put(blue);
		
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		
		assertNotNull("sidebar exists", sidebar);
		
		assertEquals("The filtering tab list starts with all added categories as well as an uncategorized checkbox", 5, sidebar.getSelectedCategories().size());
		
		// If a new category is created and added to the database,
		
		Category red=new Category();
		red.setName("red");
		red.setColor(Color.red);
		CategoryClient.getInstance().put(red);
		
		assertEquals("The filtering tab list still only has the old categories,", 5, sidebar.getSelectedCategories().size());
		sidebar.refreshFilterTab();
		assertEquals("Until you refresh it, which is called normally in the createCategory tab", 6, sidebar.getSelectedCategories().size());
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void testViewableCategoriesChangeAfterUnselecting() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		((MockNetwork)Network.getInstance()).clearCache();
		
		Category blue=new Category();
		blue.setName("blue");
		blue.setColor(Color.blue);
		CategoryClient.getInstance().put(blue);
		
		Category red=new Category();
		red.setName("red");
		red.setColor(Color.red);
		CategoryClient.getInstance().put(red);
		
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		
		assertNotNull("sidebar exists", sidebar);
		
		assertEquals("The filtering tab list starts with all added categories as well as an uncategorized checkbox", 6, sidebar.getSelectedCategories().size());
		assertTrue("The filtering tab list starts with all added categories as well as an uncategorized checkbox", sidebar.getSelectedCategories().contains(red.getUuid()));
		
		// insert manual unchecking of red's box here
		Field cBoxList = ReflectUtils.getField(sidebar, "checkBoxCategoryMap");
		Set<Entry<JCheckBox,Category>> list=((HashMap<JCheckBox,Category>)cBoxList.get(sidebar)).entrySet();
		((JCheckBox)list.toArray(new Entry[0])[1].getKey()).doClick();
		// Someone more knowledgeable help me with this please
		
	}
	
	@Test
	public void testSelectAllAndDeselectAll() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		((MockNetwork)Network.getInstance()).clearCache();
		
		Category blue=new Category();
		blue.setName("blue");
		blue.setColor(Color.blue);
		CategoryClient.getInstance().put(blue);
		
		Category red=new Category();
		red.setName("red");
		red.setColor(Color.red);
		CategoryClient.getInstance().put(red);
		
		Category green=new Category();
		green.setName("green");
		green.setColor(Color.green);
		CategoryClient.getInstance().put(green);
		
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		
		assertEquals("The filtering tab list starts with all added categories as well as an uncategorized checkbox", 7, sidebar.getSelectedCategories().size());
		assertTrue("showCommitments is also among that list, and is by default true", sidebar.showCommitments());
		// meaning they all start off as checked
		
		sidebar.deselectAllCategories();
		
		assertEquals("Clicking on the clear will cause all categories to be unselected", 0, sidebar.getSelectedCategories().size());
		
		Field cButton = sidebar.getClass().getDeclaredField("clearAllButton");
		cButton.setAccessible(true);
		assertFalse("Clicking the clear button will disable it until one or more categories are reselected", ((JButton)cButton.get(sidebar)).isEnabled());
		

		sidebar.selectAllCategories();
		
		assertEquals("Clicking on select-all will cause all categories to be selected", 4, sidebar.getSelectedCategories().size());
		assertTrue("showCommitments will also be selected", sidebar.showCommitments());
		assertTrue("Clicking the select all button will re-enable the clear button if it was disabled before", ((JButton)cButton.get(sidebar)).isEnabled());
	}
}
