package edu.wpi.cs.wpisuitetng.modules.cal.models;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.Session;
import edu.wpi.cs.wpisuitetng.modules.cal.MainPanel;
import edu.wpi.cs.wpisuitetng.network.Network;
import edu.wpi.cs.wpisuitetng.network.Request;
import edu.wpi.cs.wpisuitetng.network.RequestObserver;
import edu.wpi.cs.wpisuitetng.network.models.HttpMethod;
import edu.wpi.cs.wpisuitetng.network.models.IRequest;


/**
 * @author sarahsawatzki, Etienne, Alex
 *List of categories being pulled from the server.
 *
 */

public class CategoryModel {
		
	/**
	 * @return filteredCategories List of all categories from the database
	 */
	public List<Category> getAllCategory() {
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
}
	
