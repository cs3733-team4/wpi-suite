package edu.wpi.cs.wpisuitetng.modules.cal;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


/**
 * Test suite for running all test classes.
 *
 */


@RunWith(Suite.class)
@SuiteClasses({CalendarYearModuleTest.class, MonthItemTest.class, MonthCalendarTest.class, YearCalendarHolderTest.class, MonthDayTest.class})

public class TestSuite {

}
