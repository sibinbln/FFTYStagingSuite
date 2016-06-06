package TestCases.Temp;

import java.util.Properties;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import Operation.ExecutionStatusChecker;
import Operation.Launchbrowser;
import Operation.ReadExcel;
import Operation.ReadObjectRepository;
import Operation.SeleniumOperation;


public class AmamCookieCheck 
{
	static WebDriver webdriver;
	static RemoteWebDriver rmdriver;
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
	public void testAmamCookieCheck() throws Exception 
	{
		ReadExcel re = new ReadExcel();
		String xllocation = System.getProperty("user.dir")+"\\TestData\\TestData.xls";
		re.setInputFile(xllocation, 0);
		String[][] data = re.readFile();

		try
			{	
				
				operation.execute(allObjects, "ACCESSURL", "", "", data[0][8]);
				System.out.println("Going to click Login");
				operation.execute(allObjects, "CLICK", "AmamsLogin", "xpath", "");
				Thread.sleep(3000);
				operation.execute(allObjects, "SENDKEYS", "AmamEmail", "xpath", data[1][8]);
				operation.execute(allObjects, "SENDKEYS", "AmamPassword", "xpath", data[2][8]);
				operation.execute(allObjects, "CLICK", "AmamLogin", "xpath", "");
				Thread.sleep(5000);
				operation.execute(allObjects, "CLICK", "InventoryManagementLink", "xpath", "");
				Thread.sleep(5000);
				operation.execute(allObjects, "CLICK", "AddPart", "xpath", "");
				Thread.sleep(5000);
				operation.execute(allObjects, "CLICK", "GeneralReturnPolicy", "xpath", "");
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
