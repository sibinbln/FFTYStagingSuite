package TestCases.CRM;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import Operation.ExecutionStatusChecker;
import Operation.Launchbrowser;
import Operation.ReadExcel;
import Operation.ReadObjectRepository;
import Operation.ReadTextFile;
import Operation.SeleniumOperation;

import java.util.NoSuchElementException;
import java.util.Properties;

//import TestCases.ERP.CheckDeposit;

public class NSFCheck 
{
	static WebDriver webdriver;
	static Properties allObjects;
	static SeleniumOperation operation;
	String className = getClass().getName();
		
	@BeforeClass
	public void ExecutionStatusCheck() throws Exception
	{
		//CheckDeposit cd = new CheckDeposit(); // Pre-requisite: Need to run CheckDeposit script.
		//cd.ExecutionStatusCheck();
		//cd.testCheckDeposit(); 
		
		boolean status = ExecutionStatusChecker.getExecutionStatus(className);
		ExecutionStatusChecker.executetestcase(className, status);
		webdriver=Launchbrowser.launchBrowser();
		ReadObjectRepository object = new ReadObjectRepository();
		allObjects = object.getObject();
		operation = new SeleniumOperation(webdriver);
	}
	
	@Test
	public void testNSFCheck() throws Exception 
	{
		
		ReadExcel re = new ReadExcel();
		String xllocation = System.getProperty("user.dir")+"\\TestData\\TestData.xls";
		re.setInputFile(xllocation, 0);
		String[][] data = re.readFile();
		
		try 
		{
			String NSFAccountNumber = System.getProperty("user.dir")+"\\TestData\\AutogeneratedTestData\\NSFAccountNumber.txt";
			String AccountNumber = ReadTextFile.Readdata(NSFAccountNumber);
			
			//----operation.perform(allObjects, Keyword, ObjectName, Object Type, Value)----
			operation.execute(allObjects, "ACCESSURL", "", "", data[0][2]);
			operation.execute(allObjects, "SENDKEYS", "USERNAME", "name", data[1][2]);
			operation.execute(allObjects, "SENDKEYS", "PASSWD", "name", data[2][2]);
			operation.execute(allObjects, "CLICK", "LOGIN", "id", "");
			operation.execute(allObjects, "CLICK", "CUSTOMERLINK", "link", "");
			operation.execute(allObjects, "SENDKEYS", "COL1ROW4", "css", "Email");
			operation.execute(allObjects, "SENDKEYS", "COL2ROW4", "css", "equals");
			operation.execute(allObjects, "SENDKEYS", "COL3ROW1", "css", AccountNumber);
			operation.execute(allObjects, "CLICK", "SEARCHNOW", "css", "");
			operation.execute(allObjects, "CLICK", "GoToCustomer", "xpath", "");
			Thread.sleep(5000);
			operation.execute(allObjects, "CLICK", "NSF", "xpath", "");
			operation.execute(allObjects, "CLICK", "NSFType", "xpath", "");
			
			String location = System.getProperty("user.dir")+"\\TestData\\AutogeneratedTestData\\CheckNumber.txt";
			String CheckNumber = ReadTextFile.Readdata(location);
			
			operation.execute(allObjects, "SENDKEYS", "NSFCheckNumber", "xpath", CheckNumber);
			operation.execute(allObjects, "CLICK", "NoFeeCharged", "xpath", "");
			Thread.sleep(2000);
			operation.execute(allObjects, "CLICK", "NoFeeCharged", "xpath", "");
			Thread.sleep(5000);
			operation.execute(allObjects, "CLICK", "NSFSubmit", "xpath", "");
			Thread.sleep(5000);
			operation.execute(allObjects, "CLICK", "NSFConf", "xpath", "");
			Thread.sleep(5000);
			
			//Checking for the success message
			String Successmessage = operation.execute(allObjects, "GETTEXT", "NSFSuccess", "css", "");
			String Expectedmessage = "Check marked as 'Returned Payment'.";
			Assert.assertEquals(Successmessage,Expectedmessage);
			
			operation.execute(allObjects, "CLICK", "MYACCOUNTSELECTOR", "css", "");
			Thread.sleep(5000);
			
			//Checking if the record is added to ERP database
			String query ="SELECT nsf_fee FROM `non_sufficient_funds` WHERE customer_id = (SELECT id FROM customer WHERE account_number = '"+AccountNumber+"')";
			String NSF_Fee= SeleniumOperation.querydb(query, "brandywine_erp");	
			Assert.assertEquals(NSF_Fee,"15.00");
			
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
