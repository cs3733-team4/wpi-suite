package edu.wpi.cs.wpisuitetng.modules.cal;

/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team Rolling Thunder
 ******************************************************************************/

import java.util.HashSet;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CategoryEntityManager;
import edu.wpi.cs.wpisuitetng.modules.cal.models.CommitmentEntityManager;
import edu.wpi.cs.wpisuitetng.modules.cal.models.EventEntityManager;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * @author justinhess
 * @version $Revision: 1.0 $
 */
public class MockRequest extends Request
{

	protected boolean sent = false;
	protected String relPath;
	private Session session;
	private EventEntityManager eventManager;
	private CommitmentEntityManager commitmentManager;
	private CategoryEntityManager categoryManager;

	/**
	 * Constructor for MockRequest.
	 * 
	 * @param networkConfiguration
	 *            NetworkConfiguration
	 * @param path
	 *            String
	 * @param requestMethod
	 *            HttpMethod
	 */
	public MockRequest(NetworkConfiguration networkConfiguration, String path, HttpMethod requestMethod)
	{
		super(networkConfiguration, path, requestMethod);
		relPath = path;
	}

	/**
	 * Method send.
	 * 
	 * @throws IllegalStateException
	 */
	@Override
	public void send() throws IllegalStateException
	{
		String[] bitsOfUrl = relPath.split("/");
		if (!bitsOfUrl[0].equals("cal"))
			throw new IllegalStateException("Not a calender!" + bitsOfUrl[0] + "|yay");
		if (bitsOfUrl.length >= 3)
			bitsOfUrl[2] = bitsOfUrl[2].replaceAll("%2C", ",");
		
		this.setResponse(new ResponseModel());
		sent = true;
		try
		{
			EntityManager<? extends Model> em;
			switch (bitsOfUrl[1])
			{
				case "events":
					em = eventManager;
					break;
				case "commitments":
					em = commitmentManager;
					break;
				case "categories":
					em = categoryManager;
					break;
				default:
					throw new IllegalStateException("Not a knkown ent!" + bitsOfUrl[1]);
			}

			switch (getHttpMethod())
			{
				case GET:
					this.response.setBody(new Gson().toJson(em.getEntity(session, bitsOfUrl[2])));
					break;
				case POST:
					em.update(session, getBody());
					break;
				case PUT:
					em.makeEntity(session, getBody());
					break;
				case DELETE:
					em.deleteEntity(session, bitsOfUrl[2]);
				default:
					break;
			}
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
		this.notifyObserversResponseSuccess();
	}

	/**
	 * Method isSent.
	 * 
	 * @return boolean
	 */
	public boolean isSent()
	{
		return sent;
	}

	/**
	 * Loads the mock data
	 * 
	 * @param loggedIn
	 * @param eventManager2
	 * @param commitmentManager2
	 * @param categoryManager2
	 */
	public void loadMockingbird(Session loggedIn, EventEntityManager eventManager2, CommitmentEntityManager commitmentManager2,
			CategoryEntityManager categoryManager2)
	{
		session = loggedIn;
		eventManager = eventManager2;
		categoryManager = categoryManager2;
		commitmentManager = commitmentManager2;
	}
}