package TestCases.Magento;

import java.util.Calendar;
import java.util.Properties;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Operation.ExecutionStatusChecker;
import Operation.Launchbrowser;
import Operation.ReadExcel;
import Operation.ReadObjectRepository;
import Operation.SeleniumOperation;
import Operation.WriteTextFile;

import java.util.NoSuchElementException;

import junit.framework.Assert;

public class GuestDonateABox
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
	public void testGuestDAB() throws Exception 
	{
		
		ReadExcel re = new ReadExcel();
		String xllocation = System.getProperty("user.dir")+"\\TestData\\TestData.xls";
		re.setInputFile(xllocation, 0);
		String[][] data = re.readFile();
		int count = 1;
		int attempt = 0;
		
		try 
		{
			Calendar cal = Calendar.getInstance();
			//Email contains year + day of month + month + hour + minute + seconds
			String email = "onlinecust"+cal.get(Calendar.YEAR)+cal.get(Calendar.DAY_OF_MONTH)+(cal.get(Calendar.MONTH)+1)+cal.get(Calendar.HOUR)+cal.get(Calendar.MINUTE)+cal.get(Calendar.SECOND)+"@yopmail.com";
		
			//----operation.perform(allObjects, Keyword, ObjectName, Object Type, Value)----
			operation.execute(allObjects, "ACCESSURL", "", "", data[0][4]);
			Thread.sleep(5000);
			operation.execute(allObjects, "CLICK", "Community", "xpath", "");
			operation.execute(allObjects, "CLICK", "DonateABox", "xpath", "");
			Thread.sleep(5000);
			operation.execute(allObjects, "CLICK", "GuestSelection", "xpath", "");
			operation.execute(allObjects, "CLICK", "GuestGetStarted", "xpath", "");
			
			operation.execute(allObjects, "CLICK", "servicesizesf", "css", "");
			operation.execute(allObjects, "CLICK", "onetime", "css", "");
			operation.execute(allObjects, "CLICK", "onetimeboxqty", "css", "");
			operation.execute(allObjects, "SELECTBYINDEX", "onetimeboxqty", "css", "");
			operation.execute(allObjects, "CLICK", "selectcharity", "xpath", "");
			operation.execute(allObjects, "SENDKEYS", "GuestGCFN", "css", "Guest");
			operation.execute(allObjects, "SENDKEYS", "GuestGCLN", "css", "Automation Customer");
			operation.execute(allObjects, "SENDKEYS", "GuestGCBillingAddr", "css", "720 East 78th Street");
			operation.execute(allObjects, "SENDKEYS", "GuestGCZip", "css", "90001");
			
			do
				{
					attempt = 0;
					operation.execute(allObjects, "CLEAR", "GuestGCEmail", "css", "");
					operation.execute(allObjects, "SENDKEYS", "GuestGCEmail", "css", email);
					
					Thread.sleep(5000);
					operation.execute(allObjects, "SWICTHTOIFRAME", "CaptchFrame", "name", "");
					operation.execute(allObjects, "CLICK", "SelectCaptchaCheckbox", "xpath", "");
					Thread.sleep(5000);
					operation.execute(allObjects, "SWITCHFROMIFRAME", "", "", "");
					Thread.sleep(5000);
					
					WebElement frame= webdriver.findElement(By.xpath("//iframe[contains(@src, 'https://www.google.com/recaptcha/api2/frame?')]"));
					webdriver.switchTo().frame(frame);
					
					do
						{
						    attempt = 0;
							operation.executedynamicobject("CLICK", "Image1", "xpath", "");
							operation.execute(allObjects, "CLICK", "Image4", "xpath", "");
							operation.execute(allObjects, "CLICK", "Image5", "xpath", "");
							Thread.sleep(3000);
							operation.execute(allObjects, "CLICK", "CaptchaVerify", "xpath", "");
							Thread.sleep(3000);
							operation.isAlertPresent();
							
							String message1 = operation.execute(allObjects, "GETTEXT", "CaptchaRetryMessage1", "xpath", "");
							String message2 = operation.execute(allObjects, "GETTEXT", "CaptchaRetryMessage2", "xpath", "");
							String exp = "Multiple correct solutions required - please solve more.";
							String exp1 = "Please also check the new images.";
							if (message1.equals(exp) || message2.equals(exp1))
									{ 
								      count = 1;
								      attempt = attempt + 1;
									}
							else
							{
								count = 0;
								attempt = attempt + 1;
								break;
							}
						}
					while (count == 1 && attempt < 11);
					operation.execute(allObjects, "SWITCHFROMIFRAME", "", "", "");
				}
			while (count == 1);
			
			System.out.println("attempted count = "+attempt);
			operation.execute(allObjects, "SWITCHFROMIFRAME", "", "", "");
			Thread.sleep(5000);
						
			operation.execute(allObjects, "SELECTBYINDEX", "OTPCCType", "css", "MasterCard");
			operation.execute(allObjects, "CLEAR", "OTPCCNumber", "css", "");
			operation.execute(allObjects, "SENDKEYS", "OTPCCNumber", "css", "5555555555554444");
			operation.execute(allObjects, "SELECTBYINDEX", "OTPExpMonth", "css", "");	
			operation.execute(allObjects, "SELECTBYINDEX", "OTPExpYear", "css", "");
			operation.execute(allObjects, "SENDKEYS", "OTPSecurityCode", "css", "123");	
			operation.execute(allObjects, "CLICK", "donate", "css", "");
			operation.execute(allObjects, "CLICK", "popupconfirmation", "css", "");	
			
			Thread.sleep(3000);
			//writing donation number to notepad file
			String donationno= operation.execute(allObjects, "GETTEXT", "GuestDonationNumber", "xpath", "");	
			WriteTextFile objWrite = new WriteTextFile();
			String filePath = System.getProperty("user.dir")+"\\TestData\\AutogeneratedTestData\\GuestDonation.txt";
			objWrite.Writedata(filePath, donationno);
			
					
			String query = "SELECT customer_type FROM customer WHERE email = '"+email+"'";
			String customer_type = SeleniumOperation.querydb(query, "brandywine_erp");
			Assert.assertEquals("Guest", customer_type);
						
			webdriver.quit();
		}
		catch (NoSuchElementException e) 
		{	
			e.printStackTrace();
			webdriver.quit();
		}	
		
	}
}
