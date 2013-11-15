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
		setStartTime(new DateTime());
		endTime = new DateTime();
		isRepeating = false;
		isProjectEvent = true;
		userID = 0;
		projectID = 0;
		repeats = null;
		repeatEvery = 0;
		repeatOn = new int[7];
		setStartRepeat(new DateTime());
		setEndRepeat(new DateTime());
	}

	public Event(String name, String description, DateTime startTime, DateTime endTime, boolean isProjectEvent, int projectID, int userID){
		this.name = name;
		this.description = description;
		this.setStartTime(startTime);
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
		this.setStartTime(startTime);
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

	@Override
	public String toJSON() {
		return new Gson().toJson(this, Event.class);
	}

	@Override
	public Boolean identify(Object o) {
		return null;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getDescription(){
		return this.description;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getProjectID(){
		return this.projectID;
	}
	
	public int getUserID(){
		return this.userID;
	}
	
	/**
	 * 
	 * @return
	 */
	public RepeatType getRepeats(){
		return this.repeats;
	}
	
	public boolean isRepeating(){
		return this.isRepeating;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isProjectEvent(){
		return this.isProjectEvent;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getRepeatInterval(){
		return this.repeatEvery;
	}
	
	public int[] getWeeklySchedule(){
		return this.repeatOn;
	}
	
	/**
	 * 
	 * @return
	 */
	public DateTime getEndTime() {
		return endTime;
	}

	/**
	 * 
	 * @param endTime
	 */
	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}
	
	/**
	 * 
	 * @return
	 */
	public DateTime getStartTime() {
		return startTime;
	}
	
	/**
	 * 
	 * @param startTime
	 */
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}
	
	/**
	 * 
	 * @return
	 */
	public DateTime getStartRepeat() {
		return startRepeat;
	}
	
	/**
	 * 
	 * @param startRepeat
	 */
	public void setStartRepeat(DateTime startRepeat) {
		this.startRepeat = startRepeat;
	}

	/**
	 * 
	 * @return
	 */
	public DateTime getEndRepeat() {
		return endRepeat;
	}
	
	/**
	 * 
	 * @param endRepeat
	 */
	public void setEndRepeat(DateTime endRepeat) {
		this.endRepeat = endRepeat;
	}

	public DateTime start;
	
}