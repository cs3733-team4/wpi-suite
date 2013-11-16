package edu.wpi.cs.wpisuitetng.modules.cal.models;

import org.joda.time.DateTime;

import com.google.gson.Gson;

import edu.wpi.cs.wpisuitetng.modules.AbstractModel;
import edu.wpi.cs.wpisuitetng.modules.core.models.Project;

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
	private Project project;
	// For Handling Repeating Events:
	private RepeatType repeats;
	private int repeatEvery;
	private int[] repeatOn;
	private DateTime startRepeat;
	private DateTime endRepeat;

	
	
	/**
	 * 
	 * @param name the name of the event
	 * @return this event after having it's name set
	 */
	public Event addName(String name)
	{
		this.name = name;
		return this;
	}

	/**
	 * 
	 * @param description the event's description
	 * @return this event after having it's description set
	 */
	public Event addDescription(String description)
	{
		this.description = description;
		return this;
	}
	
	/**
	 * 
	 * @param date the starting time
	 * @return this event after having its start date set
	 */
	public Event addStartTime(DateTime date)
	{
		this.startTime = date;
		return this;
	}
	
	/**
	 * 
	 * @param date the end time of this event
	 * @return this event after having it's end time set
	 */
	public Event addEndTime(DateTime date)
	{
		this.endTime = date;
		return this;
	}
	
	/**
	 * 
	 * @param rep whether this event is repeating
	 * @return this event after having it's repeating flag set
	 */
	public Event addIsRepeating(boolean rep)
	{
		this.isRepeating = rep;
		return this;
	}
	
	/**
	 * 
	 * @param pe whether this is a project event
	 * @return this event after having it's project flag set
	 */
	public Event addIsProjectEvent(boolean pe)
	{
		this.isProjectEvent = pe;
		return this;
	}
	
	/**
	 * 
	 * @param user a user ID
	 * @return this event after having it's user ID set
	 */
	public Event addUserID(int user)
	{
		this.userID = user;
		return this;
	}
	
	/**
	 * 
	 * @param project the project ID for this event
	 * @return this event after having it's project ID set
	 */
	public Event addProjectID(Project project)
	{
		this.project = project;
		return this;
	}
	
	/**
	 * 
	 * @param repeats the RepeatType of this event
	 * @return this event after having it's repeat type set
	 */
	public Event addRepeats(RepeatType repeats)
	{
		this.repeats = repeats;
		return this;
	}
	
	/**
	 * 
	 * @param repEvery the repeat every count (ie every other, third, or fourth day)
	 * @return this event after having it's repeat every count set
	 */
	public Event addRepeatEvery(int repEvery)
	{
		this.repeatEvery = repEvery;
		return this;
	}
	
	/**
	 * 
	 * @param repOn the array of days that this event repeats on
	 * @return this event after having it's repeatOn field set
	 */
	public Event addRepeatOn(int[] repOn)
	{
		this.repeatOn = repOn;
		return this;
	}
	
	/**
	 * 
	 * @param startRep the Date to start repeating
	 * @return this event after having its repeat start date set
	 */
	public Event addStartRepeat(DateTime startRep)
	{
		this.startRepeat = startRep;
		return this;
	}
	
	/**
	 * 
	 * @param endRep the day to end this event's repeating cycle
	 * @return this event after having it's end repeat date set
	 */
	public Event addEndRepeat(DateTime endRep)
	{
		this.endRepeat = endRep;
		return this;
	}
	
	
	public Event() {
		super();
		name = "";
		description = "";
		setStartTime(new DateTime());
		endTime = new DateTime();
		isRepeating = false;
		isProjectEvent = true;
		userID = 0;
		project = null;
		repeats = null;
		repeatEvery = 0;
		repeatOn = new int[7];
		setStartRepeat(DateTime.now());
		setEndRepeat(DateTime.now());
	}

	public Event(String name, String description, DateTime startTime, DateTime endTime, boolean isProjectEvent, Project projectID, int userID){
		this.name = name;
		this.description = description;
		this.setStartTime(startTime);
		this.endTime = endTime;
		this.isProjectEvent = isProjectEvent;
		this.project = projectID;
		this.userID = userID;
		this.isRepeating = false;
	}
	
	public Event(String name, String description, DateTime startTime, DateTime endTime, boolean isProjectEvent, Project projectID, int userID,
			boolean isRepeating, RepeatType repeats, int repeatEvery, int[] repeatOn, DateTime startRepeat, DateTime endRepeat){
		this.name = name;
		this.description = description;
		this.setStartTime(startTime);
		this.endTime = endTime;
		this.isProjectEvent = isProjectEvent;
		this.project = projectID;
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
	public Project getProjectID(){
		return this.project;
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