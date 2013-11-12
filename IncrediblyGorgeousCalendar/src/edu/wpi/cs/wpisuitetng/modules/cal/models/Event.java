package edu.wpi.cs.wpisuitetng.modules.cal.models;

import org.joda.time.DateTime;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;

public class Event extends AbstractModel {
	public enum RepeatType {
		Yearly, Monthly, Weekly, Daily, Weekdays, MWF, TR
	}

	private String name;
	private String description;
	private DateTime startTime;
	private DateTime endTime;
	private boolean isRepeating;
	private boolean isProjectEvent;
	private int userID;
	private int projectID;
	// For Handling Repeating Events:
	private RepeatType repeats;
	private int repeatEvery;
	private int[] repeatOn;
	private DateTime startRepeat;
	private DateTime endRepeat;

	public Event() {
		super();
		name = "";
		description = "";
		startTime = new DateTime();
		endTime = new DateTime();
		isRepeating = false;
		isProjectEvent = true;
		userID = 0;
		projectID = 0;
		repeats = null;
		repeatEvery = 0;
		repeatOn = new int[7];
		startRepeat = new DateTime();
		endRepeat = new DateTime();
	}

	public Event(String name, String description, DateTime startTime, DateTime endTime, boolean isProjectEvent, int projectID, int userID){
		this.name = name;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isProjectEvent = isProjectEvent;
		this.projectID = projectID;
		this.userID = userID;
		this.isRepeating = false;
	}
	
	public Event(String name, String description, DateTime startTime, DateTime endTime, boolean isProjectEvent, int projectID, int userID,
			boolean isRepeating, RepeatType repeats, int repeatEvery, int[] repeatOn, DateTime startRepeat, DateTime endRepeat){
		this.name = name;
		this.description = description;
		this.startTime = startTime;
		this.endTime = endTime;
		this.isProjectEvent = isProjectEvent;
		this.projectID = projectID;
		this.userID = userID;
		this.isRepeating = false;
	}
	
	public static Event fromJson(String json) {
		final Gson parser = new Gson();
		return parser.fromJson(json, Event.class);
	}

	@Override
	public void save() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete() {
		// TODO Auto-generated method stub
		
	}


	public String toJSON() {
		return new Gson().toJson(this, Event.class);
	}

	@Override
	public Boolean identify(Object o) {
		return null;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public int getProjectID(){
		return this.projectID;
	}
	
	public int getUserID(){
		return this.userID;
	}
	
	public RepeatType getRepeats(){
		return this.repeats;
	}
	
	public boolean isRepeating(){
		return this.isRepeating;
	}
	
	public boolean isProjectEvent(){
		return this.isProjectEvent;
	}
	
	public int getRepeatInterval(){
		return this.repeatEvery;
	}
	
	public int[] getWeeklySchedule(){
		return this.repeatOn;
	}
	
	public DateTime start
	
}