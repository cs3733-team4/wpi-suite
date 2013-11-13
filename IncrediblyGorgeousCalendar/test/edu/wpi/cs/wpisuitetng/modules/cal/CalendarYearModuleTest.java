package edu.wpi.cs.wpisuitetng.modules.cal;

import static org.junit.Assert.*;
import edu.wpi.cs.wpisuitetng.modules.cal.year.CalendarYearModule;

import org.joda.time.DateTime;
import org.junit.Test;

/**
 * Tests for CalendarYearModule class
 *
 */

public class CalendarYearModuleTest {

	DateTime timeOne = new DateTime(2012, 10, 1, 1, 1);
	DateTime timeTwo = new DateTime(2012, 11, 1, 1, 1);
	DateTime timeThree = new DateTime(2012, 12, 1, 1, 1);
	
	
	@Test
	public void testGetPrevious() {
		CalendarYearModule mCalYearModuleTimeOne = new CalendarYearModule(timeOne, null);
		CalendarYearModule mCalYearModuleTimeTwo = new CalendarYearModule(timeTwo, null);
		
		assertEquals("Previous should return timeOne", mCalYearModuleTimeOne.getTime(), mCalYearModuleTimeTwo.getPrevious().getTime());
	}
	
	@Test
	public void testGetFollowing() {
		CalendarYearModule mCalYearModuleTimeThree = new CalendarYearModule(timeThree, null);
		CalendarYearModule mCalYearModuleTimeTwo = new CalendarYearModule(timeTwo, null);
		
		assertEquals("Following should return timeThree", mCalYearModuleTimeThree.getTime(), mCalYearModuleTimeTwo.getFollowing().getTime());
	}
	
	@Test
	public void testGetFollowingThenGetPreviousTwice() {
		CalendarYearModule mCalYearModuleTimeOne = new CalendarYearModule(timeOne, null);
		CalendarYearModule mCalYearModuleTimeTwo = new CalendarYearModule(timeTwo, null);
		
		assertEquals("Following should return timeOne", mCalYearModuleTimeOne.getTime(), 
				mCalYearModuleTimeTwo.getFollowing().getPrevious().getPrevious().getTime());
	}

}
