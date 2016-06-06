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

public class LateCancel 
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
	public void testLateCancel() throws Exception 
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
			
			//Getting customer email from database
			System.out.println("Please wait.. Fetching customer email from database");
			String query1 ="SELECT c.email FROM customer c, customer_order co, order_item oi WHERE c.id = co.customer_id AND co.id = oi.customer_order_id AND co.delivery_status_id = '3' AND oi.`order_status` = 'ACTIVE' ORDER BY co.delivery_date DESC LIMIT 1";
			String Customer= SeleniumOperation.querydb(query1, "brandywine_erp");
						
			operation.execute(allObjects, "CLICK", "CUSTOMERLINK", "link", "");
			Thread.sleep(5000);
			
			//Getting order item id from database
			String query2 ="SELECT oi.id FROM customer c, customer_order co, order_item oi WHERE c.id = (SELECT id FROM customer WHERE email = '"+Customer+"') AND c.id = co.customer_id AND co.id = oi.customer_order_id AND co.delivery_status_id = '3' AND oi.`order_status` = 'ACTIVE' AND `order_item_type_id` = 1 ORDER BY co.delivery_date DESC LIMIT 1";
			String OrderItemID= SeleniumOperation.querydb(query2, "brandywine_erp");
			System.out.println(OrderItemID);
			
			//Getting order item count from database
			String query3 ="SELECT count(oi.id) FROM customer c, customer_order co, order_item oi WHERE c.id = (SELECT id FROM customer WHERE email = '"+Customer+"') AND c.id = co.customer_id AND co.id = oi.customer_order_id AND `order_item_type_id` = 1 AND oi.order_status IN ('Active', 'Late Cancel') ORDER BY co.delivery_date DESC LIMIT 1";
			String OrderItemCount= SeleniumOperation.querydb(query3, "brandywine_erp");
			int count = Integer.parseInt(OrderItemCount);
			System.out.println(Customer+" has "+count+" order items");
			
			operation.execute(allObjects, "SENDKEYS", "COL1ROW4", "css", "Email");
			operation.execute(allObjects, "SENDKEYS", "COL2ROW4", "css", "equals");
			operation.execute(allObjects, "SENDKEYS", "COL3ROW4", "css", Customer);
			operation.execute(allObjects, "CLICK", "SEARCHNOW", "css", "");
			
			if (count < 5)
				{
					for (int i = 3; i < 7; )
						{
							String orderItemXpath = ".//*[@id='orderTable']/tbody/tr["+i+"]/td[3]";
							String oiID = operation.executedynamicobject("GETTEXT", orderItemXpath, "xpath", "");
							Thread.sleep(3000);
							if (oiID.equals(OrderItemID))
								{
									String ViewOrderIcon = ".//*[@id='orderTable']/tbody/tr["+i+"]/td[5]/a[1]/img";
									operation.executedynamicobject("CLICK", ViewOrderIcon, "xpath", "");
									Thread.sleep(5000);
									break;
								}
							else
								{
									i = i + 1;
								}
						}
				}
			else
				{
					
					operation.execute(allObjects, "CLICK", "ViewMore", "xpath", "");
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
				}
			
			operation.execute(allObjects, "CLICK", "LCTab", "xpath", "");
			Thread.sleep(5000);
			
			operation.execute(allObjects, "SENDKEYS", "LCComment", "xpath", "Auto LateCancel");
			operation.execute(allObjects, "CLICK", "LCSave", "xpath", "");
			operation.execute(allObjects, "CLICK", "CreditConf", "xpath", "");
			Thread.sleep(10000);
			
			//Getting success message for assertion
			String message = operation.execute(allObjects, "GETTEXT", "LCSuccess", "xpath", "");
			String success_message = "Order successfully cancelled. ArcLogistics file has been exported for this order. A ticket has been raised to the Operations team successfully.";
			String successmessage = success_message.trim();
			Assert.assertEquals(message,successmessage);
			
			//Getting feedback amount from database to compare against
			String query4 = "SELECT oi.order_status FROM customer c, order_item oi WHERE c.id = (SELECT id FROM customer WHERE email = '"+Customer+"') AND oi.id = '"+OrderItemID+"' AND oi.order_item_type_id = '1'";
			String result_status = SeleniumOperation.querydb(query4, "brandywine_erp");
			String LCStatus = "Late Cancel";
			Assert.assertEquals(result_status,LCStatus);
			
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
