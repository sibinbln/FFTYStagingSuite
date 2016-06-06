package Operation;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import org.openqa.selenium.WebDriver;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ExecutionStatusChecker 
{
	static WebDriver webdriver;
	static String[][] data = null;
	static Workbook w;
	static Properties allObjects;
	static SeleniumOperation operation;
	
	public static boolean getExecutionStatus(String ClassName) throws BiffException, IOException
	{
			String ScriptName = ClassName;
			int k = 0;
			String xllocation = System.getProperty("user.dir")+"\\TestData\\ExecutionChecker.xls";
			File inputWorkbook = new File(xllocation);
			w = Workbook.getWorkbook(inputWorkbook);
			Sheet sheet = w.getSheet(0);
            data = new String[sheet.getColumns()][sheet.getRows()];
            
            // Loop over first 10 column and lines
            for (int i = 0; i < sheet.getRows(); ) 
            {
            	for (int j = 0; j < sheet.getColumns(); )
                {
                    Cell cell = sheet.getCell(j, i);
                    data[j][i] = cell.getContents();
                    if (data[j][i].equals(ScriptName))
                    {
                    	k = i;
                     }
                    if (data[j][k].equals("yes"))
                    {
                    	System.out.println("Executing script: "+ScriptName);
                    	return true;
                    }
                    j=j+1;
                }
            	i=i+1;
            }
            return false;
		}
	public static void executetestcase(String className, boolean status) throws Exception, BiffException, IOException
	{
		if (status == false)
		{
			try
				{
					webdriver.quit();
				}
			catch(Exception e)
				{
				throw new Exception(className+" : Execution status is set to 'No' in 'ExecutionChecker.xls'");
				}
		}
	}
		
}

	
	






