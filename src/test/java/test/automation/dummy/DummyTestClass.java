package test.automation.dummy;


import java.io.IOException;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import test.automation.framework.driver.Driver;

public class DummyTestClass {
	Driver testSuiteDriver;
	
	@BeforeSuite
	public void setup() throws IOException {
		testSuiteDriver = Driver.getDriver();
		testSuiteDriver.suiteSetup();
	}
	
	@Test
	public void test() {
		
	}
}