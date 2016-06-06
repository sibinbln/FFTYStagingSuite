package Operation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ReadTextFile 
{

			private static BufferedReader br;

			public static String Readdata(String filePath) throws IOException
		    {
		     FileReader fr=new FileReader(filePath);
		     br = new BufferedReader(fr);
		     String input = br.readLine();
		     return input;
		    }
			
			public static String[] returnData(String filePath) throws IOException
		    {
		     FileReader fr=new FileReader(filePath);
		     br = new BufferedReader(fr);
		     String[] assertionarray;
				assertionarray = new String[24];
				  while (br.readLine() != null) 
				  {
					    for (int j = 0; j < assertionarray.length; j++)
					    {
					    	assertionarray[j] = br.readLine();
					    }
				  }
		       return assertionarray;
		    }
}
