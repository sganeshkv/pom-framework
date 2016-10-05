package test.automation.framework.library.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
	public static boolean createExcel(String filePath, String... sheetnames) throws Exception {
		boolean flag = false;
		String[] sheets;
		File excelFile = new File(filePath);
		FileOutputStream fos = null;

		if (excelFile.exists())
			throw new IOException("File " + excelFile.getName() + " already exists. Please check and try again");

		if (sheetnames.length >= 1 && sheetnames != null)
			sheets = sheetnames;
		else {
			sheets = new String[1];
			sheets[0] = "Sheet1";
		}

		if (excelFile.getName().toLowerCase().contains(".xlsx")) {
			fos = new FileOutputStream(excelFile);
			XSSFWorkbook excelBook = new XSSFWorkbook();
			for (String sheet : sheets) {
				excelBook.createSheet(sheet);
			}
			excelBook.write(fos);
			excelBook.close();
			fos.close();
		} else {
			fos = new FileOutputStream(excelFile);
			HSSFWorkbook excelBook = new HSSFWorkbook();
			for (String sheet : sheets) {
				excelBook.createSheet(sheet);
			}
			excelBook.write(fos);
			excelBook.close();
			fos.close();
		}
		return flag;
	}
}