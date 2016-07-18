package Operation;

import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;

import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.TakesScreenshot;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SeleniumOperation
{
	static WebDriver driver;

	public SeleniumOperation(WebDriver driver) throws Exception 
		{
			backupreport();
			SeleniumOperation.driver = driver;
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
			//Thread.sleep(5000);
		}
	
	public String execute(Properties p, String operation, String objectName, String objectType, String value) throws Exception 
		{
				Thread.sleep(3000);
				try
				{
					if (operation.toUpperCase()== "CLICK")
					{
						driver.findElement(SeleniumOperation.getObject(p, objectName, objectType)).click();
					}
					else if (operation.toUpperCase()== "SELECTLASTINDEX")
					{
						WebElement we = driver.findElement(SeleniumOperation.getObject(p, objectName, objectType));
						Select mySelect= new Select(we);
						int indexsize = mySelect.getOptions().size();
					    int size = indexsize - 1;
					    mySelect.selectByIndex(size);
					}
					else if (operation.toUpperCase()== "SENDKEYS")
					{
						driver.findElement(SeleniumOperation.getObject(p, objectName, objectType)).sendKeys(value);
					}
					else if (operation.toUpperCase()== "ACCESSURL")
					{
						driver.get(value);
					}
					else if (operation.toUpperCase()== "GETATTRIBUTE")
					{
						driver.findElement(SeleniumOperation.getObject(p, objectName, objectType)).getAttribute(value);
					}
					else if (operation.toUpperCase()== "GETCSSVALUE")
					{
						driver.findElement(SeleniumOperation.getObject(p, objectName, objectType)).getCssValue(value);
					}
					else if (operation.toUpperCase()== "GETLOCATION")
					{
						driver.findElement(SeleniumOperation.getObject(p, objectName, objectType)).getLocation();
					}
					else if (operation.toUpperCase()== "CLEAR")
					{
						driver.findElement(SeleniumOperation.getObject(p, objectName, objectType)).clear();
					}
					else if (operation.toUpperCase()== "SELECTBYINDEX")
					{
						new Select(driver.findElement(SeleniumOperation.getObject(p, objectName, objectType))).selectByIndex(2);
					}
					else if (operation.toUpperCase()== "SELECTBYINDEX1")
					{
						new Select(driver.findElement(SeleniumOperation.getObject(p, objectName, objectType))).selectByIndex(1);
					}
					else if (operation.toUpperCase()== "SWICTHTOIFRAME")
					{
						driver.switchTo().frame(driver.findElement(SeleniumOperation.getObject(p, objectName, objectType)));
					}
					else if (operation.toUpperCase()== "SWITCHFROMIFRAME")
					{
						driver.switchTo().defaultContent();
					}
					else if (operation.toUpperCase()== "SELECT")
					{
						new Select(driver.findElement(SeleniumOperation.getObject(p, objectName, objectType))).selectByValue(value);
					}
					else if (operation.toUpperCase()== "GETTEXT")
					{
						String values = driver.findElement(SeleniumOperation.getObject(p, objectName, objectType)).getText();
						return values;
					}
					else if (operation.toUpperCase()== "GETTEXTVALUE")
					{
						String data = driver.findElement(SeleniumOperation.getObject(p, objectName, objectType)).getAttribute("value");
						return data;
					}
					else if (operation.toUpperCase()== "SCROLLBROWSER")
					{
						JavascriptExecutor jse = (JavascriptExecutor)driver;
						jse.executeScript("window.scrollBy(0,500)", "");
					}
								
					}
				catch (Exception e)
				{
		    		//calls the method to take the screenshot.
		    		getscreenshot();
		    		System.out.println("TestScript Failed....");
		    		driver.quit();
		    		throw new RuntimeException(e);
		    		//System.out.println(e);
		    	 }
				return value;
	}
	
	private static By getObject(Properties p, String objectName, String objectType) throws Exception 
	{
		// Find by xpath
		if (objectType.equalsIgnoreCase("XPATH")) 
		{
			return By.xpath(p.getProperty(objectName));
		}
		// find by class
		else if (objectType.equalsIgnoreCase("CLASSNAME")) 
		{
			return By.className(p.getProperty(objectName));
		}
		// find by name
		else if (objectType.equalsIgnoreCase("NAME")) 
		{
			return By.name(p.getProperty(objectName));
		}
		// find by id
		else if (objectType.equalsIgnoreCase("ID")) 
		{
			return By.id(p.getProperty(objectName));
		}
		// Find by css
		else if (objectType.equalsIgnoreCase("CSS")) 
		{
			return By.cssSelector(p.getProperty(objectName));
		}
		// find by linkText
		else if (objectType.equalsIgnoreCase("LINK")) 
		{
			return By.linkText(p.getProperty(objectName));
		}
		// find by partial link
		else if (objectType.equalsIgnoreCase("PARTIALLINK")) 
		{
			return By.partialLinkText(p.getProperty(objectName));
		} else 
		{
			throw new Exception("Wrong object type");
		}
}
	
	public String executedynamicobject(String operation, String a, String objectType, String value) throws Exception 
	{
			try
			{
				if (operation.toUpperCase()== "CLICK")
				{
					driver.findElement(SeleniumOperation.getDynamicObject(objectType, a)).click();
				}
				else if (operation.toUpperCase()== "SENDKEYS")
				{
					driver.findElement(SeleniumOperation.getDynamicObject(objectType, a)).sendKeys(value);
				}
				else if (operation.toUpperCase()== "CLEAR")
				{
					driver.findElement(SeleniumOperation.getDynamicObject(objectType, a)).clear();
				}
				else if (operation.toUpperCase()== "SELECTBYINDEX")
				{
					new Select(driver.findElement(SeleniumOperation.getDynamicObject(objectType, a))).selectByIndex(2);
				}
				else if (operation.toUpperCase()== "GETTEXT")
				{
					String values = driver.findElement(SeleniumOperation.getDynamicObject(objectType, a)).getText();
					return values;
				}
				return value;
						
		}
			catch (Exception e)
			{
	    		//calls the method to take the screenshot.
	    		getscreenshot();
	    		throw new RuntimeException(e);
	     	}
	}
	
	private static By getDynamicObject(String objectType, String dynamicLocation) throws Exception 
	{
		// Find by xpath
		if (objectType.equalsIgnoreCase("XPATH")) 
		{
			return By.xpath(dynamicLocation);
		}
		// find by class
		else if (objectType.equalsIgnoreCase("CLASSNAME")) 
		{
			return By.className(dynamicLocation);
		}
		// find by name
		else if (objectType.equalsIgnoreCase("NAME")) 
		{
			return By.name(dynamicLocation);
		}
		// find by id
		else if (objectType.equalsIgnoreCase("ID")) 
		{
			return By.id(dynamicLocation);
		}
		// Find by css
		else if (objectType.equalsIgnoreCase("CSS")) 
		{
			return By.cssSelector(dynamicLocation);
		}
		// find by linkText
		else if (objectType.equalsIgnoreCase("LINK")) 
		{
			return By.linkText(dynamicLocation);
		}
		// find by partial link
		else if (objectType.equalsIgnoreCase("PARTIALLINK")) 
		{
			return By.partialLinkText(dynamicLocation);
		} else 
		{
			throw new Exception("Wrong object type");
		}
}
		
	public static boolean isdisplayed(Properties p, String operation, String objectName, String objectType, String value) throws Exception 
	{
			try
			{
				if (operation.toUpperCase()== "ISDISPLAYED")
				{
					boolean exists = driver.findElement(SeleniumOperation.getObject(p, objectName, objectType)).isDisplayed();
					return exists;
				}
				else
				{
					return false;
				}
			}
			catch (Exception e)
			{
				getscreenshot();
				return false;
			}
	}
	
    public static boolean verifyTextPresent(String value) throws Exception 
	{
	  
    	return driver.getPageSource().contains(value);
	  
	}
	
	public static String querydb(String query, String database) throws Exception 
	{
		try
		{
			String drv = "com.mysql.jdbc.Driver";
		    String url = "jdbc:mysql://stage-all.ccmx3ca7iwb0.us-west-1.rds.amazonaws.com/"+database+"";
		    String username = "root";
		    String password = "br0nd1#321";
		    Class.forName(drv);
		    Connection conn = DriverManager.getConnection(url, username, password);
		    Statement st = conn.createStatement();
		       
		    ResultSet rs = st.executeQuery(query);
		    String db_email = null;
		    while(rs.next())
		    	{
		    		db_email=rs.getString(1);
		    		break;
		    	}
		    return db_email;
		}
		catch (Exception ex) 
			{
				System.out.println("Error while connecting to database:" + ex);
				return null;
			}
	}
	
	public static String cookie(String operation, String cookiename, String value) throws Exception 
	{
		try
		{
			if (operation == "GETCOOKIE")
			{
				Cookie cookieval = driver.manage().getCookieNamed(cookiename);
				String cookievalue= cookieval.getValue();
				return cookievalue;
			}
			else if (operation == "DELETECOOKIE")
			{
				driver.manage().deleteCookieNamed(cookiename);
			}
			else if (operation == "ADDCOOKIE")
			{
				Cookie newcookievalue = new Cookie(cookiename, value);
				driver.manage().addCookie(newcookievalue);
			}
		}
		catch (Exception e)
		{
			getscreenshot();
		}
		return null;
	}
	
	public static void getscreenshot() throws Exception 
    {
            File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            Calendar cal = Calendar.getInstance();
            String Screenshotlocation = System.getProperty("user.dir")+"\\FailureScreenshots";
            FileUtils.copyFile(scrFile, new File(Screenshotlocation+"/failure"+cal.get(Calendar.HOUR)+cal.get(Calendar.MINUTE)+cal.get(Calendar.SECOND)+".png"));
    }
	
	public static void backupreport() throws Exception
	{
		String reportlocation = System.getProperty("user.dir")+"\\test-output";
		File file = new File(reportlocation);
		Calendar cal = Calendar.getInstance();
		String backuplocation = System.getProperty("user.dir")+"\\TestResults\\TestNG_Report_"+cal.get(Calendar.YEAR)+"_"+cal.get(Calendar.DAY_OF_MONTH)+"_"+(cal.get(Calendar.MONTH)+1)+"_"+cal.get(Calendar.HOUR)+"-"+cal.get(Calendar.MINUTE)+"-"+cal.get(Calendar.SECOND);
	    File file2 = new File(backuplocation);
		if(file2.exists()) throw new java.io.IOException("file exists");
	    FileUtils.copyDirectory(file, file2);
	}
	
	public static void RobotUploadFile(String FileName) throws Exception 
    {
		//Providing file's absolute path to the clip board
		StringSelection ss = new StringSelection(FileName);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
		
		//native key strokes for CTRL, V and ENTER keys
		Robot robot = new Robot();
		robot.keyPress(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_V);
		robot.keyRelease(KeyEvent.VK_CONTROL);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
    }
	
	public static void RobotSaveFile() throws Exception 
    {
		Robot rb =new Robot();
		rb.keyPress(KeyEvent.VK_ENTER);
		rb.keyRelease(KeyEvent.VK_ENTER);
    }
	
	public static void CreatePDFFile(String FileName) throws Exception
	{
		Document document = new Document();
		String savelocation = System.getProperty("user.home") + "/Desktop";
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(savelocation+"/"+FileName+".pdf"));
		
		document.open();
        document.add(new Paragraph("Auto created file for TR# "+FileName));
        document.close();
        writer.close();
	}
	public boolean isAlertPresent() throws Exception
	{ 
	    try 
	    { 
	        driver.switchTo().alert().accept(); 
	        return true; 
	    }   
	    catch (NoAlertPresentException Ex) 
	    { 
	        return false; 
	    }    
	} 
}