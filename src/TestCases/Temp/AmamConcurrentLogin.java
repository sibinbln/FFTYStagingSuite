package TestCases.Temp;

import java.util.Properties;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import Operation.ExecutionStatusChecker;
import Operation.Launchbrowser;
import Operation.ReadExcel;
import Operation.ReadObjectRepository;
import Operation.SeleniumOperation;


public class AmamConcurrentLogin 
{
	static WebDriver webdriver;
	static WebDriver webdriver2;
	static Properties allObjects;
	static SeleniumOperation operation;
	String className = getClass().getName();
		
	@BeforeClass
	public void ExecutionStatusCheck() throws Exception
	{
		boolean status = ExecutionStatusChecker.getExecutionStatus(className);
		ExecutionStatusChecker.executetestcase(className, status);
	}
	
	@Test
	public void testAmamConcurrentLogin() throws Exception 
	{
		ReadExcel re = new ReadExcel();
		String xllocation = System.getProperty("user.dir")+"\\TestData\\TestData.xls";
		re.setInputFile(xllocation, 0);
		String[][] data = re.readFile();

		try
			{	
				//Logging in as supplier in new window
				webdriver=Launchbrowser.launchBrowser();
				ReadObjectRepository object = new ReadObjectRepository();
				allObjects = object.getObject();
				operation = new SeleniumOperation(webdriver);
				operation.execute(allObjects, "ACCESSURL", "", "", data[0][8]);
				System.out.println("Going to click Login");
				operation.execute(allObjects, "CLICK", "AmamsLogin", "xpath", "");
				Thread.sleep(3000);
				operation.execute(allObjects, "SENDKEYS", "AmamEmail", "xpath", data[1][8]);
				operation.execute(allObjects, "SENDKEYS", "AmamPassword", "xpath", data[2][8]);
				operation.execute(allObjects, "CLICK", "AmamLogin", "xpath", "");
				Thread.sleep(3000);
				String winHandleprimary = webdriver.getWindowHandle();
				Thread.sleep(3000);
				
									
					    //Logging in as dealer in new window
						webdriver2=Launchbrowser.launchBrowser();
						ReadObjectRepository object1 = new ReadObjectRepository();
						allObjects = object1.getObject();
						operation = new SeleniumOperation(webdriver2);
						operation.execute(allObjects, "ACCESSURL", "", "", data[0][8]);
						operation.execute(allObjects, "CLICK", "AmamsLogin", "xpath", "");
						Thread.sleep(3000);
						operation.execute(allObjects, "SENDKEYS", "AmamEmail", "xpath", data[1][9]);
						operation.execute(allObjects, "SENDKEYS", "AmamPassword", "xpath", data[2][9]);
						operation.execute(allObjects, "CLICK", "AmamLogin", "xpath", "");
						Thread.sleep(5000);
						
						String email2 = SeleniumOperation.cookie("GETCOOKIE", "sessionEmail", "");
						String userid2 = SeleniumOperation.cookie("GETCOOKIE", "sessionUserId", ""); 
						String role2 = SeleniumOperation.cookie("GETCOOKIE", "sessionRole", ""); 
						String token2 = SeleniumOperation.cookie("GETCOOKIE", "sessionToken", "");
						
						Thread.sleep(5000);
						webdriver2.quit();
				
				//Navigating back to Supplier window, passing dealer cookie values and page refresh
				webdriver.switchTo().window(winHandleprimary);
				operation = new SeleniumOperation(webdriver);
				Thread.sleep(3000);
				
				SeleniumOperation.cookie("DELETECOOKIE", "sessionEmail", "");
				SeleniumOperation.cookie("DELETECOOKIE", "sessionUserId", "");
				SeleniumOperation.cookie("DELETECOOKIE", "sessionRole", "");
				SeleniumOperation.cookie("DELETECOOKIE", "sessionToken", "");
				SeleniumOperation.cookie("ADDCOOKIE", "sessionEmail", email2);
				SeleniumOperation.cookie("ADDCOOKIE", "sessionUserId", userid2);
				SeleniumOperation.cookie("ADDCOOKIE", "sessionRole", role2);
				SeleniumOperation.cookie("ADDCOOKIE", "sessionToken", token2);
				
				webdriver.navigate().refresh();
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
