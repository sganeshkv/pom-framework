package test.automation.dummy;


import test.automation.framework.library.utils.ExcelUtils;

public class DummyTestClass {
	public static void main(String[] args) throws Exception {
		System.out.println(ExcelUtils.getExcelSheetDataValues("one.xlsx", "Sheet1", true));
	}
}