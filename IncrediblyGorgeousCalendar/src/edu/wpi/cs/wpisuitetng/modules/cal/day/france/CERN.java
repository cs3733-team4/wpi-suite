package edu.wpi.cs.wpisuitetng.modules.cal.day.france;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Event;

/**
 * Takes events, converts them to particles and then COLLIDES them.
 * @author NileshP
 *
 */

// Lead Particles => ColliderItems
// TimeTravellers => RankedEvents
// Rational => Rational
// VanGoghPanels => EventPanels
// DayGrid => Louvre

public class CERN {
	public static List<VanGoghPainting> createEventsReallyNicely(List<Event> events){
		LeadParticle[] particles = eventsToParticles(events);
		List<TimeTraveller> travellers = tevatrize(particles); // shoot at speed of light to go back in time
		disperse(particles);
		Collections.sort(travellers); // Currently travelling backwards, trying to sort things out.
		return timeWarp(travellers);
	}
	
	private static LeadParticle[] eventsToParticles(List<Event> events){
		LeadParticle re[] = new LeadParticle[events.size()*2];
		for(int i = 0; i < events.size(); i++){
			re[i*2] = new LeadParticle(events.get(i), false);
			re[i*2 + 1] = new LeadParticle(events.get(i), true);
		}
		Arrays.sort(re);
		return re;
	}
	
	private static List<TimeTraveller> tevatrize(LeadParticle[] particles){
		int counter = -1;
		List<TimeTraveller> out = new ArrayList<TimeTraveller>(particles.length/2);
		HashMap<Event, TimeTraveller> active = new HashMap<Event, TimeTraveller>();
		
		for(LeadParticle c : particles)
		{
			if(!c.isEnd())
			{
				active.put(c.getEvent(), c.setResult(new TimeTraveller(c.getEvent())));
				counter++;
				// max active
				for (TimeTraveller who : active.values()) {
					who.setCollisions(Math.max(who.getCollisions(), counter));
				}
			}
			else
			{
				TimeTraveller who = active.remove(c.getEvent());
				out.add(who);
				c.setResult(who);
				counter--;
			}
		}
		return out;
		// TODO Sort?
	}
	
	private static void disperse(LeadParticle[] particles)
	{
		// TODO: particles must be sorted by start time
		ArrayList<Boolean> state = new ArrayList<>();
		for (LeadParticle x : particles)
		{
			if (!x.isEnd())
			{
				int i = 0;
				boolean foundPos = false;
				for (; i < state.size(); i++)
				{
					if (!state.get(i))
					{
						state.set(i, true);
						foundPos = true;
						break;
					}
				}
				x.getResult().setXpos(new Rational(i, 1 + x.getResult().getCollisions()));
				if (!foundPos)
				{
					state.add(true);
				}
			}
			else
			{
				state.set(x.getResult().getXpos().getNumerator(), false);
			}
		}
	}
	
	/**
	 * Let the time travellers steal painting from past
	 * @param travellers
	 * @return
	 */
	private static List<VanGoghPainting> timeWarp(List<TimeTraveller> travellers)
	{
		List<VanGoghPainting> paintings = new ArrayList<>(travellers.size());
		for(TimeTraveller t : travellers)
		{
			paintings.add(new VanGoghPainting(t));
		}
		return paintings;
	}
	
}
