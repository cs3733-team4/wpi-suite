package edu.wpi.cs.wpisuitetng.modules.cal.models;

import org.joda.time.DateTime;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Event.RepeatType;

public class EventFactory {

	
	
	/**
	 * 
	 * @return a default event with no values
	 */
	public static Event getDefaultEvent()
	{
		return new Event();
	}
	
	/**
	 * 
	 * @param name the project name
	 * @param description the project description
	 * @param startTime the project start time
	 * @param endTime the project end time
	 * @param isProjectEvent whether this is a project event
	 * @param projectID the ID of the project event
	 * @param userID the ID of the user for the event
	 * @return an Event with all these properties set
	 */
	public static Event getStandardEvent(String name, String description, DateTime startTime, DateTime endTime, boolean isProjectEvent, int projectID, int userID)
	{
		return new Event().addName(name)
				          .addDescription(description)
				          .addStartTime(startTime)
				          .addEndTime(endTime)
				          .addIsProjectEvent(isProjectEvent)
				          .addUserID(userID)
				          .addProjectID(projectID);
	}
	
	
	public static Event getRepeatingEvent(String name, String description, DateTime startTime, DateTime endTime, boolean isProjectEvent, int projectID, int userID,
			                             boolean isRepeating, RepeatType repeats, int repeatEvery, int[] repeatOn, DateTime startRepeat, DateTime endRepeat){
		return EventFactory.getStandardEvent(name, description, startTime, endTime, isProjectEvent, projectID, userID).addIsRepeating(isRepeating)
				                                                                                                      .addRepeats(repeats)
				                                                                                                      .addRepeatEvery(repeatEvery)
				                                                                                                      .addRepeatOn(repeatOn)
				                                                                                                      .addStartRepeat(startRepeat)
				                                                                                                      .addEndRepeat(endRepeat);
	}
}
