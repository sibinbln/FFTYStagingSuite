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
import Operation.ReadTextFile;
import Operation.SeleniumOperation;

public class ACHRefund 
{
	static WebDriver webdriver;
	static Properties allObjects;
	static SeleniumOperation operation;
	String className = getClass().getName();
		
	@BeforeClass
	public void ExecutionStatusCheck() throws Exception
	{
		//OTPACHSuccess otpach = new OTPACHSuccess(); // Pre-requisite: Need to run OTPACHSuccess script.
		//otpach.ExecutionStatusCheck();
		//otpach.testOTPACHSuccess();
		
		boolean status = ExecutionStatusChecker.getExecutionStatus(className);
		ExecutionStatusChecker.executetestcase(className, status);
		webdriver=Launchbrowser.launchBrowser();
		ReadObjectRepository object = new ReadObjectRepository();
		allObjects = object.getObject();
		operation = new SeleniumOperation(webdriver);
		
	}
	
	@Test
	public void testACHRefund() throws Exception 
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
		
			//Reading the customer email to perform refund
			String location = System.getProperty("user.dir")+"\\TestData\\AutogeneratedTestData\\ACHCustomerToRefund.txt";
			String Customer = ReadTextFile.Readdata(location);
			
			operation.execute(allObjects, "SENDKEYS", "COL1ROW4", "css", "Email");
			operation.execute(allObjects, "SENDKEYS", "COL2ROW4", "css", "equals");
			operation.execute(allObjects, "SENDKEYS", "COL3ROW4", "css", Customer);
			operation.execute(allObjects, "CLICK", "SEARCHNOW", "css", "");
			operation.execute(allObjects, "CLICK", "RefundsLink", "xpath", "");
			Thread.sleep(5000);
			operation.execute(allObjects, "CLICK", "ClickACHMode", "xpath", "");
			//Getting values of Amount to Refund and Transaction ID
			String amounttorefund = operation.execute(allObjects, "GETTEXT", "ACHAmountToRefund", "xpath", "");
			String TransactionID = operation.execute(allObjects, "GETTEXT", "ACHTransactionID", "xpath", "");
			
			operation.execute(allObjects, "SENDKEYS", "ACHRefundAmount", "xpath", amounttorefund);		
			operation.execute(allObjects, "SENDKEYS", "ACHComments", "xpath", "Auto Refund");	
			operation.execute(allObjects, "CLICK", "RefundSave", "xpath", "");
			operation.execute(allObjects, "CLICK", "RefundConf", "xpath", "");
			Thread.sleep(5000);
			
			//Checking the Refund status in database
			String query2="SELECT description FROM `payment_gateway_log` WHERE `transaction_id` = '"+TransactionID+"' ORDER BY date_created DESC LIMIT 1";
			String RefundStatus = SeleniumOperation.querydb(query2, "brandywine_erp");
			Assert.assertEquals(RefundStatus,"VOID");	
			
			operation.execute(allObjects, "CLICK", "MYACCOUNTSELECTOR", "css", "");
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
