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

public class AttachFileusingAutoIT 
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
	public void testAttachFileusingAutoIT() throws Exception 
	{
		ReadExcel re = new ReadExcel();
		String xllocation = System.getProperty("user.dir")+"\\TestData\\TestData.xls";
		re.setInputFile(xllocation, 0);
		String[][] data = re.readFile();
		
		try
			{	
			operation.execute(allObjects, "ACCESSURL", "", "", data[0][7]);
			operation.execute(allObjects, "SENDKEYS", "GmailID", "id", data[1][7]);
			operation.execute(allObjects, "CLICK", "Nextopt", "xpath", "");
			operation.execute(allObjects, "SENDKEYS", "GmailPwd", "id", data[2][7]);
			operation.execute(allObjects, "CLICK", "GmailSignIn", "xpath", "");
			Thread.sleep(5000);
			operation.execute(allObjects, "CLICK", "GmailCompose", "xpath", "");
			Thread.sleep(5000);
			operation.execute(allObjects, "CLICK", "GmailAttachFile", "xpath", "");
			Thread.sleep(5000);
			//Code to attach file using AutoIT
			Runtime.getRuntime().exec("C:/Users/sibchemb/Desktop/GmailUpload.exe");
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