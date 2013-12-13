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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.database.Data;
import edu.wpi.cs.wpisuitetng.exceptions.BadRequestException;
import edu.wpi.cs.wpisuitetng.exceptions.NotFoundException;
import edu.wpi.cs.wpisuitetng.exceptions.NotImplementedException;
import edu.wpi.cs.wpisuitetng.exceptions.WPISuiteException;
import edu.wpi.cs.wpisuitetng.modules.EntityManager;
import edu.wpi.cs.wpisuitetng.modules.Model;

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
         * @param s session
         * @param content string of what is actually being created
         * @return newCategory newly created entity of category
         */
        @Override
        public Category makeEntity(Session s, String content) throws WPISuiteException {
                final Category newCategory = Category.fromJson(content);
                newCategory.setOwner(s.getUser());
                newCategory.setProject(s.getProject());
                if(!db.save(newCategory, s.getProject())) {
                        throw new WPISuiteException();
                }
                return newCategory;
        }

        /**
         * gets entity depending on parameter passed in the string
         * @param s, current users cookies
         * @param id, content will determine
         * @return retrievedCategories, array of categories matching the entity specifications
         */
        
        @Override
        public Category[] getEntity(Session s, String id) throws NotFoundException, WPISuiteException {

                String[] args = id.split(",");
                
                Category[] retrievedCategories = null;
                
                switch (args[0]) {
                        case "get-all-categories":
                                return getAll(s);
                        case "get-user-categories":
                                return getUserCategories(s);
                        case "get-team-categories":
                                return getTeamCategories(s);
                        case "get-category-by-name":
                                return getCategoryByName(s, args[1]);        
                        case "get-category-by-id":
                                return getCategoryByID(s, args[1]);
                        case "get-category-by-color":
                                return getCategoryByColor(s, args[1]);
                        default:
                                System.out.println("Error: " + args[0] + " not a valid method");                        
                }
                
                return retrievedCategories;
        }
        
        /**
         * Returns an array of category exclusive to the user.
         * Does not do any user verification.
         * @param s
         * @return the user categories
         * @throws WPISuiteException
         */
        private Category[] getUserCategories(Session s) throws WPISuiteException
        {
                List<Category> retrievedCategories = new ArrayList<>();
                Category[] all = getAll(s);
                
                for (Category c : all)
                {
                        if (!c.isProjectCategory())
                        {
                                retrievedCategories.add(c);
                        }
                }
                Category[] userCategories = (Category[]) retrievedCategories.toArray();
                return userCategories;
        }
        /**
         * Returns an array of team categories
         * @param s
         * @return the team categories
         * @throws WPISuiteException
         */
        private Category[] getTeamCategories(Session s) throws WPISuiteException
        {
                List<Category> retrievedCategories = new ArrayList<>();
                Category[] all = getAll(s);
                
                for (Category c : all)
                {
                        if (c.isProjectCategory())
                        {
                                retrievedCategories.add(c);
                        }
                }
                Category[] teamCategories = (Category[]) retrievedCategories.toArray();
                return teamCategories;
        }
        /**For now, only return the first category it finds with a matching name.
         * Currently have not decided how to approach categories with matching names.
         * If a matching name is not there, returns a blank array.
         *
         * @param s session
         * @param name name given for the desired category
         * @return Category[] returns a new Category array of one category matching the given color
         * @throws WPISuiteException
         */
        private Category[] getCategoryByName(Session s, String name) throws WPISuiteException {
                List<Category> retrievedCategories = new ArrayList<>();
                
                Category[] all = getAll(s);
                
                for(Category c: all)
                {
                        if(c.getName() == name){
                                retrievedCategories.add(c);
                                return new Category[] {retrievedCategories.get(0)};
                        }        
                }
                
                return new Category[] {};
                                
        }
        /**
         * For now only returns the first category with this specific ID.
         * @param s new session
         * @param id given ID number for the category
         * @return Category[] returns a new Category array of one category matching the given UUID
         * @throws WPISuiteException
         */
        private Category[] getCategoryByID(Session s, String id) throws WPISuiteException
        {
                List<Category> retrievedCategories = new ArrayList<>();
                Category[] all = getAll(s);
                UUID idVal = UUID.fromString(id);
                for(Category c: all)
                {
                        if (c.getCategoryID().equals(idVal))
                        {
                                retrievedCategories.add(c);
                                return new Category[] {retrievedCategories.get(0)};
                        }
                }
                return new Category[] {};
        }
        /**
         * For now returns the first Category with the matching color.
         * Colors must be passed in RGB format.
         * @param s session
         * @param color color that the category must match
         * @return Category[] returns a new Category array of one category matching the given color
         * @throws WPISuiteException
         */
        private Category[] getCategoryByColor(Session s, String color) throws WPISuiteException
        {
                List<Category> retrievedCategories = new ArrayList<>();
                Category[] all = getAll(s);
                
                for(Category c: all)
                {
                        int rgbVal = Integer.parseInt(color);
                        
                        if (c.getColor() == new Color(rgbVal))
                        {
                                retrievedCategories.add(c);
                                return new Category[] {retrievedCategories.get(0)};
                        }
                }
                return new Category[] {};
        }
        
        

        @Override
        public Category[] getAll(Session s) throws WPISuiteException {
                return db.retrieveAll(new Category(), s.getProject()).toArray(new Category[0]);
        }

        @Override
        public Category update(Session session, String content) throws WPISuiteException {
                
                Category updatedCategory = Category.fromJson(content);
                /*
                 * Because of the disconnected objects problem in db4o, we can't just save Categories.
                 * We have to get the original defect from db4o, copy properties from updatedCategory,
                 * then save the original Category again.
                 */
                List<Model> oldCategories = db.retrieve(Category.class, "categoryID", updatedCategory.getCategoryID(), session.getProject());
                if(oldCategories.size() < 1 || oldCategories.get(0) == null) {
                        throw new BadRequestException("Category with ID does not exist.");
                }
                                
                Category existingCategory = (Category)oldCategories.get(0);                

                
                db.delete(existingCategory);
                
                if(!db.save(updatedCategory, session.getProject())) {
                        throw new WPISuiteException();
                }
                
                return updatedCategory;
        }

        @Override
        public void save(Session s, Category model) throws WPISuiteException {
                if (model.isProjectCategory())
                        model.setProject(s.getProject());
                db.save(model);
                
        }

        @Override
        public boolean deleteEntity(Session s, String id) throws WPISuiteException {
        		System.err.println(getEntity(s, id).length);
        		System.err.println(id);
                return (db.delete(getEntity(s, id)[0]) != null) ? true : false;
        }

        @Override
        public void deleteAll(Session s) throws WPISuiteException {
                db.deleteAll(new Event(), s.getProject());
                
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

