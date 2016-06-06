package TestCases.CRM;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import Operation.ExecutionStatusChecker;
import Operation.Launchbrowser;
import Operation.ReadExcel;
import Operation.ReadObjectRepository;
import Operation.SeleniumOperation;

import java.util.NoSuchElementException;
import java.util.Properties;

public class Reactivation 
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
	public void testReactivation() throws Exception 
	{
		
		ReadExcel re = new ReadExcel();
		String xllocation = System.getProperty("user.dir")+"\\TestData\\TestData.xls";
		re.setInputFile(xllocation, 0);
		String[][] data = re.readFile();
		
		re.setInputFile(xllocation, 3);
		String[][] values = re.readFile();
		
		try 
		{
			//----operation.perform(allObjects, Keyword, ObjectName, Object Type, Value)----
			operation.execute(allObjects, "ACCESSURL", "", "", data[0][2]);
			operation.execute(allObjects, "SENDKEYS", "USERNAME", "name", data[1][2]);
			operation.execute(allObjects, "SENDKEYS", "PASSWD", "name", data[2][2]);
			operation.execute(allObjects, "CLICK", "LOGIN", "id", "");
			operation.execute(allObjects, "CLICK", "CUSTOMERLINK", "link", "");
			
		String query ="SELECT email FROM customer WHERE is_active = 0 AND email IS NOT NULL AND customer_type = 'Member' LIMIT 1";
		String cust= SeleniumOperation.querydb(query, "brandywine_erp");
		System.out.println("Going to reactivate the customer " +cust);
		operation.execute(allObjects, "SENDKEYS", "COL1ROW4", "css", "Email");
		operation.execute(allObjects, "SENDKEYS", "COL2ROW4", "css", "equals");
		operation.execute(allObjects, "SENDKEYS", "COL3ROW4", "css", cust);
		operation.execute(allObjects, "CLICK", "SEARCHNOW", "css", "");
			
		String region = operation.execute(allObjects, "GETTEXT", "GetRegion", "xpath", "");
		String location = operation.execute(allObjects, "GETTEXT", "Location", "xpath", "");
						
		operation.execute(allObjects, "CLICK", "Reactivate", "xpath", "");
		
			if (region.equals("SF"))
				{
					operation.execute(allObjects, "CLEAR", "ReactZip", "xpath", "");
					operation.execute(allObjects, "SENDKEYS", "ReactZip", "xpath", values[1][0]);
					operation.execute(allObjects, "CLICK", "ReacPromo", "xpath", "");
					operation.execute(allObjects, "CLICK", "ReactivateContinue", "css", "");
					operation.execute(allObjects, "CLICK", "ReBoxTypeSF", "xpath", "");
					operation.execute(allObjects, "SELECTBYINDEX", "ReFreqSF", "xpath", "");
					operation.execute(allObjects, "SELECTBYINDEX", "FDDSF", "xpath", "");
					operation.execute(allObjects, "CLICK", "ContinueSF", "xpath", "");
				}
			else if (region.equals("LA"))
				{
					operation.execute(allObjects, "CLEAR", "ReactZip", "xpath", "");
					operation.execute(allObjects, "SENDKEYS", "ReactZip", "xpath", values[1][1]);
					operation.execute(allObjects, "CLICK", "ReacPromo", "xpath", "");
					operation.execute(allObjects, "CLICK", "ReactivateContinue", "css", "");
					operation.execute(allObjects, "CLICK", "ReBoxTypeLA", "xpath", "");
					operation.execute(allObjects, "SELECTBYINDEX", "ReFreqLA", "xpath", "");
					operation.execute(allObjects, "SELECTBYINDEX", "FDDLA", "xpath", "");
					operation.execute(allObjects, "CLICK", "ContinueLA", "xpath", "");
				}
			else 
				{
					System.out.println("Couldn't recognize the region "+region);
				}
			
			operation.execute(allObjects, "CLICK", "ReactLastName", "xpath", "");
			Thread.sleep(5000);
			boolean presence = SeleniumOperation.isdisplayed(allObjects, "ISDISPLAYED", "InvalidAddr", "xpath", "");
			System.out.println("Address validation failed - "+presence);
			
			if (presence == true)
				{	
					operation.execute(allObjects, "CLICK", "InvalidAddr", "xpath", "");
					Thread.sleep(5000);
				}
			else if (presence == false)
				{
					operation.execute(allObjects, "CLICK", "ReactPaymentType", "css", "");
				}
		
			if (location.equals("Office/Day"))
			{
				operation.execute(allObjects, "CLEAR", "ReactCompanyName", "xpath", "");
				operation.execute(allObjects, "SENDKEYS", "ReactCompanyName", "xpath", values[1][2]);
				operation.execute(allObjects, "CLEAR", "ReactBuildingName", "xpath", "");
				operation.execute(allObjects, "SENDKEYS", "ReactBuildingName", "xpath", values[1][3]);
				operation.execute(allObjects, "CLEAR", "ReactBussHours", "xpath", "");
				operation.execute(allObjects, "SENDKEYS", "ReactBussHours", "xpath", values[1][4]);
				operation.execute(allObjects, "SELECTBYINDEX", "ReactWinStart", "xpath", "");
				operation.execute(allObjects, "SELECTBYINDEX", "ReactWinEnd", "xpath", "");
				operation.execute(allObjects, "SELECTBYINDEX", "ReactWinReason", "xpath", "");
			}
		operation.execute(allObjects, "CLICK", "ISGATECODEREQ", "xpath", "");
		operation.execute(allObjects, "CLICK", "ISDELIVERYKEYREQ", "xpath", "");
		operation.execute(allObjects, "CLICK", "ReactPaymentType", "css", "");
		operation.execute(allObjects, "SENDKEYS", "CCTYPE", "id", values[1][5]);
		operation.execute(allObjects, "CLEAR", "CCNUMBER", "id", "");
		operation.execute(allObjects, "SENDKEYS", "CCNUMBER", "id", values[1][6]);
		operation.execute(allObjects, "SELECTBYINDEX", "CCEXPIRYMONTH", "id", "");
		operation.execute(allObjects, "SELECTBYINDEX", "CCEXPIRYYEAR", "id", "");
		operation.execute(allObjects, "CLEAR", "CCSECURITYCODE", "css", "");
		operation.execute(allObjects, "SENDKEYS", "CCSECURITYCODE", "css", values[1][7]);
		operation.execute(allObjects, "CLICK", "CONTINUEPAGE3", "css", "");
		operation.execute(allObjects, "CLICK", "CONFIRMORDER", "css", "");
		operation.execute(allObjects, "CLICK", "ConfirmReact", "css", "");
			Thread.sleep(5000);
			String query2 ="SELECT is_active FROM customer WHERE email = '"+cust+"'";
			String custstatus= SeleniumOperation.querydb(query2, "brandywine_erp");
			int count = Integer.parseInt(custstatus);
			int check = 1;
			System.out.println("Customer Reactivated Successfully with status " +custstatus);
			Assert.assertEquals(check,count);
			
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
