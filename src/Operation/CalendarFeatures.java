package Operation;

import java.rmi.server.ExportException;
import java.util.Calendar;

public class CalendarFeatures {

	private static int numDays;
	
	public static int getdayscount(int MonthNum) throws ExportException
	{
		try
			{
				Calendar cal = Calendar.getInstance();
				int Year = cal.get(Calendar.YEAR);
			    if (MonthNum == 2)
				    {
					     if ( (Year % 4 == 0) && (Year % 400 == 0) && !(Year % 100 == 0) )
					            numDays = 29;
					     else
					            numDays = 28;
				    }
	
			    else if (MonthNum == 1 || MonthNum == 3 || MonthNum == 5 || MonthNum == 7 || MonthNum == 8 || MonthNum == 10 || MonthNum == 12)
					{
				    	numDays = 31;
					}
	
			    else 
				    {
				    	numDays = 30;
				    }
			    return numDays;
			}
		catch (Exception e) 
			{
				System.out.println("Cannot get the day count:" + e);
			}
			return numDays;
	}
	
	public static int getcurrentmonth()
		{
			Calendar cal = Calendar.getInstance();
			int MonthNum = cal.get(Calendar.MONTH)+1;
			return MonthNum;
			    
		}
	
	public static int getcurrentdate()
		{
			Calendar cal = Calendar.getInstance();
			int fromdate = cal.get(Calendar.DAY_OF_MONTH);
			return fromdate;
		}
	
	public static int getdayofweek()
	{
		Calendar cal = Calendar.getInstance();
		int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
		return dayofweek;
	}
	
		
}
