/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.cal.models.client;

import java.util.List;
import java.util.UUID;

import edu.wpi.cs.wpisuitetng.modules.cal.models.data.Category;

public class CategoryModel extends CachingModel<Category, Category.SerializedAction> {

	private static CategoryModel instance;
	
	/**
	 * private singleton constructor
	 */
	private CategoryModel()
	{
		super("categories", Category.SerializedAction[].class, Category[].class);
	}
	
	/**
	 * 
	 * @return the singleton category model
	 */
	public static CategoryModel getInstance()
	{
		if (instance == null)
		{
			instance = new CategoryModel();
		}
		return instance;
	}

	/**
	 * @return filteredCategories List of all categories from the database
	 */
	public List<Category> getAllCategories()
	{
		return getAll();
	}
	
	/**
	 * 
	 * @param categoryID the ID of the category
	 * @return the category with this ID
	 */
	public Category getCategoryByUUID(UUID categoryID)
	{
		return getByUUID(categoryID);
	}

	@Override
	protected void applySerializedChange(Category.SerializedAction serializedAction)
	{
		//TODO: update lists
	}

	@Override
	protected UUID getUuidFrom(Category obj)
	{
		return obj.getCategoryID();
	}

	@Override
	protected boolean filter(Category obj)
	{
		return true;
	}
}
