package edu.wpi.cs.wpisuitetng.modules.cal.navigation;

import static org.junit.Assert.*;
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
	public void testGetPrevious() {
		CalendarNavigationModule mCalYearModuleTimeOne = new CalendarNavigationModule(timeOne, null);
		CalendarNavigationModule mCalYearModuleTimeTwo = new CalendarNavigationModule(timeTwo, null);
		
		assertEquals("Previous should return timeOne", mCalYearModuleTimeOne.getTime(), mCalYearModuleTimeTwo.getPrevious().getTime());
	}
	
	@Test
	public void testGetFollowing() {
		CalendarNavigationModule mCalYearModuleTimeThree = new CalendarNavigationModule(timeThree, null);
		CalendarNavigationModule mCalYearModuleTimeTwo = new CalendarNavigationModule(timeTwo, null);
		
		assertEquals("Following should return timeThree", mCalYearModuleTimeThree.getTime(), mCalYearModuleTimeTwo.getFollowing().getTime());
	}
	
	@Test
	public void testGetFollowingThenGetPreviousTwice() {
		CalendarNavigationModule mCalYearModuleTimeOne = new CalendarNavigationModule(timeOne, null);
		CalendarNavigationModule mCalYearModuleTimeTwo = new CalendarNavigationModule(timeTwo, null);
		
		assertEquals("Following should return timeOne", mCalYearModuleTimeOne.getTime(), 
				mCalYearModuleTimeTwo.getFollowing().getPrevious().getPrevious().getTime());
	}

}
