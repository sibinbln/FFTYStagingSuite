package TestCases.CRM;

import java.util.Properties;

import Operation.ExecutionStatusChecker;
import Operation.Launchbrowser;
import Operation.ReadExcel;
import Operation.ReadObjectRepository;
import Operation.SeleniumOperation;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import Operation.CalendarFeatures;

public class VacationCreate 
{
	static WebDriver webdriver;
	static Properties allObjects;
	static SeleniumOperation operation;
	String className = getClass().getName();
		
	@BeforeClass
	public void ExecutionStatusCheck() throws Exception
	{
		boolean status = ExecutionStatusChecker.getExecutionStatus(className);
		ExecutionStatusChecker.executetestcase(className, status);
		webdriver=Launchbrowser.launchBrowser();
		ReadObjectRepository object = new ReadObjectRepository();
		allObjects = object.getObject();
		operation = new SeleniumOperation(webdriver);
	}
	
	@Test
	public void testVacationCreate() throws Exception 
	{
		
		ReadExcel re = new ReadExcel();
		String xllocation = System.getProperty("user.dir")+"\\TestData\\TestData.xls";
		re.setInputFile(xllocation, 0);
		String[][] data = re.readFile();
		
		re.setInputFile(xllocation, 5);
		String[][] values = re.readFile();
		
		try
		{	
			//Getting input value
			//String location = System.getProperty("user.dir")+"\\TestData\\Vacation.txt";
			//String input = ReadTextFile.Readdata(location);
			int datediff = Integer.parseInt(values[0][0]);
			
			//Setting values
			int fromdate = CalendarFeatures.getcurrentdate();
			int todate = fromdate + datediff;
			int temp = datediff;
			int MonthNum = CalendarFeatures.getcurrentmonth();
			int CurrentMonthnumDays = CalendarFeatures.getdayscount(MonthNum);
	        int daysdiff = CurrentMonthnumDays - fromdate;
	        int NextMonthNum;
	        int NexttoNextMonthNum;
	        
		        if (MonthNum == 12)
			        {
			        	MonthNum = 0;
			        	NextMonthNum = MonthNum + 1;
			        	NexttoNextMonthNum = MonthNum + 2;
			        }
		        
		        else if (MonthNum == 11)
			        {
			        	NextMonthNum = MonthNum + 1;
			        	MonthNum = 0;
			        	NexttoNextMonthNum = MonthNum + 1;
			        }
		        
		        else
			        {
			        	NextMonthNum = MonthNum + 1;
			        	NexttoNextMonthNum = MonthNum + 2;
			        }
		        
	        int NextMonthnumDays = CalendarFeatures.getdayscount(NextMonthNum);
	        int firstcheck = NextMonthnumDays + daysdiff;
	        int NexttoNextMonthnumDays = CalendarFeatures.getdayscount(NexttoNextMonthNum);
	        int secondcheck = NextMonthnumDays + NexttoNextMonthnumDays + daysdiff;
	       
	        
	        //----operation.perform(allObjects, Keyword, ObjectName, Object Type, Value)----
			operation.execute(allObjects, "ACCESSURL", "", "", data[0][2]);
			operation.execute(allObjects, "SENDKEYS", "USERNAME", "name", data[1][2]);
			operation.execute(allObjects, "SENDKEYS", "PASSWD", "name", data[2][2]);
			operation.execute(allObjects, "CLICK", "LOGIN", "id", "");
			operation.execute(allObjects, "CLICK", "CUSTOMERLINK", "link", "");
			
			//Getting customer email from magento DB
			String query = "SELECT ce.email FROM customer_entity ce, customer_entity_varchar cev WHERE ce.entity_id = cev.entity_id AND ce.is_active = 1 AND cev.attribute_id = 235 AND cev.value <> 1 AND ce.email <> 'NULL' ORDER BY ce.email ASC LIMIT 1";
			String cust= SeleniumOperation.querydb(query, "brandywine_magento");
			System.out.println("Setting vacation for customer: "+cust);	
			
			operation.execute(allObjects, "SENDKEYS", "COL1ROW4", "css", "Email");
			operation.execute(allObjects, "SENDKEYS", "COL2ROW4", "css", "equals");
			operation.execute(allObjects, "SENDKEYS", "COL3ROW4", "css", cust);
			operation.execute(allObjects, "CLICK", "SEARCHNOW", "css", "");
			
			//Checking the status of recent two orders
			String Orderstatus1 = operation.execute(allObjects, "GETTEXT", "OrderStatus1", "xpath", "");
			String Orderstatus2 = operation.execute(allObjects, "GETTEXT", "OrderStatus2", "xpath", "");
					
			if (Orderstatus1.equals("Out for Harvest") | Orderstatus1.equals("Out for Delivery") | Orderstatus2.equals("Out for Harvest") | Orderstatus2.equals("Out for Delivery"))
				{
					System.out.println("Cannot set vacation, customer has a order to deliver in next 48 hrs");
					webdriver.quit();
				}
			
			//Navigating to Vacation Hold page
			operation.execute(allObjects, "CLICK", "ClickVacationLink", "xpath", "");	
			operation.execute(allObjects, "CLICK", "FromDate", "xpath", "");
			String calendarXpathFD = "//td[not(contains(@class,'ui-datepicker-days-cell-over ui-datepicker-today'))]/a[text()='"+fromdate+"']";
			operation.executedynamicobject("CLICK", calendarXpathFD, "xpath", "");
			
			Thread.sleep(2000);
			
			//Checking and selecting the dates based on the input provided
			if (todate > CurrentMonthnumDays)
				{	
					if (datediff > daysdiff && datediff <= firstcheck)
							{
								datediff = datediff - daysdiff;
													
								operation.execute(allObjects, "CLICK", "ToDate", "xpath", "");
								operation.execute(allObjects, "CLICK", "NextMonth", "xpath", "");
								String calendarXpathTD = "//td[not(contains(@class,'ui-datepicker-days-cell-over ui-datepicker-today'))]/a[text()='"+datediff+"']";
								operation.executedynamicobject("CLICK", calendarXpathTD, "xpath", "");
							}
					else if (datediff <= daysdiff)
							{
								operation.execute(allObjects, "CLICK", "ToDate", "xpath", "");
								String calendarXpathTD = "//td[not(contains(@class,'ui-datepicker-days-cell-over ui-datepicker-today'))]/a[text()='"+todate+"']";
								operation.executedynamicobject("CLICK", calendarXpathTD, "xpath", "");
							}
					else if (datediff > firstcheck && datediff <= secondcheck)
							{
								operation.execute(allObjects, "CLICK", "ToDate", "xpath", "");
								operation.execute(allObjects, "CLICK", "NextMonth", "xpath", "");
								Thread.sleep(2000);
								operation.execute(allObjects, "CLICK", "NextMonth", "xpath", "");
								Thread.sleep(2000);
								datediff =  datediff - (NextMonthnumDays + daysdiff);
								String calendarXpathTD = "//td[not(contains(@class,'ui-datepicker-days-cell-over ui-datepicker-today'))]/a[text()='"+datediff+"']";
								operation.executedynamicobject("CLICK", calendarXpathTD, "xpath", "");
							}
					else if (datediff > secondcheck)
							{
								operation.execute(allObjects, "CLICK", "ToDate", "xpath", "");
								operation.execute(allObjects, "CLICK", "NextMonth", "xpath", "");
								Thread.sleep(2000);
								operation.execute(allObjects, "CLICK", "NextMonth", "xpath", "");
								Thread.sleep(2000);
								operation.execute(allObjects, "CLICK", "NextMonth", "xpath", "");
								Thread.sleep(2000);
								datediff =  datediff - (NextMonthnumDays + NexttoNextMonthnumDays + daysdiff);
								String calendarXpathTD = "//td[not(contains(@class,'ui-datepicker-days-cell-over ui-datepicker-today'))]/a[text()='"+datediff+"']";
								operation.executedynamicobject("CLICK", calendarXpathTD, "xpath", "");
							}
				}
			else
				{
						operation.execute(allObjects, "CLICK", "ToDate", "xpath", "");
						String calendarXpathTD = "//td[not(contains(@class,'ui-datepicker-days-cell-over ui-datepicker-today'))]/a[text()='"+todate+"']";
						operation.executedynamicobject("CLICK", calendarXpathTD, "xpath", "");
				}
			
			//Setting the vacation
			Thread.sleep(2000);
			operation.execute(allObjects, "CLICK", "VacationSubmit", "xpath", "");	
			Thread.sleep(5000);
			
				if (temp < 90)
					{
						System.out.println("Vacation set successfully");
					}
				else
					{
						String errormsg = operation.execute(allObjects, "GETTEXT", "ErrorText", "xpath", "");
						String message = "You cannot set a vacation hold for more than 3 months.";
						Assert.assertEquals(errormsg,message);
						System.out.println("Cannot set vacation for more than 90 days");
					}
			operation.execute(allObjects, "CLICK", "MYACCOUNTSELECTOR", "css", "");
			operation.execute(allObjects, "CLICK", "BacktoCRM", "xpath", "");
			operation.execute(allObjects, "CLICK", "CRMLogout", "xpath", "");
			webdriver.quit();
		}
		catch (NoSuchElementException e) 
		{	
			e.printStackTrace();
			webdriver.quit();
		}	
		
	}

}
