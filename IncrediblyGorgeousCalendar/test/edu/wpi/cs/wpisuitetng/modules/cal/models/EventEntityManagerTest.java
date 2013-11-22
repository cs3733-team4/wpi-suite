package edu.wpi.cs.wpisuitetng.modules.cal.models;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.cal.MockData;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventEntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class EventEntityManagerTest {

	MockData db = new MockData(new HashSet<Object>());
	
	Event e = new Event();
	User u1 = new User("Prometheus", "twack", null, 0);
	Session ses1 = new Session(u1, "16");
	
	@Test
	public void testCanCreate() {
		EventEntityManager eem = new EventEntityManager(db);
		assertNotNull(eem);
	}
	
	@Test
	public void testCount() throws WPISuiteException {
		EventEntityManager eem = new EventEntityManager(db);
		assertEquals(0, eem.Count());
	}
	
	@Test
	public void testMakeEntity() throws WPISuiteException {
		EventEntityManager eem = new EventEntityManager(db);
		assertNotNull(eem.makeEntity(ses1, e.toJSON()));
	}
}
