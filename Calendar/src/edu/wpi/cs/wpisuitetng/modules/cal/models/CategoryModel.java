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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.wpi.cs.wpisuitetng.modules.cal.ui.main.MainPanel;

public class CategoryModel {

	private static CategoryModel instance;
	private List<Category> categories;
	private Map<UUID, Category> categoryMap = new HashMap<>();
	
	/**
	 * private singleton constructor
	 */
	private CategoryModel()
	{
		this.updateCache();
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
		return categories;
	}

	/**
	 * updates the cash of categories;
	 */
	public void updateCache()
	{
		final List<Category> categories = ServerManager.get("cal/categories/",
				Category[].class, "get-all-categories");

		List<Category> filteredCategories = new ArrayList<Category>();
		boolean showPersonal = MainPanel.getInstance().showPersonal;
		boolean showTeam = MainPanel.getInstance().showTeam;

		for (Category c : categories) {
			if (c.isProjectCategory() && showTeam) {
				filteredCategories.add(c);
			}
			if (!c.isProjectCategory() && showPersonal) {
				filteredCategories.add(c);
			}
		}

		this.categories = filteredCategories;
		this.categoryMap.clear();
		for(Category c : categories)
		{
			this.categoryMap.put(c.getCategoryID(), c);
		}
	}

	/**
	 * always updates the cache
	 * 
	 * @param toAdd
	 * @return boolean if the put request was successful
	 */
	public boolean putCategory(Category toAdd)
	{
		boolean result = ServerManager.put("cal/categories", toAdd.toJSON());
		updateCache();
		return result;
	}
	
	/**
	 * 
	 * @param categoryID the ID of the category
	 * @return the category with this ID
	 */
	public Category getCategoryByUUID(UUID categoryID)
	{
		return this.categoryMap.get(categoryID);
	}
	
	/**
	* @param toUpdate
	* @return boolean if the post request was successful
	*/
	public boolean updateCategory(Category toUpdate)
    {
		boolean result = ServerManager.post("cal/categories", toUpdate.toJSON());
		updateCache();
		return result;
    }
	
	/**
	 * 
	 * @param toRemove the category to delete from db
	 * @return true if delete was successful
	 */
	public boolean deleteCategory(Category toRemove)
	{
		return ServerManager.delete("cal/categories", "get-category-by-id", toRemove.getCategoryID().toString());
	}
}
