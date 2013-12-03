package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;

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
	
	public Category getCategoryByUUID(UUID categoryID)
	{
		return this.categoryMap.get(categoryID);
	}
}
