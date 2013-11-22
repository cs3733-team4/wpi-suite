package edu.wpi.cs.wpisuitetng.modules.cal.models;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;


/**
 * This is the entity manager for the Category
 * in the CategoryManager module.
 * @author Prateek, SarahS
 *
 */

public class CategoryEntityManager implements EntityManager<Event> {
	/** The database */
	Data db;
	
	/**
	 * Constructs the entity manager. This constructor is called by
	 * {@link edu.wpi.cs.wpisuitetng.ManagerLayer#ManagerLayer()}. To make sure
	 * this happens, be sure to place add this entity manager to the map in
	 * the ManagerLayer file.
	 * 
	 * @param db a reference to the persistent database
	 */
	public CategoryEntityManager(Data db) {
		this.db = db; 
	}
	
	/**
	 * Saves a Category when it is received from a client
	 * 
	 */
	@Override
	public Category makeEntity(Session s, UUID content) throws WPISuiteException {
		System.out.println(content+ " was just sent!");
		final Category newCategory = Category.fromJson(content);
		newCategory.setOwner(s.getUser());
		newCategory.setProject(s.getProject());
		if(!db.save(newCategory, s.getProject())) {
			throw new WPISuiteException();
		}
		return newCategory;
	}
	
}
