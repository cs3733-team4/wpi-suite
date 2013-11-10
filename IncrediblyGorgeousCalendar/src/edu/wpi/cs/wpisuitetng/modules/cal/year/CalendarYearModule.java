package edu.wpi.cs.wpisuitetng.modules.cal.year;

import javax.swing.JComponent;

public interface CalendarYearModule {
	/**
	 * 
	 * @return the previous month
	 */
	public CalendarYearModule getPrevious();
	
	/**
	 * 
	 * @return the next month
	 */
	public CalendarYearModule getFollowing();
	
	/**
	 * 
	 * @param depth the depth that the months will be preloaded to
	 * @return whether this was successful
	 */
	public void preloadPrevious(int depth);
	
	/**
	 * 
	 * @param depth the depth that the months will be preloaded to
	 * @return whether this was successful
	 */
	public void preloadFollowing(int depth);
	
	/**
	 * 
	 * @return
	 */
	public JComponent renderComponent();
	
}
