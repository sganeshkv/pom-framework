package test.automation.dummy;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;

import test.automation.application.pages.MainPage;

public class DummyTestClass {
	@Test
	public void test() {
		System.setProperty("webdriver.chrome.driver", "src/main/resources/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		MainPage mp = PageFactory.initElements(driver, MainPage.class);
		mp.openPage();
	}
}