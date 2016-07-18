package Operation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

public class Launchbrowser 
{
	static WebDriver webdriver;

	public static String getBrowserConfig() throws Exception 
	{
		String fileName = System.getProperty("user.dir")+"\\BrowserConfig\\config.txt";
		    String line = null;

	        try {
	            FileReader fr = new FileReader(fileName);
	            BufferedReader br = new BufferedReader(fr);
	            line=br.readLine();
	            br.close();
	            fr.close();
	        }
	         catch (Exception e)
	         {
	        	 System.out.println(e);
	         }
			return line;
	        
	}

	public static WebDriver launchBrowser() throws Exception {
		String browser = Launchbrowser.getBrowserConfig();
		System.out.println("Browser="+browser);
		if (browser.equals("Firefox"))
		{
			webdriver = new FirefoxDriver();
			return webdriver;
		}
		else if (browser.equals("Chrome"))
		{
			String chromedriverlocation = "../BrowserExtension/Chromedriver/chromedriver.exe";
			File file = new File(chromedriverlocation);
		    System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());
			webdriver = new ChromeDriver();
		}
		else if (browser.equals("IE"))
		{
			String iedriverlocation = "../BrowserExtension/IEdriver/IEDriverServer.exe";
			File file = new File(iedriverlocation);
		    System.setProperty("webdriver.ie.driver", file.getAbsolutePath());
			webdriver = new InternetExplorerDriver();
		}
			
		return webdriver;
	}
}
