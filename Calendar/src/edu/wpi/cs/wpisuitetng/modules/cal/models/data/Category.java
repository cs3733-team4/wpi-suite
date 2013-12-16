/*******************************************************************************
 * Copyright (c) 2013 WPI-Suite
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Team YOCO (You Only Compile Once)
 ******************************************************************************/

package edu.wpi.cs.wpisuitetng.modules.cal.models.data;

import java.awt.Color;
import java.util.UUID;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.cal.models.client.CachingModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;

public class Category extends AbstractModel
{
	private UUID categoryID = UUID.randomUUID();
	private String name;
	private Color color;
	private User owner;
	private boolean isProjectCategory;

	public static final Category DEFAULT_CATEGORY = new Category("Uncategorized");
	public static final Category DEFAULT_DISPLAY_CATEGORY = new Category("No Categories");
	public static final Category COMMITMENT_CATEGORY = new Category("Commitments");
	public static final Category EVENT_CATEGORY = new Category("Events");

	private Category(String s)
	{
		name = s;
		color = null;
		owner = null;
		categoryID = new UUID(0, 0);
	}

	public Category()
	{
	}

	/**
	 * Sets the name of the category
	 * 
	 * @param name
	 *            the name to set to the category
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Gets the name of the category
	 * 
	 * @return name the name of the category
	 */
	public String getName()
	{
		return this.name;
	}

	/**
	 * Sets the color of the category
	 * 
	 * @param color
	 *            the color to set the category
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}

	/**
	 * Gets the color of the category
	 * 
	 * @return color the color of the category
	 */
	public Color getColor()
	{
		return this.color;
	}

	/**
	 * Gets the UUID of the given category
	 * 
	 * @return categoryID the UUID of the given category
	 */
	public UUID getCategoryID()
	{
		return categoryID;
	}

	@Override
	public void save()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void delete()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public String toJSON()
	{
		return new Gson().toJson(this, Category.class);
	}

	@Override
	public Boolean identify(Object o)
	{
		if (o instanceof Category)
		{
			return ((Category) o).toJSON().equals(this.toJSON());

		}
		return false;
	}

	/**
	 * Sets the owner of the category
	 * 
	 * @param user
	 *            tracks the user ID of the person signed into the module
	 */

	public void setOwner(User user)
	{
		this.owner = user;
	}

	/**
	 * Gets the owner of the current category
	 * 
	 * @return owner the owner of the current module
	 */

	public User getOwner()
	{
		return this.owner;
	}

	/**
	 * @param content
	 *            string of information for the category
	 * @return nothing
	 */

	public static Category fromJson(String content)
	{

		final Gson parser = new Gson();
		return parser.fromJson(content, Category.class);
	}

	/**
	 * Checks to see if the given category is a ProjectCategory
	 * 
	 * @return boolean if the category is a ProjectCategory
	 */

	public boolean isProjectCategory()
	{
		return isProjectCategory;
	}

	@Override
	public String toString()
	{
		return this.getName();
	}

	/**
	 * @param categoryID
	 *            the categoryID to set
	 */
	public void setCategoryID(UUID categoryID)
	{
		this.categoryID = categoryID;
	}

	public static class SerializedAction extends CachingModel.SerializedAction<Category>
	{
		public SerializedAction(Category e, UUID eventID, boolean b)
		{
			object = e;
			uuid = eventID;
			isDeleted = b;
		}
	}
}
