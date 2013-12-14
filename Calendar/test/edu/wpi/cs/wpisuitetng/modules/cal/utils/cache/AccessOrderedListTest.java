package edu.wpi.cs.wpisuitetng.modules.cal.utils.cache;

import static org.junit.Assert.*;

import org.junit.Test;


public class AccessOrderedListTest {

	@Test
	public void constructorWorks() {
		AccessOrderedList<String> list = new AccessOrderedList<String>("hi there");
		assertTrue(list.element.equals("hi there"));
	}
	
	
	@Test
	public void mergeTwoWorks() {
		AccessOrderedList<String> a = new AccessOrderedList<String>("a");
		AccessOrderedList<String> b = new AccessOrderedList<String>("b");
		
		a.mergeTwo(a, b);
		
		assertTrue(a.below.equals(b) && b.above.equals(a));
	}
	
	@Test
	public void mergeSurroundingWorks() {
		AccessOrderedList<String> a = new AccessOrderedList<String>("a");
		AccessOrderedList<String> b = new AccessOrderedList<String>("b");
		AccessOrderedList<String> c = new AccessOrderedList<String>("c");
		a.mergeTwo(a, b);
		a.mergeTwo(b, c);
		
		b.mergeSurrounding();
		
		assertTrue(a.below.equals(c) && c.above.equals(a));
	}
	
	
	@Test
	public void accessReturnsItself() {
		AccessOrderedList<String> a = new AccessOrderedList<String>("a");
		AccessOrderedList<String> b = new AccessOrderedList<String>("b");
		AccessOrderedList<String> c = new AccessOrderedList<String>("c");
		a.mergeTwo(a, b);
		a.mergeTwo(b, c);
	
		
		assertTrue(b.access(a).getB().equals(b));
	}
	
	
	@Test
	public void accessPatchesWhole() {
		AccessOrderedList<String> a = new AccessOrderedList<String>("a");
		AccessOrderedList<String> b = new AccessOrderedList<String>("b");
		AccessOrderedList<String> c = new AccessOrderedList<String>("c");
		a.mergeTwo(a, b);
		a.mergeTwo(b, c);
	
		b.access(a);
		
		assertTrue(a.below.equals(c) && c.above.equals(a));
	}
	
	@Test
	public void accessRemovesOldReferences() {
		AccessOrderedList<String> a = new AccessOrderedList<String>("a");
		AccessOrderedList<String> b = new AccessOrderedList<String>("b");
		AccessOrderedList<String> c = new AccessOrderedList<String>("c");
		a.mergeTwo(a, b);
		a.mergeTwo(b, c);
	
		b.access(a);
		
		assertTrue(b.above == null);
	}
	
	@Test
	public void accessSetsNewTop() {
		AccessOrderedList<String> a = new AccessOrderedList<String>("a");
		AccessOrderedList<String> b = new AccessOrderedList<String>("b");
		AccessOrderedList<String> c = new AccessOrderedList<String>("c");
		a.mergeTwo(a, b);
		a.mergeTwo(b, c);
	
		b.access(a);
		
		assertTrue(b.below.equals(a));
	}
	
	
	@Test
	public void iteratorWithNoAccess() {
		AccessOrderedList<String> a = new AccessOrderedList<String>("a");
		AccessOrderedList<String> b = new AccessOrderedList<String>("b");
		AccessOrderedList<String> c = new AccessOrderedList<String>("c");
		a.mergeTwo(a, b);
		a.mergeTwo(b, c);
	
		String[] answers = {"c", "b", "a"};
		int answerCount = 0;
		
		for(String s : c)
		{
			assertTrue(s.equals(answers[answerCount++]));
		}
	}
	
	
	
	@Test
	public void iteratorWithAccess() {
		AccessOrderedList<String> a = new AccessOrderedList<String>("a");
		AccessOrderedList<String> b = new AccessOrderedList<String>("b");
		AccessOrderedList<String> c = new AccessOrderedList<String>("c");
		a.mergeTwo(a, b);
		a.mergeTwo(b, c);
		
		b.access(a);
	
		String[] answers = {"c", "a", "b"};
		int answerCount = 0;
		
		for(String s : c)
		{
			assertTrue(s.equals(answers[answerCount++]));
		}
	}
	
	
}
