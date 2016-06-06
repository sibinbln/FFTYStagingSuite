package TestCases.CRM;

import java.util.Properties;

import Operation.ExecutionStatusChecker;
import Operation.Launchbrowser;
import Operation.ReadExcel;
import Operation.ReadObjectRepository;
import Operation.SeleniumOperation;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class VacationCancel
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
	public void testVacationCancel() throws Exception 
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
			
			//String query ="SELECT c.email FROM customer c, vacation v, customer_order co WHERE c.id = v.customer_id AND v.customer_id = co.customer_id AND v.status = '1' AND v.to_date > CURDATE() AND co.delivery_date > CURDATE() AND co.delivery_status_id NOT IN ('3', '4') AND c.is_active = 1 LIMIT 1";
			String query = "SELECT ce.email FROM customer_entity ce, customer_entity_varchar cev WHERE ce.entity_id = cev.entity_id AND ce.is_active = 1 AND cev.attribute_id = 235 AND cev.value = 1 AND ce.email <> 'NULL' ORDER BY ce.email ASC LIMIT 1";
			String cust= SeleniumOperation.querydb(query, "brandywine_magento");
							
			operation.execute(allObjects, "SENDKEYS", "COL1ROW4", "css", "Email");
			operation.execute(allObjects, "SENDKEYS", "COL2ROW4", "css", "equals");
			operation.execute(allObjects, "SENDKEYS", "COL3ROW4", "css", cust);
			operation.execute(allObjects, "CLICK", "SEARCHNOW", "css", "");
			
			operation.execute(allObjects, "CLICK", "ClickVacationLink", "xpath", "");	
			operation.execute(allObjects, "CLICK", "CancelVacation", "css", "");	
			Thread.sleep(5000);
			operation.execute(allObjects, "CLICK", "ConfVacationCancel", "css", "");
			Thread.sleep(5000);
			operation.execute(allObjects, "CLICK", "MYACCOUNTSELECTOR", "css", "");
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
