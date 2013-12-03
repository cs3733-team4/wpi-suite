package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.util.ArrayList;
import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;

public class CategoryModel {
                
        /**
         * @return filteredCategories List of all categories from the database
         */
        public List<Category> getAllCategories() {
                final List<Category> categories = ServerManager.get("cal/categories/", Category[].class, "get-all-categories");
                
                List<Category> filteredCategories = new ArrayList<Category>();
                boolean showPersonal = MainPanel.getInstance().showPersonal;
                boolean showTeam = MainPanel.getInstance().showTeam;
                
                for(Category c: categories){
                        if(c.isProjectCategory()&&showTeam){
                                filteredCategories.add(c);
                        }
                        if(!c.isProjectCategory()&&showPersonal){
                                filteredCategories.add(c);
                        }
                }
                
                return filteredCategories;
        }
        
        /**
         * @param toAdd
         * @return boolean if the put request was successful
         */
        public boolean putCategory(Category toAdd){
                return ServerManager.put("cal/categories", toAdd.toJSON());
        }
        
        /**
         * @param toUpdate
         * @return boolean if the post request was succesful
         */
    	public boolean updateCategory(Category toUpdate)
    	{
    		return ServerManager.post("cal/categories", toUpdate.toJSON());
    	}
}
