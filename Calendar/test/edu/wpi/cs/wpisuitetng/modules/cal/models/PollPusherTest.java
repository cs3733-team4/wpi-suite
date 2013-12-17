/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal.models;

import static org.junit.Assert.*;

import org.junit.*;

import edu.wpi.cs.wpisuitetng.modules.cal.models.server.PollPusher;

/**
 * tests for the Poll Pusher
 */
public class PollPusherTest
{
	PollPusher<TestEvent> pp;

	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception
	{
		PollPusher.class.getConstructor().setAccessible(true);
		pp = PollPusher.class.newInstance();
	}

	/**
	 * Test method for {@link edu.wpi.cs.wpisuitetng.modules.cal.models.server.PollPusher#getInstance(java.lang.Class)}.
	 */
	@Test
	public void testGetInstance()
	{
		assertNotNull(PollPusher.getInstance(TestEvent.class));
		assertNotNull(pp);
	}
	
	@Test
	public void testOneSession()
	{
		TestEvent t1 = new TestEvent("ses1", "Mydata");
		assertNull(pp.listenSession(t1));
		pp.updated("Mydata");
		assertEquals(1, t1.calls);
	}

	@Test
	public void testOneSessionUnregister()
	{
		TestEvent t1 = new TestEvent("ses1", "Mydata");
		assertNull(pp.listenSession(t1));
		pp.unlistenSession(t1);
		pp.updated("Mydata");
		assertEquals(0, t1.calls);
	}
	
	@Test
	public void testSafeUnregister()
	{
		TestEvent t1 = new TestEvent("ses1", "Mydata");
		pp.unlistenSession(t1);
	}

	@Test
	public void testDoubleListen()
	{
		TestEvent t1 = new TestEvent("ses1", "Mydata");
		TestEvent t2 = new TestEvent("ses2", "Mydata");
		assertNull(pp.listenSession(t1));
		assertNull(pp.listenSession(t2));
		pp.updated("Mydata");
		pp.updated("Mydata2"); // should be unregistered now
		assertEquals(1, t2.calls);
		assertEquals(1, t1.calls);
	}
	
	@Test
	public void testDoubleListenReReg()
	{
		TestEvent t1 = new TestEvent("ses1", "Mydata");
		TestEvent t2 = new TestEvent("ses2", "Mydata");
		assertNull(pp.listenSession(t1));
		assertNull(pp.listenSession(t2));
		pp.updated("Mydata");
		assertNull(pp.listenSession(t1));
		assertNull(pp.listenSession(t2));
		pp.updated("Mydata");
		assertEquals(2, t2.calls);
		assertEquals(2, t1.calls);
	}

	@Test
	public void testHeldoff()
	{
		TestEvent t1 = new TestEvent("ses1", "Mydata");
		TestEvent t2 = new TestEvent("ses2", "Mydata");
		assertNull(pp.listenSession(t1));
		assertNull(pp.listenSession(t2));
		pp.unlistenSession(t1);
		pp.updated("Mydata");
		assertEquals("[Mydata]", pp.listenSession(t1));
		pp.updated("Mydata2");
		assertEquals(1, t2.calls);
		assertEquals(0, t1.calls);
	}
	
	@Test
	public void testLongHeldoff()
	{
		TestEvent t1 = new TestEvent("ses1", "Mydatax");
		TestEvent t2 = new TestEvent("ses2", "Mydata");
		TestEvent t3 = new TestEvent("ses3", "Mydata2");
		assertNull(pp.listenSession(t1));
		pp.updated("Mydatax");
		assertNull(pp.listenSession(t2));
		pp.updated("Mydata");
		assertNull(pp.listenSession(t3));
		pp.updated("Mydata2");
		assertEquals("[Mydata,Mydata2]", pp.listenSession(t1));
		assertEquals(1, t3.calls);
		assertEquals(1, t2.calls);
		assertEquals(1, t1.calls);
	}

	@Test
	public void testEnsureMove()
	{
		TestEvent t1 = new TestEvent("ses1", "NOT_CALLED");
		assertNull(pp.listenSession(t1));
		pp.unlistenSession(t1);
		pp.updated("Mydatax");
		pp.updated("Mydata");
		assertEquals("[Mydatax,Mydata]", pp.listenSession(t1));
		pp.updated("Mydata2");
		assertEquals("[Mydata2]", pp.listenSession(t1));
		assertEquals(0, t1.calls);
	}
	
	@Test
	public void testPopOnTop()
	{
		TestEvent t1 = new TestEvent("ses1", "NOT_CALLED");
		TestEvent t2 = new TestEvent("ses2", "ALSO_NEVER");
		assertNull(pp.listenSession(t1));
		pp.unlistenSession(t1);
		pp.updated("Mydatax");
		pp.updated("Mydata");
		assertEquals("[Mydatax,Mydata]", pp.listenSession(t1));
		assertNull(pp.listenSession(t2));
		pp.unlistenSession(t2);
		pp.updated("Mydata2");
		assertEquals("[Mydata2]", pp.listenSession(t1));
		assertEquals("[Mydata2]", pp.listenSession(t2));
		assertNull(pp.listenSession(t1));
		assertNull(pp.listenSession(t2));
		pp.unlistenSession(t1);
		pp.unlistenSession(t2);
		assertEquals(0, t1.calls);
		assertEquals(0, t2.calls);
	}

	private static class TestEvent extends PollPusher.PushedInfo
	{
		private String expected;
		public int calls = 0;
		public TestEvent(String sid, String expected)
		{
			super(sid);
			this.expected = expected;
		}
		@Override
		public void pushUpdates(String item)
		{
			calls++;
			assertEquals(expected, item);
		}
	}
}
