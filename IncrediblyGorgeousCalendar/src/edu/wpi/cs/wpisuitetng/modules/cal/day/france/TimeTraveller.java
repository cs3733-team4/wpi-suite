package edu.wpi.cs.wpisuitetng.modules.cal.day.france;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

public class TimeTraveller implements Comparable<TimeTraveller>
{
	private int collisions = 0;
	private Event event;
	private Rational xpos = new Rational(1, 1);
	
	public TimeTraveller(Event event)
	{
		this.event = event;
	}
	
	public int getCollisions()
	{
		return collisions;
	}
	public void setCollisions(int collisions)
	{
		this.collisions = collisions;
	}
	public Rational getXpos()
	{
		return xpos;
	}
	public void setXpos(Rational xpos)
	{
		this.xpos = xpos;
	}
	public Event getEvent()
	{
		return event;
	}

	@Override
	public int compareTo(TimeTraveller toCompare) {
		int res = Integer.compare(toCompare.xpos.toInt(10000), xpos.toInt(10000));
		if (res == 0)
			return toCompare.event.getStart().compareTo(event.getStart());
		return res;
	}
	
}
