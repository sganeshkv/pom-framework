package test.automation.framework.library.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.security.UserAndPassword;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UIUtils {
	private static PropertyFileUtils config;
	private static int defaultWaitTime;
	public static ExpectedCondition<Boolean> waitForPageLoad;

	public static int getDefaultWaitTime() {
		return defaultWaitTime;
	}

	public static void setDefaultWaitTime(int defaultWaitTime) {
		UIUtils.defaultWaitTime = defaultWaitTime;
	}

	static {
		try {
			setConfig(new PropertyFileUtils("src/main/resources/config.properties"));
			setDefaultWaitTime(Integer.valueOf(getConfig().getPropValue("DefaultWaitTime")));
			waitForPageLoad = new ExpectedCondition<Boolean>() {
				@Override
				public Boolean apply(WebDriver driver) {
					try {
						return executeScript(driver, "return document.readyState").equals("complete");
					} catch (Exception e) {
						return false;
					}
				}
			};
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static PropertyFileUtils getConfig() {
		return config;
	}

	public static void setConfig(PropertyFileUtils config) {
		UIUtils.config = config;
	}

	/**
	 * getWebDriver
	 * 
	 * @param url
	 * @param browserType
	 * @param executablePath
	 *            - Set null in case of FireFoxDriver less than 3.0
	 * @return WebDriver
	 * @throws MalformedURLException
	 */
	public static WebDriver getWebDriver(String url, BrowserTypes browserType, boolean executeInGrid)
			throws MalformedURLException {
		WebDriver driver = null;
		DesiredCapabilities dc = null;

		switch (browserType) {
		case CHROME:
			System.setProperty("webdriver.chrome.driver", getConfig().getPropValue("ChromeDriverPath"));
			dc = DesiredCapabilities.chrome();
			ChromeOptions options = new ChromeOptions();
			options.addArguments("test-type");
			dc.setBrowserName(BrowserType.CHROME);
			dc.setCapability(ChromeOptions.CAPABILITY, options);
			driver = executeInGrid ? new RemoteWebDriver(new URL(url), dc) : new ChromeDriver(dc);
			break;
		case FIREFOX:
			if (!StringUtils.isEmpty(getConfig().getPropValue("FirefoxDriverPath")))
				System.setProperty("webdriver.firefox.driver", getConfig().getPropValue("FirefoxDriverPath"));
			dc = DesiredCapabilities.firefox();
			dc.setBrowserName(BrowserType.FIREFOX);
			driver = executeInGrid ? new RemoteWebDriver(new URL(url), dc) : new FirefoxDriver(dc);
			break;
		default:
		case IE:
			System.setProperty("webdriver.ie.driver", getConfig().getPropValue("InternetExplorerDriverPath"));
			dc = DesiredCapabilities.internetExplorer();
			dc.setBrowserName(BrowserType.IE);
			driver = executeInGrid ? new RemoteWebDriver(new URL(url), dc) : new InternetExplorerDriver(dc);
			break;
		}

		driver.get(url);
		driver.manage().window().maximize();
		driver.manage().timeouts().setScriptTimeout(Long.parseLong(getConfig().getPropValue("ScriptTimeoutSeconds")),
				TimeUnit.SECONDS);
		return driver;
	}

	public static WebDriver getWebDriver(URL url, BrowserTypes browserType, boolean executeInGrid)
			throws MalformedURLException {
		return getWebDriver(url.getPath(), browserType, executeInGrid);
	}

	public static Object executeScript(WebDriver driver, String script, Object... args) {
		return ((JavascriptExecutor) (driver)).executeScript(script, args);
	}

	public static Object executeAsyncScript(WebDriver driver, String script, Object... args) {
		return ((JavascriptExecutor) (driver)).executeAsyncScript(script, args);
	}

	public static By getLocatorObject(String locatorType, String locatorValue) {
		By by = null;

		switch (locatorType.toUpperCase()) {
		case "XPATH":
			by = By.xpath(locatorValue);
			break;
		case "ID":
			by = By.id(locatorValue);
			break;
		case "NAME":
			by = By.name(locatorValue);
			break;
		case "TAGNAME":
			by = By.tagName(locatorValue);
			break;
		case "CLASSNAME":
			by = By.className(locatorValue);
			break;
		case "CSSSELECTOR":
			by = By.cssSelector(locatorValue);
			break;
		case "LINKTEXT":
			by = By.linkText(locatorValue);
			break;
		case "PARTIALLINKTEXT":
			by = By.partialLinkText(locatorValue);
			break;
		default:
			break;
		}

		return by;
	}

	public static By getLocatorObject(String locator) {
		return getLocatorObject(locator.split(getConfig().getPropValue("LocatorValueSeparator"))[0],
				locator.split(getConfig().getPropValue("LocatorValueSeparator"))[1]);
	}

	public static WebElement funcFindElement(WebDriver driver, By by) {
		return funcFindElement(driver, by, defaultWaitTime);
	}

	public static WebElement funcFindElement(WebDriver driver, By by, int waitTime) {
		return new WebDriverWait(driver, waitTime).until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	public static WebElement funcFindElement(WebDriver driver, String locator) {
		return funcFindElement(driver, getLocatorObject(locator), defaultWaitTime);
	}

	public static WebElement funcFindElement(WebDriver driver, String locator, int waitTime) {
		return funcFindElement(driver, getLocatorObject(locator), waitTime);
	}

	public static WebElement funcFindElement(WebDriver driver, String locatorType, String locatorValue) {
		return funcFindElement(driver, getLocatorObject(locatorType, locatorValue), defaultWaitTime);
	}

	public static WebElement funcFindElement(WebDriver driver, String locatorType, String locatorValue, int waitTime) {
		return funcFindElement(driver, getLocatorObject(locatorType, locatorValue), waitTime);
	}

	public static void dynWait(WebDriver driver) {

	}

	public static Alert getAlert(WebDriver driver) {
		return getAlert(driver, defaultWaitTime);
	}

	public static Alert getAlert(WebDriver driver, int waitTime) {
		return new WebDriverWait(driver, waitTime).until(ExpectedConditions.alertIsPresent());
	}

	public static void alertAccept(WebDriver driver) {
		getAlert(driver).accept();
	}

	public static void alertDismiss(WebDriver driver) {
		getAlert(driver).dismiss();
	}

	public static void alertAccept(WebDriver driver, int waitTime) {
		getAlert(driver, waitTime).accept();
	}

	public static void alertDismiss(WebDriver driver, int waitTime) {
		getAlert(driver, waitTime).dismiss();
	}

	public static void authenticateAlert(WebDriver driver, String username, String password) {
		authenticateAlert(driver, username, password, defaultWaitTime);
	}

	public static void authenticateAlert(WebDriver driver, String username, String password, int waitTime) {
		getAlert(driver, waitTime).authenticateUsing(new UserAndPassword(username, password));
	}

	public static void setUserAndPasswordAlert(WebDriver driver, String username, String password, int waitTime) {
		getAlert(driver, waitTime).setCredentials(new UserAndPassword(username, password));
	}

	public static void setUserAndPasswordAlert(WebDriver driver, String username, String password) {
		setUserAndPasswordAlert(driver, username, password, defaultWaitTime);
	}

	public static boolean waitForPageLoad(WebDriver driver, int waitTime) {
		return new WebDriverWait(driver, waitTime).until(waitForPageLoad);
	}

	public static boolean waitForPageLoad(WebDriver driver) {
		return waitForPageLoad(driver, defaultWaitTime);
	}
	
	public static void takeScreenshot(WebDriver driver, String filepath) throws IOException {
		takeScreenshot(driver, new File(filepath));
	}
	
	public static void takeScreenshot(WebDriver driver, File filepath) throws IOException {
		if(!filepath.getParentFile().exists())
			filepath.getParentFile().mkdirs();
		FileUtils.moveFile(((TakesScreenshot) (driver)).getScreenshotAs(OutputType.FILE), filepath);
	}
}