package test.automation.framework.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	//TODO
	//private static final String ConditionSeparator = ";";
	//private static final String ConditionValueSeparator = "=";

	/**
	 * <b>createExcel - Creates Excel</b>
	 * 
	 * @param filePath
	 *            - Full Qualified excel Path
	 * @param sheetnames
	 *            -names of sheets if any, else default Sheet1
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean createExcel(String filePath, String... sheetnames) throws Exception {
		boolean flag = false;
		String[] sheets;
		File excelFile = new File(filePath);
		FileOutputStream fos = null;

		if (excelFile.exists())
			throw new IOException("File " + excelFile.getName() + " already exists. Please check and try again");

		if (!excelFile.getParentFile().exists())
			excelFile.getParentFile().mkdirs();

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

	/**
	 * 
	 * @param excelPath
	 * @param sheetname
	 * @param rownum
	 * @param columnnum
	 * @return Object
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	public static Object getCellValue(String excelPath, String sheetname, int rownum, int columnnum)
			throws IOException {
		Object value = null;
		Workbook workbook = null;
		Cell cell = null;
		File excelPath_f = new File(excelPath);
		FileInputStream fis = null;
		CellValue cellvalue = null;

		if (!excelPath_f.exists())
			throw new FileNotFoundException("File does not exist - " + excelPath);

		fis = new FileInputStream(excelPath_f);

		if (excelPath_f.getName().contains(".xlsx")) {
			workbook = new XSSFWorkbook(fis);
			XSSFSheet sheet = ((XSSFWorkbook) workbook).getSheet(sheetname);
			try {
				cell = sheet.getRow(rownum).getCell(columnnum);
			} catch (NullPointerException e) {
				return null;
			} finally {
				workbook.close();
				fis.close();
			}
		} else {
			workbook = new HSSFWorkbook(fis);
			HSSFSheet sheet = ((HSSFWorkbook) workbook).getSheet(sheetname);
			try {
				cell = sheet.getRow(rownum).getCell(columnnum);
			} catch (NullPointerException e) {
				return null;
			} finally {
				workbook.close();
				fis.close();
			}
		}

		cellvalue = workbook.getCreationHelper().createFormulaEvaluator().evaluate(cell);

		switch (cellvalue.getCellType()) {

		case Cell.CELL_TYPE_STRING:
			value = cell.getStringCellValue();
			break;

		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				value = cell.getDateCellValue();
			} else {
				value = cell.getNumericCellValue();
			}
			break;

		case Cell.CELL_TYPE_BLANK:
			value = "";
			break;

		case Cell.CELL_TYPE_BOOLEAN:
			value = cell.getBooleanCellValue();
			break;
		default:
			break;
		}

		workbook.close();
		fis.close();
		return value;
	}
}