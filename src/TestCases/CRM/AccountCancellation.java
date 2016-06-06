package TestCases.CRM;

import java.util.Properties;

import Operation.ExecutionStatusChecker;
import Operation.Launchbrowser;
import Operation.ReadExcel;
import Operation.ReadObjectRepository;
import Operation.SeleniumOperation;

import org.junit.Assert;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class AccountCancellation 
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
	public void testAccountCancellation() throws Exception 
	{
		
		ReadExcel re = new ReadExcel();
		String xllocation = System.getProperty("user.dir")+"\\TestData\\TestData.xls";
		re.setInputFile(xllocation, 0);
		String[][] data = re.readFile();
		
		try
		{	
			
			//----operation.perform(allObjects, Keyword, ObjectName, Object Type, Value)----
			operation.execute(allObjects, "ACCESSURL", "", "", data[0][2]);
			operation.execute(allObjects, "SENDKEYS", "USERNAME", "name", data[1][2]);
			operation.execute(allObjects, "SENDKEYS", "PASSWD", "name", data[2][2]);
			operation.execute(allObjects, "CLICK", "LOGIN", "id", "");
			operation.execute(allObjects, "CLICK", "CUSTOMERLINK", "link", "");
			
			//Connecting to data base to fetch customer email
			String query ="SELECT email FROM customer WHERE is_active='1' AND billing_method='Invoice' LIMIT 1";
			String cust= SeleniumOperation.querydb(query, "brandywine_erp");
			System.out.println("Going to cancel the customer " +cust);
							
			operation.execute(allObjects, "SENDKEYS", "COL1ROW4", "css", "Email");
			operation.execute(allObjects, "SENDKEYS", "COL2ROW4", "css", "equals");
			operation.execute(allObjects, "SENDKEYS", "COL3ROW4", "css", cust);
			operation.execute(allObjects, "CLICK", "SEARCHNOW", "css", "");
			operation.execute(allObjects, "CLICK", "ACBUTTON", "css", "");
			operation.execute(allObjects, "SENDKEYS", "SOURCE", "id", "Email");
			operation.execute(allObjects, "SENDKEYS", "REASON", "id", "Did not specify");
			operation.execute(allObjects, "CLICK", "ACSUBMIT", "id", "");
						
			//Confirming to windows alert
			webdriver.switchTo().alert().accept();
				
			operation.execute(allObjects, "CLICK", "ACCANCELTKT", "css", "");
			
			//Checking the customer status after cancellation. Expected status is Inactive
			String custstatus = operation.execute(allObjects, "GETTEXT", "CUSTSTATUS", "xpath", "");
			Thread.sleep(5000);
			Assert.assertEquals("Inactive",custstatus);
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
