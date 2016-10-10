package test.automation.dummy;


import java.util.Date;

import test.automation.framework.library.utils.ExcelUtils;

public class DummyTestClass {
	public static void main(String[] args) throws Exception {
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", 1, 3));
		ExcelUtils.setCellValue("one.xlsx", "Sheet1", 1, 3, new Date());
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", 1, 3));
	}
}