package test.automation.application.pages;

import java.io.IOException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import test.automation.framework.library.utils.PropertyFileUtils;

public class MainPage implements BasePage {

	private WebDriver driver;
	private PropertyFileUtils locatorPropertyFile;
	
	public MainPage(WebDriver driver) throws IOException {
		this.driver = driver;
		locatorPropertyFile = new PropertyFileUtils("");
	}
	
	@Override
	public void openPage() {
		driver.findElement(By.xpath(locatorPropertyFile.getPropValue(""))).click();		
	}

	@Override
	public boolean isPageOpen() {
		// TODO Auto-generated method stub
		return false;
	}
}