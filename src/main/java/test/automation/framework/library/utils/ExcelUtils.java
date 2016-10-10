package test.automation.framework.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	private static final String ConditionSeparator = ";";
	private static final String ConditionValueSeparator = "=";
	private static final String ColumnSeparator = "@@@@";
	private static final String ColumnValueSeparator = "::::";

	/**
	 * <b>createExcel - Creates Excel</b>
	 * 
	 * @param excelPath
	 *            - Full Qualified excel Path
	 * @param sheetnames
	 *            -names of sheets if any, else default Sheet1
	 * @return boolean
	 * @throws Exception
	 */
	public static boolean createExcel(String excelPath, String... sheetnames) throws Exception {
		boolean flag = false;
		String[] sheets;
		File excelFile = new File(excelPath);
		FileOutputStream fos = null;
		FileInputStream fis = null;

		if ((excelFile.getParentFile() != null) && (!excelFile.getParentFile().exists()))
			excelFile.getParentFile().mkdirs();

		if (sheetnames.length >= 1 && sheetnames != null)
			sheets = sheetnames;
		else {
			sheets = new String[1];
			sheets[0] = "Sheet1";
		}

		if (excelFile.getName().toLowerCase().contains(".xlsx")) {
			fis = (excelFile.exists()) ? new FileInputStream(excelFile) : null;
			XSSFWorkbook excelBook = (excelFile.exists()) ? new XSSFWorkbook(fis) : new XSSFWorkbook();
			for (String sheet : sheets) {
				if (!doesWorkbookContainSheet(excelBook, sheet))
					excelBook.createSheet(sheet);
			}
			if (fis != null)
				fis.close();
			fos = new FileOutputStream(excelFile);
			excelBook.write(fos);
			excelBook.close();
			fos.close();
		} else {
			fis = (excelFile.exists()) ? new FileInputStream(excelFile) : null;
			HSSFWorkbook excelBook = (excelFile.exists()) ? new HSSFWorkbook(fis) : new HSSFWorkbook();
			for (String sheet : sheets) {
				if (!doesWorkbookContainSheet(excelBook, sheet))
					excelBook.createSheet(sheet);
			}
			if (fis != null)
				fis.close();
			fos = new FileOutputStream(excelFile);
			excelBook.write(fos);
			excelBook.close();
			fos.close();
		}
		return flag;
	}

	private static boolean doesWorkbookContainSheet(Workbook workbook, String sheetname) {
		boolean flag = false;
		for (Sheet sheet : workbook) {
			if (sheet.getSheetName().equalsIgnoreCase(sheetname)) {
				flag = true;
				break;
			}
		}
		return flag;
	}

	/**
	 * createSheetsInExcel
	 * 
	 * @param filePath
	 * @param sheetnames
	 * @return
	 * @throws Exception
	 */
	public static boolean createSheetsInExcel(String excelPath, String... sheetnames) throws Exception {
		return (sheetnames != null && sheetnames.length >= 1) ? createExcel(excelPath, sheetnames) : false;
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
	public static Object getCellValue(String excelPath, String sheetname, int rownum, int columnnum)
			throws IOException {
		Object value = null;
		Workbook workbook = getExcelWorkbook(excelPath);
		Sheet sheet = workbook.getSheet(sheetname);
		Cell cell = null;

		try {
			cell = sheet.getRow(rownum).getCell(columnnum);
		} catch (NullPointerException e) {
			workbook.close();
			return null;
		}
		value = getCellValue(cell);
		workbook.close();
		return value;
	}

	/**
	 * getCellValue
	 * 
	 * @param cell
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("deprecation")
	private static Object getCellValue(Cell cell) throws IOException {
		Object value = null;

		try {
			CellValue cellvalue = cell.getSheet().getWorkbook().getCreationHelper().createFormulaEvaluator()
					.evaluate(cell);
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
		} catch (NullPointerException e) {
			return null;
		}
		return value;
	}

	/**
	 * setCellValue
	 * 
	 * @param excelPath
	 * @param sheetName
	 * @param rowNum
	 * @param columnNum
	 * @param valueToSet
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void setCellValue(String excelPath, String sheetName, int rowNum, int columnNum, Object valueToSet)
			throws IOException, ParseException {
		Workbook workbook = getExcelWorkbook(excelPath);
		Sheet sheet = workbook.getSheet(sheetName);
		Cell cell = null;
		File excelPath_f = new File(excelPath);
		FileOutputStream fos = null;

		try {
			if (sheet.getRow(rowNum) == null)
				cell = sheet.createRow(rowNum).createCell(columnNum);
			else if (sheet.getRow(rowNum).getCell(columnNum) == null) {
				cell = sheet.getRow(rowNum).createCell(columnNum);
			} else {
				cell = sheet.getRow(rowNum).getCell(columnNum);
			}
		} catch (NullPointerException e) {
			workbook.close();
			return;
		}

		if (valueToSet != null) {
			switch (valueToSet.getClass().getSimpleName().toUpperCase()) {
			case "INTEGER":
			case "DOUBLE":
			case "FLOAT":
			case "SHORT":
			case "BYTE":
			case "LONG":
				cell.setCellValue(Double.parseDouble(String.valueOf(valueToSet)));
				cell.setCellType(CellType.NUMERIC);
				break;
			case "DATE":
				CellStyle cellStyle = workbook.createCellStyle();
				CreationHelper createHelper = workbook.getCreationHelper();
				cellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd/MMM/yyyy HH:mm:ss.ms"));
				cell.setCellValue((Date) valueToSet);
				cell.setCellStyle(cellStyle);
				break;
			case "BOOLEAN":
				cell.setCellValue(Boolean.parseBoolean(String.valueOf(valueToSet)));
				cell.setCellType(CellType.BOOLEAN);
				break;
			case "OBJECT":
			case "STRING":
			default:
				cell.setCellValue(String.valueOf(valueToSet));
				cell.setCellType(CellType.STRING);
				break;
			}
		}
		fos = new FileOutputStream(excelPath_f);
		workbook.write(fos);
		workbook.close();
		fos.close();
	}

	/**
	 * getColumnIndex - Returns the columnIndex for the first match, returns -1
	 * if not found
	 * 
	 * @param sheet
	 * @param columnValue
	 * @return
	 * @throws IOException
	 */
	private static int getColumnIndex(Sheet sheet, Object columnName) throws IOException {
		for (Row row : sheet) {
			for (Cell cell : row) {
				if (columnName.equals(getCellValue(cell))) {
					return cell.getColumnIndex();
				}
			}
		}
		return -1;
	}

	/**
	 * 
	 * @param sheet
	 * @param filterCondition
	 * @return
	 * @throws IOException
	 */
	private static int getRowIndex(Sheet sheet, String filterCondition, boolean strictCompare) throws IOException {
		String[] conditions = filterCondition.split(ConditionSeparator);
		LinkedHashMap<String, String> fullConditions = new LinkedHashMap<String, String>();
		for (String condition : conditions) {
			fullConditions.put(condition.split(ConditionValueSeparator)[0],
					condition.split(ConditionValueSeparator)[1]);
		}
		int[] columnIndices = new int[fullConditions.size()];
		Set<String> columnNames = fullConditions.keySet();
		for (String columnName : columnNames) {
			columnIndices = ArrayUtils.add(columnIndices, getColumnIndex(sheet, columnName));
			columnIndices = ArrayUtils.remove(columnIndices, 0);
		}

		for (Row row : sheet) {
			LinkedHashMap<String, String> newHashMap = new LinkedHashMap<>();
			for (int index : columnIndices) {
				newHashMap.put(String.valueOf(getCellValue(sheet.getRow(0).getCell(index))),
						String.valueOf(getCellValue(row.getCell(index))));
			}
			if (strictCompare) {
				if (newHashMap.equals(fullConditions))
					return row.getRowNum();
			} else {
				if (compareHashMapLoosely(newHashMap, fullConditions))
					return row.getRowNum();
			}
		}

		return -1;
	}

	/**
	 * 
	 * @param excelPath
	 * @param sheetName
	 * @param columnName
	 * @param filterCondition
	 * @return
	 * @throws IOException
	 */
	public static Object getCellValue(String excelPath, String sheetName, String columnName, String filterCondition,
			boolean... strictCompareFlag) throws IOException {
		boolean strictCompare = (strictCompareFlag != null && strictCompareFlag.length >= 1) ? strictCompareFlag[0]
				: false;

		Object value = null;
		Workbook workbook = getExcelWorkbook(excelPath);
		Sheet sheet = workbook.getSheet(sheetName);
		Cell cell = null;

		try {
			cell = sheet.getRow(getRowIndex(sheet, filterCondition, strictCompare))
					.getCell(getColumnIndex(sheet, columnName));
		} catch (NullPointerException e) {
			workbook.close();
			return null;
		}

		value = getCellValue(cell);
		workbook.close();
		return value;
	}

	private static boolean compareHashMapLoosely(LinkedHashMap<String, String> one, LinkedHashMap<String, String> two) {
		Set<String> keySetOne = one.keySet();
		Set<String> keySetTwo = two.keySet();

		if (!CollectionUtils.isEqualCollection(keySetOne, keySetTwo))
			return false;
		for (String col1 : keySetOne) {
			for (String col2 : keySetTwo) {
				if (col1.equalsIgnoreCase(col2)) {
					// Numeric Logic
					if (NumberUtils.isNumber(one.get(col1)) && NumberUtils.isNumber(two.get(col2))) {
						if (Double.valueOf(one.get(col1)).doubleValue() != Double.valueOf(two.get(col2)).doubleValue())
							return false;
					}
					// Ignore case and trim
					else if (StringUtils.isAlphanumeric(one.get(col1)) && StringUtils.isAlphanumeric(two.get(col2))) {
						if (!one.get(col1).trim().equalsIgnoreCase(two.get(col2).trim())) {
							return false;
						}
					}

					// TODO - Logic to be added for dateformat comparisons
				}
			}
		}

		return true;
	}

	public static ArrayList<Object> getEntireColumnData(String excelPath, String sheetname, int columnIndex)
			throws IOException {
		ArrayList<Object> data = new ArrayList<>();
		Sheet sheet = getSheetOfWorkbook(excelPath, sheetname);
		for (Row row : sheet) {
			data.add(getCellValue(row.getCell(columnIndex)));
		}
		return data;
	}

	public static ArrayList<Object> getEntireColumnData(String excelPath, String sheetname, String columnName)
			throws IOException {
		ArrayList<Object> data = new ArrayList<>();
		data.addAll(getEntireColumnData(excelPath, sheetname,
				getColumnIndex(getSheetOfWorkbook(excelPath, sheetname), columnName)));
		data.remove(0);
		return data;
	}

	private static Workbook getExcelWorkbook(String excelPath) throws IOException {
		Workbook workbook = null;
		FileInputStream fis = null;
		File excel = new File(excelPath);

		if (!excel.exists())
			throw new FileNotFoundException("No such file exists at - " + excel.getCanonicalPath());
		fis = new FileInputStream(excel);
		if (excel.getName().contains(".xlsx"))
			workbook = new XSSFWorkbook(fis);
		else
			workbook = new HSSFWorkbook(fis);

		fis.close();
		return workbook;
	}

	private static Sheet getSheetOfWorkbook(String excelPath, String sheetName) throws IOException {
		return getExcelWorkbook(excelPath).getSheet(sheetName);
	}

	public static HashMap<Integer, Object> getExcelSheetData(String excelPath, String sheetname,
			boolean... columnHeaderPresent) throws IOException {
		HashMap<Integer, Object> data = new HashMap<>();
		boolean headersPresent = (columnHeaderPresent != null && columnHeaderPresent.length >= 1)
				? (columnHeaderPresent[0]) : false;
		Workbook workbook = getExcelWorkbook(excelPath);
		Sheet sheet = workbook.getSheet(sheetname);
		ArrayList<String> columnNames = new ArrayList<>();
		int cnt = 0;

		for (Row row : sheet) {
			String values = "";
			for (Cell cell : row) {
				if ((row.getRowNum() == 0) && (!headersPresent)) {
					values += String.valueOf(getCellValue(cell)) + ColumnSeparator;
				} else if ((row.getRowNum() == 0) && headersPresent) {
					columnNames.add(String.valueOf(getCellValue(cell)));
					values += String.valueOf(getCellValue(cell)) + ColumnSeparator;
				} else if ((row.getRowNum() != 0) && (!headersPresent)) {
					values += String.valueOf(getCellValue(cell)) + ColumnSeparator;
				} else if ((row.getRowNum() != 0) && (headersPresent)) {
					values += columnNames.get(cell.getColumnIndex()) + ColumnValueSeparator
							+ String.valueOf(getCellValue(cell)) + ColumnSeparator;
				}
			}
			values = values.substring(0, (values.length() - 1));
			data.put(++cnt, values);
		}
		data.remove(0);
		workbook.close();
		return data;
	}
	
	public static List<HashMap<Integer, Object>> getExcelData(String excelPath) throws IOException {
		List<HashMap<Integer, Object>> data = new ArrayList<HashMap<Integer, Object>>();
		Workbook workbook = getExcelWorkbook(excelPath);
		
		for (Sheet sheet : workbook) {
			data.add(getExcelSheetData(excelPath, sheet.getSheetName()));
		}
		
		workbook.close();
		return data;
	}
}