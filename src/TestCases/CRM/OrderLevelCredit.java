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

public class OrderLevelCredit 
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
	public void testOrderLevelCredit() throws Exception 
	{
		
		ReadExcel re = new ReadExcel();
		String xllocation = System.getProperty("user.dir")+"\\TestData\\TestData.xls";
		re.setInputFile(xllocation, 0);
		String[][] data = re.readFile();
		re.setInputFile(xllocation, 6);
		String[][] values = re.readFile();
		
		try
		{	
			//----operation.perform(allObjects, Keyword, ObjectName, Object Type, Value)----
			operation.execute(allObjects, "ACCESSURL", "", "", data[0][2]);
			operation.execute(allObjects, "SENDKEYS", "USERNAME", "name", data[1][2]);
			operation.execute(allObjects, "SENDKEYS", "PASSWD", "name", data[2][2]);
			operation.execute(allObjects, "CLICK", "LOGIN", "id", "");
			
			//Getting Order ID from database
			String query1 ="SELECT oi.id FROM customer c, customer_order co, order_item oi WHERE c.id = (SELECT id FROM customer WHERE email = '"+values[0][1]+"') AND c.id = co.customer_id AND co.id = oi.customer_order_id AND co.delivery_date = '"+values[1][1]+"' AND oi.`order_status` = 'ACTIVE' AND `order_item_type_id` = 1;";
			String OrderItemID= SeleniumOperation.querydb(query1, "brandywine_erp");
						
			operation.execute(allObjects, "CLICK", "CUSTOMERLINK", "link", "");
			Thread.sleep(5000);
			
			operation.execute(allObjects, "SENDKEYS", "COL1ROW4", "css", "Email");
			operation.execute(allObjects, "SENDKEYS", "COL2ROW4", "css", "equals");
			operation.execute(allObjects, "SENDKEYS", "COL3ROW4", "css", values[0][1]);
			operation.execute(allObjects, "CLICK", "SEARCHNOW", "css", "");
			operation.execute(allObjects, "CLICK", "ViewMore", "xpath", "");
			Thread.sleep(10000);
			operation.execute(allObjects, "SELECTLASTINDEX", "PageSize", "xpath", "");
		    Thread.sleep(10000);
		    
					for (int i = 1; i < 25; )
						{
							String orderItemXpath = ".//*[@id='pagination_order']/tr["+i+"]/td[3]";
							String oiID = operation.executedynamicobject("GETTEXT", orderItemXpath, "xpath", "");
							Thread.sleep(3000);
							if (oiID.equals(OrderItemID))
								{
									String ViewOrderIcon = ".//*[@id='pagination_order']/tr["+i+"]/td[6]/a[1]";
									operation.executedynamicobject("CLICK", ViewOrderIcon, "xpath", "");
									Thread.sleep(5000);
									break;
								}
							else
								{
									i = i + 1;
								}
						}
					
			operation.execute(allObjects, "CLICK", "OrderTab", "xpath", "");
			operation.execute(allObjects, "SENDKEYS", "OrderFBAmount", "xpath", values[2][1]);
			operation.execute(allObjects, "SELECTBYINDEX1", "OrderFBSubject", "xpath", "");
			Thread.sleep(3000);
			operation.execute(allObjects, "SELECTBYINDEX1", "OrderFBReason", "xpath", "");
			operation.execute(allObjects, "SENDKEYS", "OrderFBComment", "xpath", values[3][1]);
			operation.execute(allObjects, "CLICK", "OrderFBSave", "xpath", "");
			operation.execute(allObjects, "CLICK", "CreditConf", "xpath", "");
			Thread.sleep(5000);
			
			//Getting success message for assertion
			String message = operation.execute(allObjects, "GETTEXT", "OrderFBSuccess", "xpath", "");
			String success_message = "Credit has been saved successfully.";
			String successmessage = success_message.trim();
			Assert.assertEquals(message,successmessage);
			
			//Getting feedback amount from database to compare against
			String query4 = "SELECT refund_amount FROM `order_item_feedback` ORDER BY date_created DESC LIMIT 1";
			String result_amount = SeleniumOperation.querydb(query4, "brandywine_erp");
			Assert.assertEquals(values[2][1],result_amount);
			
			//Checking if the entry is displayed in Credits Listing page
			operation.execute(allObjects, "CLICK", "MYACCOUNTSELECTOR", "css", "");
			operation.execute(allObjects, "CLICK", "CreditsListing", "LINK", "");
			Thread.sleep(5000);
			String CLAmount = operation.execute(allObjects, "GETTEXT", "CreditsListingAmount", "xpath", "");
			Assert.assertEquals(CLAmount,values[2][1]);
			
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
