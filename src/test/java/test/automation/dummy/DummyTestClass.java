package test.automation.dummy;

import java.text.ParseException;

import test.automation.framework.library.utils.RestServiceUtils;

public class DummyTestClass {
	public static void main(String[] args) throws ParseException {
		System.out.println(RestServiceUtils.http_get("http://www.thomas-bayer.com/sqlrest").asString());
}
}
