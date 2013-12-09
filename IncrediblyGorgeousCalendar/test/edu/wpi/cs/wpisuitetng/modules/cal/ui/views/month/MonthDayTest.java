package edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month;

import static org.junit.Assert.*;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Commitment;
import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthDay;
import edu.wpi.cs.wpisuitetng.modules.cal.DayStyle;

import java.lang.reflect.Field;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Test;

public class MonthDayTest {

	DateTime now=new DateTime(2000,10,10,0,0);
	
	@Test
	public void testExists() {
		MonthDay MD = new MonthDay(now, DayStyle.Normal, null);
		assertNotNull("A MonthDay can be created", MD);
	}
	
	@Test
	public void testAddEvents() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MonthDay MD = new MonthDay(now, DayStyle.Normal, null);
		Event eatIcecream=new Event().addName("Eat icecream")
									 .addDescription("Yummy!")
									 .addStartTime(new DateTime(2000, 10, 10, 0, 0))
									 .addEndTime(new DateTime(2000, 10, 10, 0, 30));
									
		MD.addEvent(eatIcecream);
		
		Field f= MD.getClass().getDeclaredField("events");
		f.setAccessible(true);
		
		assertTrue("New events can be added to a MonthDay", ((List<Event>) f.get(MD)).contains(eatIcecream));
		
		Event throwUpIcecream=new Event().addName("Throw up icecream")
				 .addDescription("Ugh!")
				 .addStartTime(new DateTime(2000, 10, 10, 0, 30))
				 .addEndTime(new DateTime(2000, 10, 10, 1, 30));
		MD.addEvent(throwUpIcecream);
		assertTrue("Multiple events can be added to a MonthDay", ((List<Event>) f.get(MD)).contains(throwUpIcecream));
	}
	
	@Test
	public void testRemoveEvents() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MonthDay MD = new MonthDay(now, DayStyle.Normal, null);
		Event eatIcecream=new Event().addName("Eat icecream")
				 					 .addDescription("Yummy!")
				 					 .addStartTime(new DateTime(2000, 10, 10, 0, 0))
				 					 .addEndTime(new DateTime(2000, 10, 10, 0, 30));
				
		MD.addEvent(eatIcecream);
		
		Field f= MD.getClass().getDeclaredField("events");
		f.setAccessible(true);

		assertTrue("Events can be added,", ((List<Event>) f.get(MD)).contains(eatIcecream));
		
		MD.removeEvent(eatIcecream);

		assertFalse("Events can be removed from a MonthDay", ((List<Event>) f.get(MD)).contains(eatIcecream));
	}
	
	@Test
	public void testRemoveEventsMultiple() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MonthDay MD = new MonthDay(now, DayStyle.Normal, null);
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
		
		Field f= MD.getClass().getDeclaredField("events");
		f.setAccessible(true);
		
		assertFalse("Events can be removed from a MonthDay", ((List<Event>) f.get(MD)).contains(throwUpIcecream));
		assertTrue("Without removing any of the other events", ((List<Event>) f.get(MD)).contains(eatIcecream));
		
		
		
		Event seekRevenge=new Event().addName("Seek vengance")
				 					 .addDescription("Try to sell me bad icecream, will he?")
				 					 .addStartTime(new DateTime(2000, 10, 10, 1, 30))
				 					 .addEndTime(new DateTime(2000, 10, 10, 4, 0));
		MD.addEvent(throwUpIcecream);
		MD.addEvent(seekRevenge);
		
		MD.removeEvent(throwUpIcecream);
		
		assertTrue("Removing events between other events still won't affect the other events", ((List<Event>) f.get(MD)).contains(eatIcecream));
		assertTrue("Removing events between other events still won't affect the other events", ((List<Event>) f.get(MD)).contains(seekRevenge));
	}
	
	@Test
	public void testAddCommitments() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MonthDay MD = new MonthDay(now, DayStyle.Normal, null);
		Commitment icecreamDay=new Commitment().addName("Icecream day")
									 .addDescription("Eat icecream by today!")
									 .setDueDate(new DateTime(2000, 10, 10, 0, 0));
									
		MD.addCommitment(icecreamDay);
		
		Field f= MD.getClass().getDeclaredField("commitments");
		f.setAccessible(true);
		
		assertTrue("New commitments can be added to a MonthDay", ((List<Commitment>) f.get(MD)).contains(icecreamDay));
		
		Commitment venganceDay=new Commitment().addName("Vengance day")
				 .addDescription("Exact vengance by today!")
				 .setDueDate(new DateTime(2000, 10, 10, 0, 30));
		MD.addCommitment(venganceDay);
		
		assertTrue("Multiple commitments can be added to a MonthDay", ((List<Event>) f.get(MD)).contains(icecreamDay));
		assertTrue("Multiple commitments can be added to a MonthDay", ((List<Event>) f.get(MD)).contains(venganceDay));
	}
	
	@Test
	public void testRemoveCommitments() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MonthDay MD = new MonthDay(now, DayStyle.Normal, null);
		Commitment icecreamDay=new Commitment().addName("Icecream day")
				 					 .addDescription("Eat icecream by today!")
				 					 .setDueDate(new DateTime(2000, 10, 10, 0, 0));
				
		MD.addCommitment(icecreamDay);
		
		Field f= MD.getClass().getDeclaredField("commitments");
		f.setAccessible(true);
		
		assertTrue("Commitments can be added,", ((List<Commitment>) f.get(MD)).contains(icecreamDay));
		
		MD.removeCommitment(icecreamDay);
		
		assertFalse("and removed from a MonthDay", ((List<Commitment>) f.get(MD)).contains(icecreamDay));
	}
	
	@Test
	public void testRemoveCommitmentsMultiple() throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		MonthDay MD = new MonthDay(now, DayStyle.Normal, null);
		Commitment icecreamDay=new Commitment().addName("Icecream day")
				 					 .addDescription("Eat icecream by today!")
				 					 .setDueDate(new DateTime(2000, 10, 10, 0, 0));
				
		MD.addCommitment(icecreamDay);
		
		Commitment venganceDay=new Commitment().addName("Vengance day")
										 .addDescription("Exact vengace by today!")
										 .setDueDate(new DateTime(2000, 10, 10, 0, 30));
		MD.addCommitment(venganceDay);
		
		MD.removeCommitment(venganceDay);
		
		Field f= MD.getClass().getDeclaredField("commitments");
		f.setAccessible(true);
		
		assertFalse("Commitments can be removed from a MonthDay", ((List<Commitment>) f.get(MD)).contains(venganceDay));
		assertTrue("Without removing any of the other commitments", ((List<Commitment>) f.get(MD)).contains(icecreamDay));
	}

}
