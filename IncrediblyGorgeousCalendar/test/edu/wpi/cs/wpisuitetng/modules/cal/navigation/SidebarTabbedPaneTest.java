package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import static org.junit.Assert.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.swing.JButton;

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
        	Object sidebarEditButton = ((Field) sidebarEditButtonField).get(sidebar);
        	
        	Field sidebarCancelButtonField = sidebarClass.getDeclaredField("detailCancelButton");
        	sidebarCancelButtonField.setAccessible(true);
        	Object sidebarCancelButton = ((Field) sidebarCancelButtonField).get(sidebar);
        	
        	Method getButtonEnabled = sidebarEditButtonField.getDeclaringClass().getMethod("isEnabled");
        	
        	assertFalse(((boolean) getButtonEnabled.invoke(sidebarEditButton, null)));
        	assertFalse(((boolean) getButtonEnabled.invoke(sidebarCancelButton, null)));
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
        	Object sidebarEditButton = ((Field) sidebarEditButtonField).get(sidebar);
        	
        	Field sidebarCancelButtonField = sidebarClass.getDeclaredField("detailCancelButton");
        	sidebarCancelButtonField.setAccessible(true);
        	Object sidebarCancelButton = ((Field) sidebarCancelButtonField).get(sidebar);
        	
        	Method getButtonEnabled = sidebarEditButtonField.getDeclaringClass().getMethod("isEnabled");
        	
        	assertFalse(((boolean) getButtonEnabled.invoke(sidebarEditButton, null)));
        	assertFalse(((boolean) getButtonEnabled.invoke(sidebarCancelButton, null)));
        
        	sidebar.showDetails(new Event());
        
        	assertTrue(((boolean) getButtonEnabled.invoke(sidebarEditButton, null)));
        	assertTrue(((boolean) getButtonEnabled.invoke(sidebarCancelButton, null)));
    	
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
        	Object sidebarEditButton = ((Field) sidebarEditButtonField).get(sidebar);
        	
        	Field sidebarCancelButtonField = sidebarClass.getDeclaredField("detailCancelButton");
        	sidebarCancelButtonField.setAccessible(true);
        	Object sidebarCancelButton = ((Field) sidebarCancelButtonField).get(sidebar);
        	
        	Method getButtonEnabled = sidebarEditButtonField.getDeclaringClass().getMethod("isEnabled");
        	
        	assertFalse(((boolean) getButtonEnabled.invoke(sidebarEditButton, null)));
        	assertFalse(((boolean) getButtonEnabled.invoke(sidebarCancelButton, null)));
        
        	sidebar.showDetails(new Commitment());
        
        	assertTrue(((boolean) getButtonEnabled.invoke(sidebarEditButton, null)));
        	assertTrue(((boolean) getButtonEnabled.invoke(sidebarCancelButton, null)));
    	
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
        	Object sidebarEditButton = ((Field) sidebarEditButtonField).get(sidebar);
        	
        	Field sidebarCancelButtonField = sidebarClass.getDeclaredField("detailCancelButton");
        	sidebarCancelButtonField.setAccessible(true);
        	Object sidebarCancelButton = ((Field) sidebarCancelButtonField).get(sidebar);
        	
        	Method getButtonEnabled = sidebarEditButtonField.getDeclaringClass().getMethod("isEnabled");
        	
        	assertFalse(((boolean) getButtonEnabled.invoke(sidebarEditButton, null)));
        	assertFalse(((boolean) getButtonEnabled.invoke(sidebarCancelButton, null)));
        
        	sidebar.showDetails(new Commitment());
        
        	assertTrue(((boolean) getButtonEnabled.invoke(sidebarEditButton, null)));
        	assertTrue(((boolean) getButtonEnabled.invoke(sidebarCancelButton, null)));
        	
        	sidebar.clearDetails();
        	
        	assertFalse(((boolean) getButtonEnabled.invoke(sidebarEditButton, null)));
        	assertFalse(((boolean) getButtonEnabled.invoke(sidebarCancelButton, null)));
    	
        }catch(Exception e)
        {
        	e.printStackTrace();
        }
	}
}
