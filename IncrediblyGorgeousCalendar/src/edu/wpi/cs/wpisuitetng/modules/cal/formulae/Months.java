package edu.wpi.cs.wpisuitetng.modules.cal.formulae;

public class Months {
	
	/**
	 * 
	 * @param year the year to test
	 * @return whether the year provided is a leap year
	 */
	public static boolean isLeapYear(int year)
	{
		if (year%4 != 0)
			return false;
		if (year%400 == 0)
			return true;
		if (year%100 == 0)
			return false;
		return true;
	}
	
	/**
	 * 
	 * @param month the month's number (jan = 1, dec = 12)
	 * @param year
	 * @return the number of days in a given month in a given year
	 */
	public static int getDaysInMonth(int month, int year)
	{
		if (month == 2)
			return 28 + (isLeapYear(year)?1:0);
		
		if ((month == 4) || (month == 6) || (month == 9) || (month == 11))
			return 30;
		
		else
			return 31;
	}
	
	/**
	 * 
	 * @param year they year
	 * @param month the month
	 * @param day the day
	 * @return on which day of the week this date falls, ie:
	 * 		1-sundae (mmmmm)
	 * 		2-monday
	 */
	public static int getDayOfMonth(int year, int month, int day)
	{
		if (month < 3)
		{
			month+=12;
			year--;
		}
		int d = (day + 2*month + (3*(month+1)/5) + year + (year/4) - (year/100) + (year/400) + 2)%7;
		return (d==0?7:d);
		
	}
	
	/**
	 * 
	 * @param year the year
	 * @param month the month
	 * @return the day (same format as getDayOfMonth)
	 */
	public static int getStartingDay(int year, int month)
	{
		return Months.getDayOfMonth(year, month, 1);
	}
	
	/**
	 * 
	 * @param monthNumber the number of the month
	 * @return the name of the month
	 */
	public static String getMonthName(int monthNumber)
	{
		switch (monthNumber){
			case 1:
				return "January";
			case 2:
				return "February";
			case 3:
				return "March";
			case 4:
				return "April";
			case 5:
				return "May";
			case 6:
				return "June";
			case 7:
				return "July";
			case 8:
				return "August";
			case 9:
				return "September";
			case 10:
				return "October";
			case 11:
				return "November";
			case 12:
				return "December";
		}
		
		return "Milnisium";
	}
}
