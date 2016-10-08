package test.automation.dummy;


import test.automation.framework.library.utils.ExcelUtils;

public class DummyTestClass {
	public static void main(String[] args) throws Exception {
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Data", 1, 3));
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Data", "Salary", "Name=A3"));
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Data", "Salary", "Name=A3;EmpId=104.0"));
	}
}