package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.awt.Color;
import java.util.UUID;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.User;


/**
 *  Basic categories class that contains all information required to 
 *  designate an event category in the calendar
 *  
 *  @author SarahS Etienne Prateek
 */
public class Category extends AbstractModel
{
	private UUID categoryID = UUID.randomUUID();
	private String name;
	private Color color;
	private User owner;
	private boolean isProjectCategory;
	
	/**
	 * @param name
	 * 		the name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * @return name
	 */
	public String getName()
	{
		return this.name;
	}
	
	/** 
	 * @param color 
	 * 		the color to set the category
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	/**
	 * 
	 * @return color
	 */
	public Color getColor()
	{
		return this.color;
	}
	
	public UUID getUUID()
	{
		return categoryID;
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toJSON() {
		return new Gson().toJson(this, Category.class);
	}

	@Override
	public Boolean identify(Object o) {
		if(o instanceof Category)
		{
			return ((Category) o).toJSON().equals(this.toJSON());
			
		}
		return false;
	}

	public void setOwner(User user) {
		this.owner = user;
	}

	public static Category fromJson(String content) {
		return null;
	}

	public boolean isProjectCategory() {
		return isProjectCategory;
	}

	public Object getCategoryID() {
		return this.categoryID;
	}
}
