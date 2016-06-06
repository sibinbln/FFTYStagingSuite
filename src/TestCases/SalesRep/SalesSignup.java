package TestCases.SalesRep;

import java.util.Calendar;
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
import Operation.WriteTextFile;

public class SalesSignup 
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
	public void testSalesSignup() throws Exception 
	{
		
		ReadExcel re = new ReadExcel();
		String xllocation = System.getProperty("user.dir")+"\\TestData\\TestData.xls";
		re.setInputFile(xllocation, 0);
		String[][] data = re.readFile();
		
		re.setInputFile(xllocation, 4);
		String[][] values = re.readFile();
		
		try 
		{
			//----operation.perform(allObjects, Keyword, ObjectName, Object Type, Value)----
			operation.execute(allObjects, "ACCESSURL", "", "", data[0][5]);
			operation.execute(allObjects, "SENDKEYS", "salesemailid", "id", data[1][5]);	
			operation.execute(allObjects, "SENDKEYS", "salespassword", "id", data[2][5]);	
			operation.execute(allObjects, "CLICK", "salesloginbutton", "id", "");	
			
			//Confirming to windows alert
			webdriver.switchTo().alert().accept();
			//Thread.sleep(5000);
			operation.execute(allObjects, "CLICK", "EnterCustomers", "xpath", "");
			operation.execute(allObjects, "SENDKEYS", "saleszipcode", "id", values[1][0]);	
			operation.execute(allObjects, "SENDKEYS", "salesstreetaddress", "id", values[1][1]);
			
			Calendar cal = Calendar.getInstance();
			java.lang.String custemail="salescust"+cal.get(Calendar.YEAR)+cal.get(Calendar.DAY_OF_MONTH)+(cal.get(Calendar.MONTH)+1)+cal.get(Calendar.HOUR)+cal.get(Calendar.MINUTE)+cal.get(Calendar.SECOND)+"@yopmail.com";
			
			operation.execute(allObjects, "SENDKEYS", "salesemail", "id", custemail);
			operation.execute(allObjects, "SENDKEYS", "salesreenteremail", "id", custemail);
			operation.execute(allObjects, "CLICK", "saleslookup", "id", "");
			operation.execute(allObjects, "CLICK", "salessubmit", "css", "");
			operation.execute(allObjects, "SELECTBYINDEX", "salespromo", "id", "");
			operation.execute(allObjects, "SENDKEYS", "salesfirstname", "id", values[1][2]);
			operation.execute(allObjects, "SENDKEYS", "saleslastname", "id", values[1][3]);
			operation.execute(allObjects, "SENDKEYS", "salesapartmentname", "id", values[1][4]);
			operation.execute(allObjects, "SENDKEYS", "specialdeliverynotes", "id", values[1][5]);
			operation.execute(allObjects, "SENDKEYS", "salesphone", "id", values[1][6]);
			operation.execute(allObjects, "SELECTBYINDEX", "salesservicename", "id", "");
			operation.execute(allObjects, "SELECTBYINDEX", "salesfrequency", "id", "");
			operation.execute(allObjects, "SELECTBYINDEX", "salesdelstartdate", "id", "");
			operation.execute(allObjects, "CLICK", "review", "css", "");
			Thread.sleep(3000);
			operation.execute(allObjects, "CLICK", "confirm", "css", "");
			Thread.sleep(3000);
			operation.execute(allObjects, "CLICK", "SalesRepsTab", "css", "");
			operation.execute(allObjects, "CLICK", "ViewCustomers", "xpath", "");
			Thread.sleep(5000);
			
			String TRNumber = operation.execute(allObjects, "GETTEXT", "TRNumber", "xpath", "");
			WriteTextFile objWrite = new WriteTextFile();
			String TR_Number = System.getProperty("user.dir")+"\\TestData\\AutogeneratedTestData\\TRNumber.txt";
			objWrite.Writedata(TR_Number, TRNumber);
			
			operation.execute(allObjects, "CLICK", "saleslogout", "css", "");
			webdriver.quit();
		}
		catch (NoSuchElementException e) 
		{	
			e.printStackTrace();
			webdriver.quit();
		}
		
	}
}