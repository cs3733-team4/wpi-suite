/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/
package edu.wpi.cs.wpisuitetng.modules.cal;

import java.util.HashMap;
import java.util.HashSet;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CategoryEntityManager;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CommitmentEntityManager;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventEntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.*;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * @author justinhess
 * @version $Revision: 1.0 $
 */
public class MockNetwork extends Network {
	
	protected MockRequest lastRequestMade = null;
	protected HashMap<String, Project> projects = new HashMap<>();
	protected HashMap<String, User> users = new HashMap<>();
	protected HashMap<String, Session> sessions = new HashMap<>();
	protected static int projectID = 0, userID=0;
	protected Session loggedIn;
	protected EventEntityManager eventManager = new EventEntityManager(new MockData(new HashSet<Object>()));
	protected CommitmentEntityManager commitmentManager = new CommitmentEntityManager(new MockData(new HashSet<Object>()));
	protected CategoryEntityManager categoryManager = new CategoryEntityManager(new MockData(new HashSet<Object>()));
	
	/**
	 * Method makeRequest.
	 * @param path String
	 * @param requestMethod HttpMethod
	
	 * @return Request */
	@Override
	public Request makeRequest(String path, HttpMethod requestMethod) {
		if (requestMethod == null) {
			throw new NullPointerException("requestMethod may not be null");
		}
		
		lastRequestMade = new MockRequest(defaultNetworkConfiguration, path, requestMethod); 
		lastRequestMade.loadMockingbird(loggedIn, eventManager, commitmentManager, categoryManager);
		return lastRequestMade;
	}
	
	/**
	 * Method getLastRequestMade.
	
	 * @return MockRequest */
	public MockRequest getLastRequestMade() {
		return lastRequestMade;
	}
	
	public void addProject(String name)
	{
		projects.put(name, new Project(name, Integer.toString(projectID++)));
	}
	
	public void addUser(String name)
	{
		users.put(name, new User(name,name, name, userID++));
	}
	
	public void addSession(String user, String project, String ssid)
	{
		sessions.put(ssid, new Session(users.get(user), projects.get(project), ssid));
	}
	
	public void loginSession(String ssid)
	{
		loggedIn = sessions.get(ssid);
	}
	
	public void clearCache()
	{
		eventManager = new EventEntityManager(new MockData(new HashSet<Object>()));
		commitmentManager = new CommitmentEntityManager(new MockData(new HashSet<Object>()));
		categoryManager = new CategoryEntityManager(new MockData(new HashSet<Object>()));
	}
}
