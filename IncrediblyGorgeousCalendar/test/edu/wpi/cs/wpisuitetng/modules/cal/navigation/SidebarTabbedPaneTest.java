package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Component;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.junit.Test;
import org.junit.BeforeClass;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Category;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CategoryModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CommitmentModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventModel;
import edu.wpi.cs.wpisuitetng.modules.cal.MockNetwork;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;

/**
 * Tests for SidebarTabbedPane class
 *
 */

public class SidebarTabbedPaneTest {

	private EventModel dummyModel = EventModel.getInstance();
	private CommitmentModel dummyModel2 = CommitmentModel.getInstance();
	
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
		
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		
		assertNotNull("sidebar exists", sidebar);
		
		Field filTab = sidebar.getClass().getDeclaredField("categoryFilterTab");
		filTab.setAccessible(true);
		
		assertNotNull("Filtering tab exists", filTab.get(sidebar));
		
		Field catList = sidebar.getClass().getDeclaredField("categoryList");
		catList.setAccessible(true);
		
		assertNotNull("Filtering tab list exists", catList.get(sidebar));
		// note getSelectedCategories is a list of category UUIDs that correspond to each category
		assertEquals("The filtering tab list starts with only one categorty to filter: uncategorized", 1, sidebar.getSelectedCategories().size());
		
		((MockNetwork)Network.getInstance()).clearCache();
	}
	
	@Test
	public void testAddingCategoriesToTheList() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		Category blue=new Category();
		blue.setName("blue");
		blue.setColor(Color.blue);
		CategoryModel.getInstance().putCategory(blue);
		
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		
		assertNotNull("sidebar exists", sidebar);
		
		assertEquals("The filtering tab list starts with all added categories as well as an uncategorized checkbox", 2, sidebar.getSelectedCategories().size());
		assertEquals("The filtering tab list will contain the newly created category", blue.getCategoryID(), sidebar.getSelectedCategories().toArray()[1]);
		
		((MockNetwork)Network.getInstance()).clearCache();
	}
	
	@Test
	public void testRefreshAfterAdding() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		Category blue=new Category();
		blue.setName("blue");
		blue.setColor(Color.blue);
		CategoryModel.getInstance().putCategory(blue);
		
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		
		assertNotNull("sidebar exists", sidebar);
		
		assertEquals("The filtering tab list starts with all added categories as well as an uncategorized checkbox", 2, sidebar.getSelectedCategories().size());
		
		// If a new category is created and added to the database,
		
		Category red=new Category();
		red.setName("red");
		red.setColor(Color.red);
		CategoryModel.getInstance().putCategory(red);
		
		assertEquals("The filtering tab list still only has the old categories,", 2, sidebar.getSelectedCategories().size());
		sidebar.refreshFilterTab();
		assertEquals("Until you refresh it, which is called normally in the createCategory tab", 3, sidebar.getSelectedCategories().size());
		
		((MockNetwork)Network.getInstance()).clearCache();
	}
	
	@Test
	public void testRefresh() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		
		Category blue=new Category();
		blue.setName("blue");
		blue.setColor(Color.blue);
		CategoryModel.getInstance().putCategory(blue);
		
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		
		assertNotNull("sidebar exists", sidebar);
		
		assertEquals("The filtering tab list starts with all added categories as well as an uncategorized checkbox", 2, sidebar.getSelectedCategories().size());
		
		// If a new category is created and added to the database,
		
		Category red=new Category();
		red.setName("red");
		red.setColor(Color.red);
		CategoryModel.getInstance().putCategory(red);
		
		assertEquals("The filtering tab list still only has the old categories,", 2, sidebar.getSelectedCategories().size());
		sidebar.refreshFilterTab();
		assertEquals("Until you refresh it, which is called normally in the createCategory tab", 3, sidebar.getSelectedCategories().size());
		
		((MockNetwork)Network.getInstance()).clearCache();
	}
}
