package test.automation.dummy;

import test.automation.framework.library.utils.ExcelUtils;

public class DummyTestClass {
	public static void main(String[] args) throws Exception {
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", 0, 0));
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", 1, 0));
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", 2, 0));
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", 3, 0));
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", 4, 0));
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", 5, 0));
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", 6, 0));
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", 7, 0));
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", 8, 0));
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", 18, 0));
		System.out.println(ExcelUtils.getCellValue("two.xls", "Sheet1", 0, 0));
		System.out.println(ExcelUtils.getCellValue("two.xls", "Sheet1", 1, 0));
		System.out.println(ExcelUtils.getCellValue("two.xls", "Sheet1", 2, 0));
	}
}