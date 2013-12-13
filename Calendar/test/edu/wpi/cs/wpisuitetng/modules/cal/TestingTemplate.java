/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal; // Change this to be the same package as the class being tested; if the package isn't in testing, create it

import static org.junit.Assert.*;	// You'll need to import that function as well

import org.junit.Before;
import org.junit.Test;

public class TestingTemplate {

	// Put all variables you want globally viewable here, but uninitialized
	int sixsixsix;
	String hello;
	
	@Before
	public void setup() {
		//Construct your global variables you need here
		sixsixsix=333;
		hello="hello";
	}
	
	@Test
	public void testTestExists() { // Name the test after the function being tested / parts of that function you're testing
		assertNotNull(sixsixsix);
		assertNotNull(hello);
	}
	
	@Test
	public void testTestIsCorrect() { // Name the test after the function being tested / parts of that function you're testing
		
		int lucky=777; // You can create variables in the testing itself, but the variables arn't visible /saved to other tests
		
		assertTrue(hello.equals("hello"));
		assertFalse(sixsixsix==666);
		assertEquals("You can also add comments that will be shown if the test fails", sixsixsix, lucky-444);
	}

}
