package test.automation.testcase.testclasses;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import test.automation.application.pages.MainPage;
import test.automation.application.pages.SearchPage;
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
		MainPage mainPage = PageFactory.initElements(driver, MainPage.class);
		Assert.assertTrue(mainPage.isPageOpen(), "Page did not open successfully");
		
		SearchPage searchPage = mainPage.searchText("Booyakasha");
		Assert.assertTrue(searchPage.isPageOpen(), "Page did not open successfully");
		
		driver.quit();
	}
	
	@AfterMethod
	public void afterTest(ITestResult result) {
		
	}
}