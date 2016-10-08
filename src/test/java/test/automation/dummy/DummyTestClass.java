package test.automation.dummy;


import test.automation.framework.library.utils.ExcelUtils;

public class DummyTestClass {
	public static void main(String[] args) throws Exception {
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", 1, 3));
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", "Salary", "Name=A3"));
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", "Salary", "Name=A3;EmpId=104.0"));
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", "Salary", "Name=A3;EmpId=4.0"));
		System.out.println(ExcelUtils.getCellValue("one.xlsx", "Sheet1", "Salary", "Name=a4;EmpId=4;DOJ=01/05/1987"));
	}
}