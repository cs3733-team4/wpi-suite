/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.cal.models.server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.Model;
import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Category;

public class CategoryEntityManager extends CachedEntityManager<Category>
{

	/**
	 * Constructs the entity manager. This constructor is called by
	 * {@link edu.wpi.cs.wpisuitetng.ManagerLayer#ManagerLayer()}. To make sure
	 * this happens, be sure to place add this entity manager to the map in the
	 * ManagerLayer file.
	 * 
	 * @param db
	 *            a reference to the persistent database
	 */
	public CategoryEntityManager(Data db)
	{
		super(db);
		pollPusher = PollPusher.getInstance(Category.class);
	}

	/**
	 * Saves a Category when it is received from a client
	 * 
	 * @param s
	 *            session
	 * @param content
	 *            string of what is actually being created
	 * @return newCategory newly created entity of category
	 */
	@Override
	public Category makeEntity(Session s, String content) throws WPISuiteException
	{
		final Category newCategory = Category.fromJson(content);
		newCategory.setOwner(s.getUser());
		newCategory.setProject(s.getProject());
		if (!db.save(newCategory, s.getProject()))
		{
			throw new WPISuiteException();
		}
		PollPusher.getInstance(Category.class).updated(updated(newCategory));
		return newCategory;
	}

	/**
	 * gets entity depending on parameter passed in the string
	 * 
	 * @param s
	 *            , current users cookies
	 * @param id
	 *            , content will determine
	 * @return retrievedCategories, array of categories matching the entity
	 *         specifications
	 */

	@Override
	public Category[] getEntity(Session s, String id) throws NotFoundException, WPISuiteException
	{
		return getCategoryByID(s, id);
	}

	/**
	 * For now only returns the first category with this specific ID.
	 * 
	 * @param s
	 *            new session
	 * @param id
	 *            given ID number for the category
	 * @return Category[] returns a new Category array of one category matching
	 *         the given UUID
	 * @throws WPISuiteException
	 */
	private Category[] getCategoryByID(Session s, String id) throws WPISuiteException
	{
		List<Category> retrievedCategories = new ArrayList<>();
		Category[] all = getAll(s);
		UUID idVal = UUID.fromString(id);
		for (Category c : all)
		{
			if (c.getUuid().equals(idVal))
			{
				retrievedCategories.add(c);
				return new Category[] { retrievedCategories.get(0) };
			}
		}
		return new Category[] {};
	}

	@Override
	public Category[] getAll(Session s) throws WPISuiteException
	{
		return db.retrieveAll(new Category(), s.getProject()).toArray(new Category[0]);
	}

	@Override
	public Category update(Session session, String content) throws WPISuiteException
	{

		Category updatedCategory = Category.fromJson(content);
		/*
		 * Because of the disconnected objects problem in db4o, we can't just
		 * save Categories. We have to get the original defect from db4o, copy
		 * properties from updatedCategory, then save the original Category
		 * again.
		 */
		List<Model> oldCategories = db.retrieve(Category.class, "uuid", updatedCategory.getUuid(), session.getProject());
		if (oldCategories.size() < 1 || oldCategories.get(0) == null)
		{
			throw new BadRequestException("Category with ID does not exist.");
		}

		Category existingCategory = (Category) oldCategories.get(0);

		db.delete(existingCategory);

		if (!db.save(updatedCategory, session.getProject()))
		{
			throw new WPISuiteException();
		}

		PollPusher.getInstance(Category.class).updated(updated(updatedCategory));

		return updatedCategory;
	}

	@Override
	public void save(Session s, Category model) throws WPISuiteException
	{
		if (model.isProjectCategory())
			model.setProject(s.getProject());
		db.save(model);
		PollPusher.getInstance(Category.class).updated(updated(model));
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException
	{
		boolean res = (db.delete(getEntity(s, id)[0]) != null) ? true : false;
		if (res)
			PollPusher.getInstance(Category.class).updated(deleted(UUID.fromString(id)));
		return res;
	}

	@Override
	public void deleteAll(Session s) throws WPISuiteException
	{
		db.deleteAll(new Category(), s.getProject());

	}

	@Override
	public int Count() throws WPISuiteException
	{
		return db.retrieveAll(new Category()).size();
	}

	@Override
	public String advancedGet(Session s, String[] args) throws WPISuiteException
	{
		// shift cal/events off
		args = Arrays.copyOfRange(args, 2, args.length);
		switch (args[0])
		{
			case "filter-category-by-uuid":
				return json((Object[]) getCategoryByID(s, args[1]));
			case "poll":
				return getFromPoll(s);
			default:
				System.out.println(args[0]);
				throw new NotFoundException("Error: " + args[0] + " not a valid method");
		}
	}

	@Override
	protected String updated(Category e)
	{
		return new Gson().toJson(new Category.SerializedAction(e, e.getUuid(), false));
	}

	@Override
	protected String deleted(UUID id)
	{
		return new Gson().toJson(new Category.SerializedAction(null, id, true));
	}
}
