package test.automation.framework.library.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyFileUtils {
	/* Variables */
	private Properties properties;
	
	/* Constructors */
	/**
	 * PropertyFileUtils Constructor - Initializes Properties Object for the file path specified
	 * @param propertyFilePath
	 * @throws IOException
	 */
	public PropertyFileUtils(String propertyFilePath) throws IOException {
		InputStream inStream = new FileInputStream(propertyFilePath);
		Properties tempProp = new Properties();
		tempProp.load(inStream);
		setProperties(tempProp);
		inStream.close();
	}
	
	/* Getters and Setters */
	public Properties getProperties() {
		return properties;
	}
	
	private void setProperties(Properties properties) {
		this.properties = properties;
	}
	
	/* Functions */
	/**
	 * getPropValue function, returns value of the property key identified by parameter
	 * @param propertyKey
	 * @return String value of the property key, returns null if not found
	 */
	public String getPropValue(String propertyKey) {
		return getProperties().getProperty(propertyKey, null);
	}
}