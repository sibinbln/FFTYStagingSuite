package TestCases.CRM;

import java.util.Properties;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

import Operation.ExecutionStatusChecker;
import Operation.Launchbrowser;
import Operation.ReadExcel;
import Operation.ReadObjectRepository;
import Operation.SeleniumOperation;

public class CustomerCredit 
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
	public void testCustomerCredit() throws Exception 
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
		
			String query ="SELECT email FROM customer WHERE is_active='1' LIMIT 1";
			String Customer= SeleniumOperation.querydb(query, "brandywine_erp");
			String amount = "1.25";
			
			operation.execute(allObjects, "SENDKEYS", "COL1ROW4", "css", "Email");
			operation.execute(allObjects, "SENDKEYS", "COL2ROW4", "css", "equals");
			operation.execute(allObjects, "SENDKEYS", "COL3ROW4", "css", Customer);
			operation.execute(allObjects, "CLICK", "SEARCHNOW", "css", "");
			operation.execute(allObjects, "CLICK", "GoToCustomer", "xpath", "");
			operation.execute(allObjects, "CLICK", "CustomerCredit", "xpath", "");
			operation.execute(allObjects, "SENDKEYS", "CustCreditAmount", "xpath", amount);
			operation.execute(allObjects, "SELECTBYINDEX", "CustCreditSubject", "xpath", "");
			Thread.sleep(5000);
			operation.execute(allObjects, "SELECTBYINDEX", "CustCreditReason", "xpath", "");
			operation.execute(allObjects, "SENDKEYS", "CustCreditComments", "xpath", "Automation Customer-Credit");
			operation.execute(allObjects, "CLICK", "CreditSave", "xpath", "");
			operation.execute(allObjects, "CLICK", "CreditConf", "xpath", "");
	
			Thread.sleep(5000);
			String message = operation.execute(allObjects, "GETTEXT", "CreditSaveSuccess", "xpath", "");
			String success_message = "Credit has been saved successfully.";
			String successmessage = success_message.trim();
			Assert.assertEquals(message,successmessage);
			
			String query2 = "SELECT amount FROM customer_feedback WHERE customer_id = (SELECT id FROM customer WHERE email = '"+Customer+"') ORDER BY date_created DESC LIMIT 1";
			String result_amount = SeleniumOperation.querydb(query2, "brandywine_erp");
			Assert.assertEquals(result_amount,amount);
			
			//Checking if the entry is displayed in Credits Listing page
			operation.execute(allObjects, "CLICK", "MYACCOUNTSELECTOR", "css", "");
			operation.execute(allObjects, "CLICK", "CreditsListing", "LINK", "");
			Thread.sleep(5000);
			String CLAmount = operation.execute(allObjects, "GETTEXT", "CreditsListingAmount", "xpath", "");
			Assert.assertEquals(CLAmount,amount);
			
			operation.execute(allObjects, "CLICK", "BacktoCRM", "xpath", "");
			webdriver.quit();
		}
		catch (NoSuchElementException e) 
		{	
			e.printStackTrace();
			webdriver.quit();
		}	
		
	}

}
