package util;

import java.io.InputStream;
import java.util.Properties;

public class DBPropertyUtil {
	
	private DBPropertyUtil() {
		
	}
	
	public static Properties getPropertyString(String file_path) {
		Properties properties = new Properties();
		
		try(InputStream input = DBPropertyUtil.class.getClassLoader().getResourceAsStream(file_path)) {
			if(input == null) {
				System.out.println("Sorry Unable to Find "+file_path);
				return null;
			}
			
			properties.load(input);
		}
		catch(Exception e) {
			e.printStackTrace();
	    }
		return properties;
	}
}
