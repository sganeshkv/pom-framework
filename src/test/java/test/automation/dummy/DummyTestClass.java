package test.automation.dummy;


import test.automation.framework.library.utils.ExcelUtils;

public class DummyTestClass {
	public static void main(String[] args) throws Exception {
		System.out.println(ExcelUtils.getExcelSheetData("one.xlsx", "Sheet1",true));
	}
}