package Operation;

import java.io.File;
import java.io.FileInputStream;
//import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadObjectRepository 
{
	Properties p = new Properties();

	public Properties getObject() throws Exception 
	{
		// Read object repository file
		InputStream stream = new FileInputStream(new File(System.getProperty("user.dir") + "\\ObjectRepository\\object.properties"));
		// load all objects
		p.load(stream);
		return p;
	}

}
