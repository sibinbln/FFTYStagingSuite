package TestCases.Magento;

import java.util.Properties;
import java.util.Calendar;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import Operation.ExecutionStatusChecker;
import Operation.Launchbrowser;
import Operation.ReadExcel;
import Operation.ReadObjectRepository;
import Operation.SeleniumOperation;
import Operation.WriteExcel;
import Operation.WriteTextFile;
//import TestCases.Magento.SignupEmailConfirmationCheck;

import java.util.NoSuchElementException;

public class Signup 
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
	public void testSignup() throws Exception 
	{
		ReadExcel re = new ReadExcel();
		String xllocation = System.getProperty("user.dir")+"\\TestData\\TestData.xls";
		re.setInputFile(xllocation, 0);
		String[][] data = re.readFile();
		re.setInputFile(xllocation, 1);
		String[][] values = re.readFile();
				
		try 
		{
			
			//----operation.perform(allObjects, Keyword, ObjectName, Object Type, Value)----
			operation.execute(allObjects, "ACCESSURL", "", "", data[0][4]);
			operation.execute(allObjects, "CLICK", "SignUpButton", "link", "");
			operation.execute(allObjects, "CLEAR", "ZIPCODETEXTFIELD", "className", "");
			operation.execute(allObjects, "SENDKEYS", "ZIPCODETEXTFIELD", "className", values[1][0]);
			operation.execute(allObjects, "CLICK", "GOBUTTON", "id", "");
			operation.execute(allObjects, "SENDKEYS", "ADDRESSTYPE", "id", values[1][16]);
			
			Calendar cal = Calendar.getInstance();
			//Email contains year + day of month + month + hour + minute + seconds
			String email = "onlinecust"+cal.get(Calendar.YEAR)+cal.get(Calendar.DAY_OF_MONTH)+(cal.get(Calendar.MONTH)+1)+cal.get(Calendar.HOUR)+cal.get(Calendar.MINUTE)+cal.get(Calendar.SECOND)+"@yopmail.com";
			
			operation.execute(allObjects, "SENDKEYS", "EMAILADDRESS", "id", email);
			operation.execute(allObjects, "SENDKEYS", "PROMOCODE", "id", values[1][1]);
			operation.execute(allObjects, "SENDKEYS", "PASSWORD", "id", values[1][2]);
			operation.execute(allObjects, "SENDKEYS", "CONFIRMPASSWORD", "id", values[1][2]);
			operation.execute(allObjects, "CLICK", "CONTINUEPAGE1", "xpath", "");
			operation.execute(allObjects, "CLICK", "SERVICESIZE", "xpath", "");
			operation.execute(allObjects, "SENDKEYS", "FREQUENCY", "id", values[1][3]);
			operation.execute(allObjects, "SELECTBYINDEX1", "DATE", "id", "");
			operation.execute(allObjects, "CLICK", "CONTINUEPAGE2", "xpath", "");
			operation.execute(allObjects, "SENDKEYS", "FIRSTNAME", "id", values[1][4]);
			operation.execute(allObjects, "SENDKEYS", "LASTNAME", "id", values[1][5]);
			operation.execute(allObjects, "SENDKEYS", "DELIVERYADDRESS", "id", values[1][6]);
			operation.execute(allObjects, "SENDKEYS", "BUILDINGTYPE", "id", values[1][7]);
			operation.execute(allObjects, "SENDKEYS", "BUILDINGNAME", "id", values[1][8]);
			operation.execute(allObjects, "SENDKEYS", "CROSSSTREET", "id", values[1][9]);
			operation.execute(allObjects, "SENDKEYS", "DELIVERYINSTRUCTIONS", "id", values[1][10]);
			operation.execute(allObjects, "CLICK", "ISGATECODEREQ", "xpath", "");
			operation.execute(allObjects, "CLICK", "ISDELIVERYKEYREQ", "xpath", "");
			operation.execute(allObjects, "SENDKEYS", "PHONENUMBER", "id", values[1][11]);
			operation.execute(allObjects, "SENDKEYS", "MOBILENUMBER", "id", values[1][12]);
			operation.execute(allObjects, "CLICK", "ISBILLLINGSAMEASDEL", "xpath", "");
			operation.execute(allObjects, "SENDKEYS", "CCTYPE", "id", values[1][13]);
			operation.execute(allObjects, "SENDKEYS", "CCNUMBER", "id", values[1][14]);
			operation.execute(allObjects, "SELECTBYINDEX", "CCEXPIRYMONTH", "id", "");
			operation.execute(allObjects, "SELECTBYINDEX", "CCEXPIRYYEAR", "id", "");
			operation.execute(allObjects, "SENDKEYS", "CCSECURITYCODE", "css", values[1][15]);
			operation.execute(allObjects, "CLICK", "CONTINUEPAGE3", "css", "");
			operation.execute(allObjects, "CLICK", "CONFIRMORDER", "css", "");
			operation.execute(allObjects, "CLICK", "CLOSEBUTTON", "css", "");
			operation.execute(allObjects, "CLICK", "MYACCOUNTSELECTOR", "css", "");
			
				//To verify if the customer is created in ERP database
				String account_number = operation.execute(allObjects, "GETTEXT", "ACC_NUMBER", "xpath", "");
				Thread.sleep(5000);
				String acc_no = account_number.substring(account_number.lastIndexOf(" "));
				String ac_no = acc_no.replace("#", "");
				String query ="SELECT email FROM customer WHERE account_number = "+ac_no+"";
				String custemail = SeleniumOperation.querydb(query, "brandywine_erp");
				Thread.sleep(5000);
				Assert.assertEquals(email,custemail);
				
				//Writing customer email to a text file, this data is used by AddSubscription and DeleteSubscription scripts.
				WriteTextFile objWrite = new WriteTextFile();
				String filePath = System.getProperty("user.dir")+"\\TestData\\AutogeneratedTestData\\Subscription.txt";
				objWrite.Writedata(filePath, email);
				
				//Write newly created customer email to excel file for future reference
				WriteExcel objExcelFile = new WriteExcel();
				String[] Email = new String[] {email};
				objExcelFile.writeExcel(System.getProperty("user.dir")+"\\TestData","CustomerEmails.xls","Emails",Email);
				
			operation.execute(allObjects, "CLICK", "LOGOUT", "css", "");
			webdriver.quit();
			
			//SignupEmailConfirmationCheck ecc = new SignupEmailConfirmationCheck(); // To check the welcome email
			//ecc.ExecutionStatusCheck();
			//ecc.testSignupEmailConfirmationCheck();
		}
		catch (NoSuchElementException e) 
		{	
			e.printStackTrace();
			webdriver.quit();
		}
		
	}
}
