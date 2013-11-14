package edu.wpi.cs.wpisuitetng.modules.cal.models;

import org.joda.time.DateTime;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
/**
 * Basic event class that contains the information required to represent an event on a calendar.
 * 
 * @author NileshP
 *
 */
public class Event extends AbstractModel {
	public enum RepeatType {
		Yearly, Monthly, Weekly, Daily, Weekdays, MWF, TR
	}
	private int eventID;
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

	/**
	 * Create an event with the default characteristics.
	 */
	public Event() {
		super();
		eventID = 0;
		name = "";
		description = "";
		setStartTime(new DateTime());
		setEndTime(new DateTime());
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
	/**
	 * Create a non repeating event, no need to pass in the repeating related information.
	 * @param name
	 * @param description
	 * @param startTime
	 * @param endTime
	 * @param isProjectEvent
	 * @param projectID
	 * @param userID
	 */
	public Event(int eventID, String name, String description, DateTime startTime, DateTime endTime, boolean isProjectEvent, int projectID, int userID){
		super();
		this.eventID = eventID;
		this.name = name;
		this.description = description;
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.isProjectEvent = isProjectEvent;
		this.projectID = projectID;
		this.userID = userID;
		this.isRepeating = false;
	}
	/**
	 * Set everything needed by hand, can create a repeating event or a non repeating event.
	 * @param name
	 * @param description
	 * @param startTime
	 * @param endTime
	 * @param isProjectEvent
	 * @param projectID
	 * @param userID
	 * @param isRepeating
	 * @param repeats
	 * @param repeatEvery
	 * @param repeatOn
	 * @param startRepeat
	 * @param endRepeat
	 */
	public Event(int eventID, String name, String description, DateTime startTime, DateTime endTime, boolean isProjectEvent, int projectID, int userID,
			boolean isRepeating, RepeatType repeats, int repeatEvery, int[] repeatOn, DateTime startRepeat, DateTime endRepeat){
		super();
		this.eventID = eventID;
		this.name = name;
		this.description = description;
		this.setStartTime(startTime);
		this.setEndTime(endTime);
		this.isProjectEvent = isProjectEvent;
		this.projectID = projectID;
		this.userID = userID;
		this.isRepeating = isRepeating;
		this.repeats = repeats;
		this.repeatEvery = repeatEvery;
		this.repeatOn = repeatOn;
		this.startRepeat = startRepeat;
		this.endRepeat = endRepeat;
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
	
	public void copyFrom(Event source){
		name = source.getName();
		description = source.getDescription();
		isProjectEvent = source.isProjectEvent();
		isRepeating = source.isRepeating();
		endRepeat = source.getEndRepeat();
		repeatOn = source.getWeeklySchedule();
		projectID = source.getProjectID();
		userID = source.getUserID();
		repeatEvery = source.getRepeatInterval();
		repeats = source.getRepeats();
		startRepeat = source.getStartRepeat();
		
	}
	// Accessor and Mutator Methods:
	
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
	
	public DateTime getStartRepeat(){
		return this.startRepeat;
	}

	public DateTime getEndRepeat(){
		return this.endRepeat;
	}

	public void setName(String name){
		this.name = name;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public void setProjectID(int projectID){
		this.projectID = projectID;
	}
	
	public void setUserID(int userID){
		this.userID = userID;
	}
	
	public void setRepeats(RepeatType repeats){
		this.repeats = repeats;
	}
	
	public void setIsRepeating(boolean isRepeating){
		this.isRepeating = isRepeating;
	}
	
	public void setIsProjectEvent(boolean isProjectEvent){
		this.isProjectEvent = isProjectEvent;
	}
	
	public void setRepeatInterval(int repeatInterval){
		this.repeatEvery = repeatInterval;
	}
	
	public void setWeeklySchedule(int[] repeatOn){
		this.repeatOn = repeatOn;
	}
	
	public void setStartRepeat(DateTime startRepeat){
		this.startRepeat = startRepeat;
	}

	public void setEndRepeat(DateTime endRepeat){
		this.endRepeat = endRepeat;
	}
	
	public int getEventID(){
		return this.eventID;
	}
	
	public void setEventID(int eventID){
		this.eventID = eventID;
	}
	public DateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(DateTime startTime) {
		this.startTime = startTime;
	}
	public DateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(DateTime endTime) {
		this.endTime = endTime;
	}
}