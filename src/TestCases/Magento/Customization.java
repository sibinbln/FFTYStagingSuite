
// Build menu for respective week(s) and add 8-9 varieties as customizable with appropriate customization qty. 
//Add at least one commodity as case available.

package TestCases.Magento;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import Operation.CalendarFeatures;
import Operation.ExecutionStatusChecker;
import Operation.Launchbrowser;
import Operation.ReadExcel;
import Operation.ReadObjectRepository;
import Operation.SeleniumOperation;

import java.util.NoSuchElementException;

public class Customization
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
	public void testCustomization() throws Exception 
	{
				
		ReadExcel re = new ReadExcel();
		String xllocation = System.getProperty("user.dir")+"\\TestData\\TestData.xls";
		re.setInputFile(xllocation, 0);
		String[][] data = re.readFile();
		
		String region = "LA";
		
		try 
		{
			int s = CalendarFeatures.getdayofweek();
			int diff = 0;
			
			if (s == 1 || s == 2 || s == 3 || s == 4)
			  {
				diff=2;
			  }
			else
			{
				diff=4;
			}
			
			System.out.println("Please wait, fetching customer email from database");
			String query = "SELECT c.email FROM customer_order co,order_item oi, customer_weekly_service_menu cwsm, subscription s, customer c , customer_address ca,region r WHERE oi.customer_order_id = co.id AND oi.customer_weekly_service_menu_id = cwsm.id AND cwsm.subscription_id = s.id AND s.frequency = 1 AND cwsm.is_customized = b'0' AND co.customer_id = c.id AND c.is_active IS TRUE AND co.delivery_date = DATE_ADD(CURDATE(),INTERVAL "+diff+" DAY) AND c.shipping_address_id = ca.id AND ca.region_id = r.id AND r.name = '"+region+"' and c.billing_method = 'AUTHORIZE_DOT_NET' LIMIT 1";
			String result = SeleniumOperation.querydb(query, "brandywine_erp");
			System.out.println("Going to customize the box for customer: "+result);
			
			//----operation.perform(allObjects, Keyword, ObjectName, Object Type, Value)----
			operation.execute(allObjects, "ACCESSURL", "", "", data[0][1]);
			operation.execute(allObjects, "SENDKEYS", "ENTEREMAIL", "id", result);
			operation.execute(allObjects, "SENDKEYS", "ENTERPASSWORD", "id", data[2][1]);
			operation.execute(allObjects, "CLICK", "LOGINBUTTON", "id", "");
			operation.execute(allObjects, "CLICK", "MyAccountlink", "css", "");
			
			// Updating Credit Card details to sand box data
			operation.execute(allObjects, "CLICK", "UBILink", "xpath", "");	
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
			
			// Proceeding with Customization flow	
			operation.execute(allObjects, "CLICK", "MyFarmStand", "css", "");	
			operation.execute(allObjects, "CLICK", "Customization", "css", "");
			
			Assert.assertTrue(webdriver.findElement(By.id("add_5")).isDisplayed());
			
			operation.execute(allObjects, "CLICK", "itemadd", "xpath", "");
			Thread.sleep(5000);
			operation.execute(allObjects, "CLICK", "Conf&Save", "xpath", "");
			operation.execute(allObjects, "CLICK", "Order", "xpath", "");
			operation.execute(allObjects, "CLICK", "SendItems", "xpath", "");
			operation.execute(allObjects, "CLICK", "BacktoFS", "xpath", "");
			
			Assert.assertTrue(SeleniumOperation.verifyTextPresent("Customized"));
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
