package edu.wpi.cs.wpisuitetng.modules.cal.models;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

public class CommitmentModel {

	public static final DateTimeFormatter serializer = ISODateTimeFormat.basicDateTime();


	public List<Commitment> getCommitments(DateTime from, DateTime to) {
		return ServerManager.get("cal/commitments/", Commitment[].class, 
				"filter-commitments-by-range", 
				from.toString(serializer),
				to.toString(serializer));
	}

	public boolean putCommitment(Commitment toAdd){
		return ServerManager.put("cal/commitments", toAdd.toJSON());
	}
	
	

}
