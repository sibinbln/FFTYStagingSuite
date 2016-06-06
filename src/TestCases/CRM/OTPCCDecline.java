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

public class OTPCCDecline 
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
	public void testOTPDecline() throws Exception 
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
			
			String query ="SELECT email FROM customer WHERE is_active='1' AND running_balance < '0' LIMIT 1";
			String cust= SeleniumOperation.querydb(query, "brandywine_erp");
							
			operation.execute(allObjects, "SENDKEYS", "COL1ROW4", "css", "Email");
			operation.execute(allObjects, "SENDKEYS", "COL2ROW4", "css", "equals");
			operation.execute(allObjects, "SENDKEYS", "COL3ROW4", "css", cust);
			operation.execute(allObjects, "CLICK", "SEARCHNOW", "css", "");
			operation.execute(allObjects, "CLICK", "OTPlink", "css", "");	
			operation.execute(allObjects, "CLICK", "SelectEnterAmount", "css", "");	
			operation.execute(allObjects, "SENDKEYS", "Enteramount", "css", "70.02");
			operation.execute(allObjects, "CLICK", "OTPPaymentmode", "css", "");	
			operation.execute(allObjects, "SELECTBYINDEX", "OTPCCType", "css", "MasterCard");
			operation.execute(allObjects, "CLEAR", "OTPCCNumber", "css", "");
			operation.execute(allObjects, "SENDKEYS", "OTPCCNumber", "css", "5555555555554444");	
			operation.execute(allObjects, "SELECTBYINDEX", "OTPExpMonth", "css", "");	
			operation.execute(allObjects, "SELECTBYINDEX", "OTPExpYear", "css", "");
			operation.execute(allObjects, "SENDKEYS", "OTPSecurityCode", "css", "123");	
			operation.execute(allObjects, "CLICK", "SetasDefault", "css", "");
			operation.execute(allObjects, "CLICK", "Makepayment", "css", "");
			operation.execute(allObjects, "CLICK", "popupconfirmation", "css", "");
			
			String msg = operation.execute(allObjects, "GETTEXT", "error", "css", "");
			String confirmation = "Transaction failed. We were unable to receive payment. Please try again.";
			Assert.assertEquals(confirmation,msg);
			
			operation.execute(allObjects, "CLICK", "Paymenttab", "css", "");	
			
			String amount= operation.execute(allObjects, "GETTEXT", "Paymentrecord", "xpath", "");	
			System.out.println("Amount2 = "+amount);
			Assert.assertEquals(amount,"$70.02");
			
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