package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import edu.wpi.cs.wpisuitetng.modules.cal.navigation.CalendarNavigationModule;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Tests for CalendarYearModule class
 *
 */

public class CalendarNavigationModuleTest {

	DateTime timeOne = new DateTime(2012, 10, 1, 1, 1);
	DateTime timeTwo = new DateTime(2012, 11, 1, 1, 1);
	DateTime timeThree = new DateTime(2012, 12, 1, 1, 1);
	
	
	@Test
	public void testGetPrevious() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		CalendarNavigationModule mCalYearModuleTimeOne = new CalendarNavigationModule(timeOne, null, false);
		CalendarNavigationModule mCalYearModuleTimeTwo = new CalendarNavigationModule(timeTwo, null, false);
		CalendarNavigationModule Result = mCalYearModuleTimeTwo.getPrevious();
		Field f= mCalYearModuleTimeOne.getClass().getDeclaredField("time");
		f.setAccessible(true);
		assertEquals("Previous should return timeOne",f.get(mCalYearModuleTimeOne), f.get(Result));
	}
	
	@Test
	public void testGetFollowing() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		CalendarNavigationModule mCalYearModuleTimeThree = new CalendarNavigationModule(timeThree, null, false);
		CalendarNavigationModule mCalYearModuleTimeTwo = new CalendarNavigationModule(timeTwo, null, false);
		CalendarNavigationModule Result = mCalYearModuleTimeTwo.getFollowing();
		
		Field f= mCalYearModuleTimeTwo.getClass().getDeclaredField("time");
		f.setAccessible(true);
		
		assertEquals("Following should return timeThree", f.get(mCalYearModuleTimeThree), f.get(Result));
	}
	
	@Test
	public void testGetFollowingThenGetPreviousTwice() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		CalendarNavigationModule mCalYearModuleTimeOne = new CalendarNavigationModule(timeOne, null, false);
		CalendarNavigationModule mCalYearModuleTimeTwo = new CalendarNavigationModule(timeTwo, null, false);
		CalendarNavigationModule Result = mCalYearModuleTimeTwo.getFollowing().getPrevious().getPrevious();
		
		Field f= mCalYearModuleTimeOne.getClass().getDeclaredField("time");
		f.setAccessible(true);
		
		assertEquals("Following should return timeOne", f.get(mCalYearModuleTimeOne), f.get(Result));
	}

}
