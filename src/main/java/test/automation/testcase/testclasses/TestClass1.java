package test.automation.testcase.testclasses;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import test.automation.framework.driver.Driver;
import test.automation.framework.library.utils.BrowserTypes;
import test.automation.framework.library.utils.UIUtils;

public class TestClass1 {
	Driver testSuiteDriver;

	@BeforeSuite
	public void setup(ITestContext thisSuiteContext) throws IOException {
		testSuiteDriver = Driver.getDriver();
		testSuiteDriver.suiteSetup();
	}

	@Test
	public void test() throws Exception {
		WebDriver driver = UIUtils.getWebDriver(testSuiteDriver.getEnvironmentUrl(), BrowserTypes.CHROME, false);
		UIUtils.waitForPageLoad(driver,60);
		driver.quit();
	}
}