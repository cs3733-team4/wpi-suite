package edu.wpi.cs.wpisuitetng.modules.cal.models.google;

import java.io.IOException;

import com.google.gdata.util.ServiceException;

import edu.wpi.cs.wpisuitetng.modules.cal.models.Displayable;

/**
 * 
 * not used much any more, since we have generally decided to not push back to google
 *
 */
public class DisplayableSyncer implements Runnable {

	private GoogleSync gs;
	private Displayable d;
	
	/**
	 * 
	 * @param gs the GoogleSync object
	 * @param d the displayable to add to google
	 */
	public DisplayableSyncer(GoogleSync gs, Displayable d)
	{
		this.d = d;
		this.gs = gs;
		
		if (gs != null)
		{
			new Thread(this).start();
		}
		
		System.out.println("gonna sync event!");
	}
	
	@Override
	public void run() {
		try {
			gs.addEventToCalendar(d);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ServiceException e) {
			e.printStackTrace();
		}
	}

}
