package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.util.UUID;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.ConflictException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;


/**
 * This is the entity manager for the Category
 * in the CategoryManager module.
 * @author Prateek, SarahS, Etienne
 *
 */

public class CategoryEntityManager implements EntityManager<Category> {
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
	public Category makeEntity(Session s, String content) throws WPISuiteException {
		System.out.println(content+ " was just sent!");
		final Category newCategory = Category.fromJson(content);
		newCategory.setOwner(s.getUser());
		newCategory.setProject(s.getProject());
		if(!db.save(newCategory, s.getProject())) {
			throw new WPISuiteException();
		}
		return newCategory;
	}

	@Override
	public Category[] getEntity(Session s, String id) throws NotFoundException,
			WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category[] getAll(Session s) throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Category update(Session s, String content) throws WPISuiteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Session s, Category model) throws WPISuiteException {
		if (model.isProjectCategory())
			model.setProject(s.getProject());
		db.save(model);
		
	}

	@Override
	public boolean deleteEntity(Session s, String id) throws WPISuiteException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deleteAll(Session s) throws WPISuiteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int Count() throws WPISuiteException {
		return db.retrieveAll(new Category()).size();
	}

	@Override
	public String advancedGet(Session s, String[] args) throws NotImplementedException {
		throw new NotImplementedException();
	}
	
	@Override
	public String advancedPut(Session s, String[] args, String content) throws NotImplementedException {
		throw new NotImplementedException();
	}

	@Override
	public String advancedPost(Session s, String string, String content) throws NotImplementedException {
		throw new NotImplementedException();
	}
	
}
