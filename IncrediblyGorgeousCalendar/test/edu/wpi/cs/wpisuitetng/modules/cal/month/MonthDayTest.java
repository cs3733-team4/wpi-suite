package edu.wpi.cs.wpisuitetng.modules.cal.month;

import static org.junit.Assert.*;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.month.MonthDay;
import edu.wpi.cs.wpisuitetng.modules.cal.DayStyle;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

import org.joda.time.DateTime;
import org.junit.Test;

public class MonthDayTest {

	DateTime now=new DateTime(2000,10,10,0,0);
	
	@Test
	public void testExists() {
		MonthDay MD = new MonthDay(now, DayStyle.Normal);
		assertNotNull("A MonthDay can be created", MD);
	}
	
	@Test
	public void testAddEvents() {
		MonthDay MD = new MonthDay(now, DayStyle.Normal);
		Event eatIcecream=new Event().addName("Eat icecream")
									 .addDescription("Yummy!")
									 .addStartTime(new DateTime(2000, 10, 10, 0, 0))
									 .addEndTime(new DateTime(2000, 10, 10, 0, 30));
									
		MD.addEvent(eatIcecream);
		assertTrue("New events can be added to a MonthDay", MD.hasEvent(eatIcecream));
		Event throwUpIcecream=new Event().addName("Throw up icecream")
				 .addDescription("Ugh!")
				 .addStartTime(new DateTime(2000, 10, 10, 0, 30))
				 .addEndTime(new DateTime(2000, 10, 10, 1, 30));
		MD.addEvent(throwUpIcecream);
		assertTrue("Multiple events can be added to a MonthDay", MD.hasEvent(throwUpIcecream));
	}
	
	@Test
	public void testRemoveEvents() {
		MonthDay MD = new MonthDay(now, DayStyle.Normal);
		Event eatIcecream=new Event().addName("Eat icecream")
				 					 .addDescription("Yummy!")
				 					 .addStartTime(new DateTime(2000, 10, 10, 0, 0))
				 					 .addEndTime(new DateTime(2000, 10, 10, 0, 30));
				
		MD.addEvent(eatIcecream);
		
		Event throwUpIcecream=new Event().addName("Throw up icecream")
										 .addDescription("Ugh!")
										 .addStartTime(new DateTime(2000, 10, 10, 0, 30))
										 .addEndTime(new DateTime(2000, 10, 10, 1, 30));
		MD.addEvent(throwUpIcecream);
		
		MD.removeEvent(throwUpIcecream);
		
		assertFalse("Events can be removed from a MonthDay", MD.hasEvent(throwUpIcecream));
		assertTrue("Without removing any of the other events", MD.hasEvent(eatIcecream));
		
		
		
		Event seekRevenge=new Event().addName("Seek vengance")
				 					 .addDescription("Try to sell me bad icecream, will he?")
				 					 .addStartTime(new DateTime(2000, 10, 10, 1, 30))
				 					 .addEndTime(new DateTime(2000, 10, 10, 4, 0));
		MD.addEvent(throwUpIcecream);
		MD.addEvent(seekRevenge);
		
		MD.removeEvent(throwUpIcecream);
		
		assertTrue("Removing events between other events still won't affect the other events", MD.hasEvent(eatIcecream));
		assertTrue("Removing events between other events still won't affect the other events", MD.hasEvent(seekRevenge));
	}

}
