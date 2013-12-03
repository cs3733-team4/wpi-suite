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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;

/**
 * Class for abstracting the network access away.
 */
public class ServerManager {
	public static final String separator = "%2C";
	public static <T> ArrayList<T> get(String path, final Class classType, String... args) {
		final Semaphore sem = new Semaphore(1);
		try {
			sem.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final ArrayList<T> events = new ArrayList<T>();
		
		// Send a request to the core to save this message
		final Request request = Network.getInstance().makeRequest(path + glue(args),
				HttpMethod.GET);
		request.addObserver(new RequestObserver() {

			@Override
			public void responseSuccess(IRequest iReq) {
				final Gson parser = new Gson();
				events.addAll(Arrays.asList((T[])parser.fromJson(iReq.getResponse().getBody(), classType)));
				sem.release();
			}

			@Override
			public void responseError(IRequest iReq) {
				System.err.println("The request to select events errored:");
				System.err.println(iReq.getBody());
				sem.release();

			}

			@Override
			public void fail(IRequest iReq, Exception exception) {
				System.err.println("The request to select events failed:");
				System.err.println(iReq.getBody());
				sem.release();
			}
		}); // add an observer to process the response
		request.send(); // send the request

		boolean acquired = false;
		while (!acquired) {
			try {
				sem.acquire();
				acquired = true;
			} catch (InterruptedException e) {
			}
		}
		return events;
	}
	public static boolean put(String path, String json)
	{
		return sendData(HttpMethod.PUT, path, json);
	}
	
	public static boolean post(String path, String json)
	{
		return sendData(HttpMethod.POST, path, json);
	}
	public static boolean delete(String path, String json)
	{
		return sendData(HttpMethod.DELETE, path, json);
	}
	
	public static boolean sendData(HttpMethod method, String path, String json) {

		final Request request = Network.getInstance().makeRequest(path,
				method);
		final Semaphore sem = new Semaphore(1);
		try {
			sem.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final Boolean[] success = new Boolean[1];
		success[0] = Boolean.FALSE;
		// Send a request to the core to save this message
		
		request.setBody(json);
		request.addObserver(new RequestObserver() {

			@Override
			public void responseSuccess(IRequest iReq) {
				success[0] = Boolean.TRUE;
				sem.release();
			}

			@Override
			public void responseError(IRequest iReq) {
				System.err.println("The request to add data errored:");
				System.err.println(iReq.getResponse().getBody());
				sem.release();
 
			}

			@Override
			public void fail(IRequest iReq, Exception exception) {
				System.err.println("The request to add events failed:");
				System.err.println(iReq.getBody());
				sem.release();
			}
		}); // add an observer to process the response
		request.send(); // send the request

		boolean acquired = false;
		while (!acquired) {
			try {
				sem.acquire();
				acquired = true;
			} catch (InterruptedException e) {
			}
		}
		return success[0].booleanValue();
	}

	/**
	 * "glues" together arguments and separates them with commas.
	 * @param args
	 * @return 
	 */
	public static String glue(String[] args) {
		StringBuilder sb = new StringBuilder();
		for (String string : args) {
			sb.append(string);
			if (args[args.length - 1] != string) // yes, this is reference equals to check for the last item
				sb.append(separator);
		}
		return sb.toString();
	}
}
