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
 * 
 * @author SarahS, Etienne
 *
 */

public class CategoryModel {
	public List<Category> getAllCategory() {
		final List<Category> categories = ServerManager.get("cal/categories/", Category[].class, null); //Need to create directory in database
		
		List<Category> filteredCategories = new ArrayList<Category>();
		boolean showPersonal = MainPanel.getInstance().showPersonal; // Where do these go?
		boolean showTeam = MainPanel.getInstance().showTeam; // Mainpanel replacement?
		
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
	
	public boolean putCategory(Category toAdd){
		return ServerManager.put("cal/categories/", toAdd.toJSON());
	}
}
	
