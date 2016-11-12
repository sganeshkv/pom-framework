package test.automation.application.pages;

import java.io.File;
import java.io.IOException;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

import test.automation.framework.driver.Driver;
import test.automation.framework.library.utils.PropertyFileUtils;
import test.automation.framework.library.utils.UIUtils;

public class MainPage implements BasePage {

	private WebDriver driver;
	private PropertyFileUtils locatorPropertyFile;
	private Driver testDriver;

	public MainPage(WebDriver driver) throws IOException {
		this.driver = driver;
		testDriver = Driver.getDriver();
		locatorPropertyFile = new PropertyFileUtils(
				testDriver.getConfig().getPropValue("ObjectRepositoryPath") + File.separator + "MainPage.properties");
	}

	@Override
	public void openPage() {
		driver.get(testDriver.getEnvironmentUrl());
	}

	@Override
	public boolean isPageOpen() {
		return UIUtils.funcFindElement(driver,
				UIUtils.getLocatorObject(locatorPropertyFile.getPropValue("searchButton")), testDriver.getMaxWaitTime())
				.isDisplayed();
	}

	public SearchPage searchText(String text) {
		UIUtils.funcFindElement(driver, UIUtils.getLocatorObject(locatorPropertyFile.getPropValue("searchTextBox")))
				.sendKeys(text + Keys.RETURN);
		return PageFactory.initElements(driver, SearchPage.class);
	}
}