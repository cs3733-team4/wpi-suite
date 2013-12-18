package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.joda.time.DateTime;
import org.junit.Test;

import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.navigation.MainCalendarNavigation;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthCalendar;

public class MainCalendarNavigationTest {

	MainPanel dummyPanel;
	
	MainCalendarNavigation mc = new MainCalendarNavigation(dummyPanel ,new MonthCalendar(new DateTime()));
	
	@Test
	public void testMainCalendarFocused() {
		assertTrue(mc.isFocusable());
	}
	
	@Test
	public void testPreviousButtonNotFocused() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = mc.getClass().getDeclaredField("previousButton");
		f.setAccessible(true);
		assertFalse(((JButton)f.get(mc)).isFocusable());
	}
	
	@Test
	public void testNextButtonNotFocused() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = mc.getClass().getDeclaredField("nextButton");
		f.setAccessible(true);
		assertFalse(((JButton)f.get(mc)).isFocusable());
	}
	
	@Test
	public void testTodayButtonNotFocused() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = mc.getClass().getDeclaredField("todayButton");
		f.setAccessible(true);
		assertFalse(((JButton)f.get(mc)).isFocusable());
	}
	
	@Test
	public void testNavigatoinButtonPanelNotFocused() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		Field f = mc.getClass().getDeclaredField("navigationButtonPanel");
		f.setAccessible(true);
		assertFalse(((JPanel)f.get(mc)).isFocusable());
	}

}
