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

import java.lang.reflect.Array;
import java.util.Arrays;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.cal.models.server.CategoryEntityManager;
import edu.wpi.cs.wpisuitetng.modules.cal.models.server.CommitmentEntityManager;
import edu.wpi.cs.wpisuitetng.modules.cal.models.server.EventEntityManager;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.configuration.NetworkConfiguration;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.ResponseModel;

/**
 * Enables fake requests to actually contact the right EM
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
		boolean advanced;
		if (advanced = bitsOfUrl[0].equals("Advanced"))
		{
			bitsOfUrl = Arrays.copyOfRange(bitsOfUrl, 1, bitsOfUrl.length);
		}
		if (!bitsOfUrl[0].equals("cal"))
			throw new IllegalStateException("Not a calender!" + bitsOfUrl[0] + "|yay");
		
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
					this.response.setBody(advanced ? em.advancedGet(session, bitsOfUrl) : new Gson().toJson(bitsOfUrl.length > 2 ? em.getEntity(session, bitsOfUrl[2]) : em.getAll(session)));
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
