package TestCases.Magento;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Operation.ExecutionStatusChecker;
import Operation.Launchbrowser;
import Operation.ReadExcel;
import Operation.ReadObjectRepository;
import Operation.SeleniumOperation;

import java.util.NoSuchElementException;

public class UpdateBillingInfo 
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
	public void testUpdateBillingInfo() throws Exception 
	{
		
		ReadExcel re = new ReadExcel();
		String xllocation = System.getProperty("user.dir")+"\\TestData\\TestData.xls";
		re.setInputFile(xllocation, 0);
		String[][] data = re.readFile();
				
		try 
		{
			String query = "SELECT account_number FROM customer WHERE billing_method = 'Invoice' and is_active = 1 limit 1";
			String result = SeleniumOperation.querydb(query, "brandywine_erp");
				
			//----operation.perform(allObjects, Keyword, ObjectName, Object Type, Value)----
			operation.execute(allObjects, "ACCESSURL", "", "", data[0][1]);
			operation.execute(allObjects, "SENDKEYS", "ENTEREMAIL", "id", result);
			operation.execute(allObjects, "SENDKEYS", "ENTERPASSWORD", "id", data[2][1]);
			operation.execute(allObjects, "CLICK", "LOGINBUTTON", "id", "");
			Thread.sleep(5000);
			boolean presence1 = SeleniumOperation.isdisplayed(allObjects, "ISDISPLAYED", "AddCard", "css", "");
			boolean presence2 = SeleniumOperation.isdisplayed(allObjects, "ISDISPLAYED", "ExpInfo", "xpath", "");
			System.out.println("No card on customer profile - "+presence1);
			System.out.println("Credit card expired - "+presence2);
			if (presence1 == true)
				{
					operation.execute(allObjects, "CLICK", "AddCard", "css", "");
				}
			else if (presence2 == true)
				{
					operation.execute(allObjects, "CLICK", "ExpInfo", "xpath", "");
				}
			Thread.sleep(5000);
			operation.execute(allObjects, "CLICK", "MyAccountlink", "css", "");
			operation.execute(allObjects, "CLICK", "EditUBI", "xpath", "");
			operation.execute(allObjects, "CLICK", "UBIPaymentMethod", "xpath", "");	
			operation.execute(allObjects, "SENDKEYS", "UBICardType", "css", "Visa");	
			operation.execute(allObjects, "CLEAR", "UBICardNo", "css", "");	
			operation.execute(allObjects, "SENDKEYS", "UBICardNo", "css", "4111111111111111");	
			operation.execute(allObjects, "SELECTBYINDEX", "UBICardExpMonth", "css", "");	
			operation.execute(allObjects, "SELECTBYINDEX", "UBICardExpyear", "css", "");	
			operation.execute(allObjects, "CLEAR", "UBICVV", "css", "");	
			operation.execute(allObjects, "SENDKEYS", "UBICVV", "css", "123");	
			operation.execute(allObjects, "CLICK", "UBIPaymentOption", "css", "");	
			operation.execute(allObjects, "CLICK", "UBISave", "css", "");	
			Thread.sleep(10000);
			String query2 = "SELECT billing_method FROM customer WHERE account_number= '"+result+"'";
			String result2 = SeleniumOperation.querydb(query2, "brandywine_erp");
			
			String billing_method="AUTHORIZE_DOT_NET";
			Assert.assertEquals(result2,billing_method);
			
			operation.execute(allObjects, "CLICK", "LOGOUT", "css", "");
			webdriver.quit();
		}
		catch (NoSuchElementException e) 
		{	
			e.printStackTrace();
			webdriver.quit();
		}
		
	}
}
