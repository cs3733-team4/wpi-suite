package edu.wpi.cs.wpisuitetng.modules.cal.models;

import org.joda.time.DateTime;

public interface Displayable {
	public String getName();
	public String getDescription();
	public String getParticipants();
	public DateTime getDate();
}
