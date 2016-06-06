package TestCases.Magento;

import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Operation.ExecutionStatusChecker;
import Operation.Launchbrowser;
import Operation.ReadExcel;
import Operation.ReadObjectRepository;
import Operation.SeleniumOperation;

import java.util.NoSuchElementException;


public class Loginout
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
	public void testLoginout() throws Exception 
	{
		
		ReadExcel re = new ReadExcel();
		String xllocation = System.getProperty("user.dir")+"\\TestData\\TestData.xls";
		re.setInputFile(xllocation, 0);
		String[][] data = re.readFile();
		
		try 
		{
			//----operation.perform(allObjects, Keyword, ObjectName, Object Type, Value)----
			operation.execute(allObjects, "ACCESSURL", "", "", data[0][1]);
			operation.execute(allObjects, "SENDKEYS", "ENTEREMAIL", "id", data[1][1]);
			operation.execute(allObjects, "SENDKEYS", "ENTERPASSWORD", "id", data[2][1]);
			operation.execute(allObjects, "CLICK", "LOGINBUTTON", "id", "");
			Thread.sleep(5000);
			operation.execute(allObjects, "CLICK", "LOGOUT", "css", "");
			Thread.sleep(5000);
			webdriver.quit();
		}
		catch (NoSuchElementException e) 
		{	
			e.printStackTrace();
			webdriver.quit();
		}	
		}
	

	}
	
