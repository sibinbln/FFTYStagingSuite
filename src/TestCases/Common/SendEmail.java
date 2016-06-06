package TestCases.Common;

import org.testng.annotations.Test;

import Operation.SendMail;
public class SendEmail 
{

	@Test
	public void testsendEmail() throws Exception 
	{
		SendMail.initialize("emailable-report.html");
		//SendMail.initialize("index.html");
	}

}
