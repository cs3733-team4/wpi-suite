/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.cal;

import java.lang.reflect.InvocationTargetException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthCalendarTest;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CategoryEntityManagerTest;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CommitmentClientTest;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventClientTest;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventEntityManagerTest;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CommitmentEntityManagerTest;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthDayTest;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.month.MonthItemTest;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.views.week.WeekCalendarTest;
import edu.wpi.cs.wpisuitetng.modules.cal.formulae.MonthsTest;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.GoToPanelTest;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.MainCalendarNavigationTest;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.SidebarTabbedPaneTest;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.CommitmentUIValidationTest;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.DatePickerTest;
import edu.wpi.cs.wpisuitetng.modules.cal.ui.EventUIValidationTest;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.cache.AccessOrderedListTest;
import edu.wpi.cs.wpisuitetng.modules.cal.utils.cache.CacheTest;
import edu.wpi.cs.wpisuitetng.modules.cal.navigation.CalendarNavigationModuleTest;
import edu.wpi.cs.wpisuitetng.modules.cal.year.YearCalendarTest;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;

/**
 * Test suite for running all test classes.
 * 
 */

@RunWith(Suite.class)
@SuiteClasses({
	//sorted by package
	MonthsTest.class,
	CommitmentEntityManagerTest.class, EventEntityManagerTest.class, CommitmentClientTest.class, EventClientTest.class, CategoryEntityManagerTest.class,
	CalendarNavigationModuleTest.class, GoToPanelTest.class, MainCalendarNavigationTest.class, SidebarTabbedPaneTest.class,
	MonthCalendarTest.class, MonthDayTest.class, MonthItemTest.class,
	WeekCalendarTest.class,
	CommitmentUIValidationTest.class, DatePickerTest.class,
	EventUIValidationTest.class,
	AccessOrderedListTest.class, CacheTest.class,
	YearCalendarTest.class})

public class TestSuite
{
	@BeforeClass
	public static void setUpClass() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException
	{
		MockNetwork mn = new MockNetwork();
		mn.addUser("testing11");
		mn.addUser("testing12");
		mn.addUser("testing21");
		mn.addUser("testing22");
		mn.addProject("project1");
		mn.addProject("project2");
		mn.addSession("testing11", "project1", "default");
		// TODO: add more sessions
		mn.loginSession("default");
		Network.initNetwork(mn);
		Network.getInstance().setDefaultNetworkConfiguration(new NetworkConfiguration("http://wpisuitetng"));
		ReflectUtils.callMethod(MainPanel.getInstance(), "finishInit");
	}

	@AfterClass
	public static void tearDownClass()
	{
		// Common cleanup for all tests
	}
}
