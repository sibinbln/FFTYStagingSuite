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

public class OTPACHDecline
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
	public void testOTPACHDecline() throws Exception 
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
		
			//Getting customer from database
			String query ="SELECT email FROM customer WHERE is_active='1' AND running_balance < '0' LIMIT 1";
			String cust= SeleniumOperation.querydb(query, "brandywine_erp");
										
			operation.execute(allObjects, "SENDKEYS", "COL1ROW4", "css", "Email");
			operation.execute(allObjects, "SENDKEYS", "COL2ROW4", "css", "equals");
			operation.execute(allObjects, "SENDKEYS", "COL3ROW4", "css", cust);
			operation.execute(allObjects, "CLICK", "SEARCHNOW", "css", "");
			operation.execute(allObjects, "CLICK", "OTPlink", "css", "");	
			Thread.sleep(5000);
			
			//Getting Account Balance of customer
			String amount = operation.execute(allObjects, "GETTEXT", "amount", "css", "");
			String amounttrim = amount.replace("$", "");
			int DisplayAmount = Integer.parseInt(String.valueOf(amounttrim).split("\\.")[0]);
	
			//ACH sand box mode doesn't allows to take a payment > 100. Any value > 100 declines the transaction.
			if (DisplayAmount <= 100)
				{
					DisplayAmount = DisplayAmount + 100;
					System.out.println("Taking amount $"+DisplayAmount+" from customer "+cust);
					operation.execute(allObjects, "CLICK", "EnterAmountRadio", "xpath", "");	
					amount = Integer.toString(DisplayAmount);
					operation.execute(allObjects, "SENDKEYS", "EnterAmountTextBox", "xpath", amount);	
				}
			else
				{
					amount = Integer.toString(DisplayAmount);
					System.out.println("Taking amount $"+amount+" from customer "+cust);
				}
			
			operation.execute(allObjects, "CLICK", "ACH", "xpath", "");	
			operation.execute(allObjects, "CLEAR", "BankName", "xpath", "");
			operation.execute(allObjects, "SENDKEYS", "BankName", "xpath", "IndianBank");
			operation.execute(allObjects, "SELECTBYINDEX", "BankAccountType", "xpath", "");
			operation.execute(allObjects, "CLEAR", "BankAccountNumber", "xpath", "");	
			operation.execute(allObjects, "SENDKEYS", "BankAccountNumber", "xpath", "3348659832");	
			operation.execute(allObjects, "CLEAR", "BankRoutingNumber", "xpath", "");	
			operation.execute(allObjects, "SENDKEYS", "BankRoutingNumber", "xpath", "011000015");	
			operation.execute(allObjects, "CLICK", "SetasDefault", "css", "");	
			operation.execute(allObjects, "CLICK", "Makepayment", "css", "");
			operation.execute(allObjects, "CLICK", "ACHconf", "xpath", "");
			Thread.sleep(2000);
			String msg = operation.execute(allObjects, "GETTEXT", "error", "css", "");
			String confirmation = "Transaction failed. We were unable to receive payment. Please try again.";
			Assert.assertEquals(confirmation,msg);
			
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
