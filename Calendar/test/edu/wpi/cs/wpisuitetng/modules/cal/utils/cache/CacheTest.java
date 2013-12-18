package edu.wpi.cs.wpisuitetng.modules.cal.utils.cache;

import static org.junit.Assert.*;

import org.junit.Test;


public class CacheTest {

	@Test
	public void creatingCacheNoFail() {
		Cache<String, String> c = new Cache<>("");
		//we didnt error!
		assertTrue(true);
	}
	
	@Test
	public void addingToCacheNoFail() {
		Cache<String, String> c = new Cache<>("");
		c.put("something", "epic!!!");
		//we didnt error!
		assertTrue(true);
	}
	
	@Test
	public void highLevelIteraterFromCacheNoFail() {
		Cache<String, String> c = new Cache<>("");
		c.put("something", "epic!!!");
		assertTrue(c.accessOrderedCallIterator("something") != null);
	}
	
	@Test
	public void highLevelIteraterFromCacheNull() {
		Cache<String, String> c = new Cache<>("");
		c.put("something", "epic!!!");
		assertTrue(c.accessOrderedCallIterator("borked") == null);
	}
	
	@Test
	public void lowLevelIteraterFromCacheNoFail() {
		Cache<String, String> c = new Cache<>("");
		c.put("something", "epic!!!");
		assertTrue(c.timeOrderedCallIterator("something") != null);
	}
	
	@Test
	public void lowLevelIteraterFromCacheNPE() {
		Cache<String, String> c = new Cache<>("");
		c.put("something", "epic!!!");
		c.timeOrderedCallIterator("borked"); // should not throw NPE, should create session
	}
	
	@Test
	public void highLevelIteraterFromCacheWorksOnce() {
		Cache<String, String> c = new Cache<>("");
		c.put("something", "epic!!!");
		for(TimeOrderedList<String, String> q : c.accessOrderedCallIterator("something"))
		{
			assertTrue(q.getValue().equals("epic!!!"));
		}
	}
	
	
	@Test
	public void cacheUpdatesAccessOrder() {
		Cache<String, String> c = new Cache<>("");
		c.put("something", "epic!!!");
		c.put("even", "better");
		
		assertTrue(c.head.element.getValue().equals("better"));
	}
	
	
	@Test
	public void highLevelIteraterFromCacheWorksMultiInsert() {
		Cache<String, String> c = new Cache<>("");
		c.put("something", "epic!!!");
		c.put("even", "better");
		c.put("takes the", "cake");
		c.put("next", "level");
		
		String[] expected = {"epic!!!", "better", "cake", "level"};
		int xpCount = 0;
		boolean successes = true;
		
		for(TimeOrderedList<String, String> q : c.accessOrderedCallIterator("something"))
		{
			successes &= q.getValue().equals(expected[xpCount++]);
		}
		assertTrue(successes);
	}
	
	@Test
	public void cacheUpdatesAccessOrderAfterAccess() {
		Cache<String, String> c = new Cache<>("");
		c.put("something", "epic!!!");
		c.put("even", "better");
		c.put("a", "a");
		c.put("b", "b");
		c.put("c", "c");
		c.put("d", "d");
		c.put("e", "e");
		c.put("f", "f");
		
		
		assertTrue(c.access("c").equals(c.head.element.getValue()));
	}
	
	
	@Test
	public void highLevelIteraterFromCacheWorksMultiInsertShuffle() {
		Cache<String, String> c = new Cache<>("");
		c.put("something", "epic!!!");
		c.put("even", "better");
		c.put("takes the", "cake");
		c.put("next", "level");
		
		c.access("takes the");
		
		String[] expected = {"epic!!!", "better", "level", "cake"};
		int xpCount = 0;
		boolean successes = true;
		
		for(TimeOrderedList<String, String> q : c.accessOrderedCallIterator("something"))
		{
			successes &= q.getValue().equals(expected[xpCount++]);
		}
		assertTrue(successes);
	}
	
	
	@Test
	public void lowLevelIteraterFromCacheWorksMultiInsertShuffle() {
		Cache<String, String> c = new Cache<>("");
		c.put("something", "epic!!!");
		c.put("even", "better");
		c.put("takes the", "cake");
		c.put("next", "level");
		
		//TODO: I'm not sure if this is a useful test anymore as we have changed these requirements
		String[] expected = {"cake", "better", "epic!!!", ""};
		int xpCount = 0;
		
		for(String q : c.timeOrderedCallIterator("next"))
		{
			assertEquals(q, expected[xpCount++]);
			assertTrue(xpCount <= expected.length);
		}
	}
}
