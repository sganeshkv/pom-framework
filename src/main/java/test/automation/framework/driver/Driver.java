package test.automation.framework.driver;

import java.io.IOException;

import org.apache.commons.lang3.math.NumberUtils;

import test.automation.framework.library.utils.ExcelUtils;
import test.automation.framework.library.utils.PropertyFileUtils;

public class Driver {

	// Variables
	private String environmentUrl;
	private int minWaitTime;
	private int maxWaitTime;
	private static Driver driver = new Driver();
	private PropertyFileUtils config;
	private String testConfigPath;
	private String testConfigTestParamsSheet;

	private Driver() {
		try {
			setConfig(new PropertyFileUtils("src/main/resources/config.properties"));
			setTestConfigPath(getConfig().getPropValue("TestCaseConfigPath"));
			setTestConfigTestParamsSheet(getConfig().getPropValue("TestCaseConfigSheetName"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static Driver getDriver() {
		return driver;
	}

	public String getEnvironmentUrl() {
		return environmentUrl;
	}

	public void setEnvironmentUrl(String environmentUrl) {
		this.environmentUrl = environmentUrl;
	}

	public int getMinWaitTime() {
		return minWaitTime;
	}

	public void setMinWaitTime(int minWaitTime) {
		this.minWaitTime = minWaitTime;
	}

	public int getMaxWaitTime() {
		return maxWaitTime;
	}

	public void setMaxWaitTime(int maxWaitTime) {
		this.maxWaitTime = maxWaitTime;
	}

	public PropertyFileUtils getConfig() {
		return config;
	}

	public void setConfig(PropertyFileUtils config) {
		this.config = config;
	}

	public String getTestConfigPath() {
		return testConfigPath;
	}

	public void setTestConfigPath(String testConfigPath) {
		this.testConfigPath = testConfigPath;
	}

	public String getTestConfigTestParamsSheet() {
		return testConfigTestParamsSheet;
	}

	public void setTestConfigTestParamsSheet(String testConfigTestParamsSheet) {
		this.testConfigTestParamsSheet = testConfigTestParamsSheet;
	}

	// Functions
	public void suiteSetup() throws IOException {
		setEnvironmentUrl(String.valueOf(ExcelUtils.getCellValue(getTestConfigPath(), getTestConfigTestParamsSheet(),
				"Value", "TestParameter=EnvironmentURL", false)));
		setMinWaitTime(NumberUtils.toInt(String.valueOf(ExcelUtils.getCellValue(getTestConfigPath(),
				getTestConfigTestParamsSheet(), "Value", "TestParameter=MinWaitTime", false))));
		setMaxWaitTime(NumberUtils.toInt(String.valueOf(ExcelUtils.getCellValue(getTestConfigPath(),
				getTestConfigTestParamsSheet(), "Value", "TestParameter=MaxWaitTime", false))));
	}
}