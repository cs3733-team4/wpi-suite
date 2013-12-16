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

import java.util.HashMap;
import java.util.HashSet;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.cal.models.client.CategoryModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.client.CommitmentModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.client.EventModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.server.CategoryEntityManager;
import edu.wpi.cs.wpisuitetng.modules.cal.models.server.CommitmentEntityManager;
import edu.wpi.cs.wpisuitetng.modules.cal.models.server.EventEntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;

/**
 * Allows testing of network things without having a real network to test on.
 * 
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
		
		// Should we put these here? Doing so breaks the network, but makes a global "clear cache" method
		CategoryModel.getInstance().invalidateCache();
		EventModel.getInstance().invalidateCache();
		CommitmentModel.getInstance().invalidateCache();
	}
}
