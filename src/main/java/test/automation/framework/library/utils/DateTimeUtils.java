package test.automation.framework.library.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtils {
	public static Date convertStringtoDate(String date, String sourceDateFormat) throws ParseException {
		return new SimpleDateFormat().parse(date);
	}
	
	public static String convertDatetoString(Date date) {
		return date.toString();
	}
	
	public static String returnDateInRequestedFormat(Date date, String format) {
		return new SimpleDateFormat(format).format(date);
	}
	
	public static String returnDateInRequestedFormat(String date, String sourceFormat, String destFormat) throws ParseException {
		return new SimpleDateFormat(destFormat).format(convertStringtoDate(date,sourceFormat));
	}
}