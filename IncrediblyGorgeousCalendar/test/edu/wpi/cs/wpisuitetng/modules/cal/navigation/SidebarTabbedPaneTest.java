package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import static org.junit.Assert.*;

import java.awt.Component;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

/**
 * Tests for SidebarTabbedPane class
 *
 */

public class SidebarTabbedPaneTest {

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
	public void testSidePaneIsNotFocused() {
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		assertFalse(sidebar.isFocusable());
	}
	
	@Test
	public void testDetailTabIsNotFocused() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		Field f = sidebar.getClass().getDeclaredField("detailTab");
		f.setAccessible(true);
		assertFalse(((JPanel)f.get(sidebar)).isFocusable());
	}
	
	@Test
	public void testDetailButtonPaneIsNotFocused() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		Field f = sidebar.getClass().getDeclaredField("detailButtonPane");
		f.setAccessible(true);
		assertFalse(((JPanel)f.get(sidebar)).isFocusable());
	}
	
	@Test
	public void testDetailEditButtonIsNotFocused() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		Field f = sidebar.getClass().getDeclaredField("detailEditButton");
		f.setAccessible(true);
		assertFalse(((JButton)f.get(sidebar)).isFocusable());
	}
	
/*	TODO: Uncomment when detailDeleteButton is implemented.
 * @Test
	public void testDetailDeleteButtonIsNotFocused() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		SidebarTabbedPane sidebar = new SidebarTabbedPane();
		Field f = sidebar.getClass().getDeclaredField("detailDeleteButton");
		f.setAccessible(true);
		assertFalse(((JButton)f.get(sidebar)).isFocusable());
	} */
}
