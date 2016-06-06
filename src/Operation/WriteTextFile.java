package Operation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class WriteTextFile 
{

		public String Writedata(String filePath, String data) throws IOException
		    {
		     FileWriter fr=new FileWriter(filePath);
		     BufferedWriter br=new BufferedWriter(fr);
		     br.write(data);
		     br.newLine();
		     br.close();
			return data;
		    }
}
