package test.automation.framework.library.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;

import org.apache.commons.lang3.StringUtils;

/**
 * DBUtils.java - Singleton pattern
 * 
 * @category Utilities for Database execution
 * @authors VINOD JOSHI, PRATEEK AGARWAL AND SHANKAR GANESH
 * @version 1.0
 */

public class DBUtils {
	/* Variables */
	public static final String rowColumnSeparator = "@@@@";
	public static final String columnValueSeparator = "::::";
	public static final String nullValueRepresent = "++NULL++";

	/* Functions */
	/**
	 * establishConnection
	 * 
	 * used locally in Query and Update Functions
	 * 
	 * @param connString
	 * @param connDriver
	 * @return Connection
	 * @throws SQLException
	 * @throws ClassNotFoundException
	 */
	private static Connection establishConnection(String connString, String connDriver)
			throws ClassNotFoundException, SQLException {
		Class.forName(connDriver);
		return DriverManager.getConnection(connString);
	}

	/**
	 * 
	 * <b>executeQuery</b><br>
	 * <br>
	 * public HashMap<Integer, String> executeQuery(String connString, String
	 * connDriver, String sqlQuery, Object... params) <br>
	 * <br>
	 * This function executes the sql query for a provided connection jdbc
	 * string <br>
	 * and provided driver (eg - com.microsoft.sqlserver.jdbc.SQLServerDriver)
	 * <br>
	 * and returns the results in the form of HashMap<Integer, String> <br>
	 * where Integer identifies the row number and String contains the
	 * concatenated <br>
	 * row (ColumnName1=ColumnValue1|ColumnName2=ColumnValue2). <br>
	 * Row number starts from 1. <br>
	 * 
	 * @param connString
	 *            - jdbc connection string
	 * @param connDriver
	 *            - Driver for the connection string (eg- SQLServer driver)
	 * @param sqlQuery
	 *            - String
	 * @param params
	 *            optional prepared statement parameters
	 * @return HashMap<Integer, String>
	 * @throws Exception
	 */
	public static HashMap<Integer, String> executeQuery(String connString, String connDriver, String sqlQuery,
			Object... params) throws Exception {
		HashMap<Integer, String> results = new HashMap<>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ResultSetMetaData resultSetMetaData = null;
		int totalColumns = 0;

		Connection connection = establishConnection(connString, connDriver);

		try {
			statement = connection.prepareStatement(sqlQuery);
			if (params != null && params.length > 0) {
				int cnt = 1;
				for (Object object : params) {
					if (object != null) {
						statement.setObject(cnt++, object);
					} else {
						statement.setNull(cnt++, Types.NULL);
					}
				}
			}
			resultSet = statement.executeQuery();
			resultSetMetaData = resultSet.getMetaData();
			totalColumns = resultSetMetaData.getColumnCount();

			while (resultSet.next()) {
				String row = "";
				for (int i = 1; i <= totalColumns; i++) {
					if (resultSet.getObject(i) == null)
						row += resultSetMetaData.getColumnName(i) + columnValueSeparator + nullValueRepresent
								+ rowColumnSeparator;
					else
						row += resultSetMetaData.getColumnName(i) + columnValueSeparator + resultSet.getString(i)
								+ rowColumnSeparator;
				}
				row = row.substring(0, (row.length() - 1));
				results.put(resultSet.getRow(), row);
			}

		} catch (SQLException e) {

		} finally {
			resultSet.close();
			resultSet = null;
			statement.close();
			statement = null;
			connection.close();
			connection = null;
		}

		return results;
	}

	/**
	 * 
	 * <b>executeQueryValues</b><br>
	 * <br>
	 * public HashMap<Integer, String> executeQueryValues(String connString,
	 * String connDriver, String sqlQuery, Object...params) <br>
	 * <br>
	 * This function executes the sql query for a provided connection jdbc
	 * string <br>
	 * and provided driver (eg - com.microsoft.sqlserver.jdbc.SQLServerDriver)
	 * <br>
	 * and returns the results in the form of HashMap<Integer, String> <br>
	 * where Integer identifies the row number and String contains the
	 * concatenated <br>
	 * row (ColumnValue1|ColumnValue2). <br>
	 * Row number starts from 1. This function does not return the ColumnNames
	 * <br>
	 * <br>
	 * 
	 * @param connString
	 *            - jdbc connection string
	 * @param connDriver
	 *            - Driver for the connection string (eg- SQLServer driver)
	 * @param sqlQuery
	 *            - String
	 * @param params
	 *            optional prepared statement parameters
	 * @return HashMap<Integer, String>
	 * @throws Exception
	 */
	public static HashMap<Integer, String> executeQueryValues(String connString, String connDriver, String sqlQuery,
			Object... params) throws Exception {
		HashMap<Integer, String> results = new HashMap<>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ResultSetMetaData resultSetMetaData = null;
		int totalColumns = 0;

		Connection connection = establishConnection(connString, connDriver);

		try {
			statement = connection.prepareStatement(sqlQuery);
			if (params != null && params.length > 0) {
				int cnt = 1;
				for (Object object : params) {
					if (object != null) {
						statement.setObject(cnt++, object);
					} else {
						statement.setNull(cnt++, Types.NULL);
					}
				}
			}
			resultSet = statement.executeQuery(sqlQuery);
			resultSetMetaData = resultSet.getMetaData();
			totalColumns = resultSetMetaData.getColumnCount();

			while (resultSet.next()) {
				String row = "";
				for (int i = 1; i <= totalColumns; i++) {
					if (resultSet.getObject(i) == null)
						row += nullValueRepresent + rowColumnSeparator;
					else
						row += resultSet.getString(i) + rowColumnSeparator;
				}
				row = row.substring(0, (row.length() - 1));
				results.put(resultSet.getRow(), row);
			}

		} catch (SQLException e) {

		} finally {
			resultSet.close();
			resultSet = null;
			statement.close();
			statement = null;
			connection.close();
			connection = null;
		}

		return results;
	}

	/**
	 * 
	 * @param connString
	 *            - jdbc connection string
	 * @param connDriver
	 *            - eg :- com.microsoft.sqlserver.jdbc.SQLServerDriver
	 * @param sqlQuery
	 *            - Insert/Update or any DDL command
	 * @param params
	 *            - Optional
	 * @return Number of Rows Updated
	 * @throws Exception
	 */
	public static int executeUpdateInsert(String connString, String connDriver, String sqlQuery, Object... params)
			throws Exception {
		int recordsUpdated = 0;
		Connection connection = establishConnection(connString, connDriver);
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement(sqlQuery);
			if (params != null && params.length > 0) {
				int cnt = 1;
				for (Object object : params) {
					if (object != null) {
						statement.setObject(cnt++, object);
					} else {
						statement.setNull(cnt++, Types.NULL);
					}
				}
			}
			recordsUpdated = statement.executeUpdate();
		} catch (SQLException e) {

		}
		return recordsUpdated;
	}

	/**
	 * 
	 * @param resultSet
	 *            as HashMap Integer, String
	 * @return List of HashMap String, String
	 */
	public static List<HashMap<String, String>> getResultSetAsLHM(HashMap<Integer, String> resultSet) {
		List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		for (int i = 1; i <= resultSet.size(); i++) {
			HashMap<String, String> oneRow = new HashMap<>();
			String[] Column_ColumnValues = resultSet.get(i).split(rowColumnSeparator);
			for (String string : Column_ColumnValues) {
				oneRow.put(string.split(columnValueSeparator)[0], string.split(columnValueSeparator)[1]);
			}
		}
		return results;
	}

	/**
	 * 
	 * @param resultSet
	 *            as List of HashMap
	 * @return HashMap
	 */
	public static HashMap<Integer, String> getResultSetAsHM(List<HashMap<String, String>> resultSet) {
		HashMap<Integer, String> results = new HashMap<Integer, String>();
		int cnt = 1;
		for (HashMap<String, String> iterable_element : resultSet) {
			String rowValue = "";
			SortedSet<String> keys = (SortedSet<String>) iterable_element.keySet();
			for (String key : keys) {
				rowValue += ((key + columnValueSeparator + iterable_element.get(key)) + rowColumnSeparator);
			}
			rowValue = rowValue.substring(0, (rowValue.length() - 1));
			results.put(cnt++, rowValue);
		}
		return results;
	}

	/**
	 * 
	 * @param results
	 *            - Takes input as a HashMap Integer,String
	 * @param conditions
	 *            - conditions as ColumnName=ColumnValue (The = represents the
	 *            split in DBUtils Column_Name to Column Value)
	 * @return HashMap Integer, String filtered by the conditions
	 */
	public static HashMap<Integer, String> filterResultsCondition(HashMap<Integer, String> results, String... conditions) {
		if (conditions.length > 0 && conditions != null) {
			HashMap<Integer, String> newResults = new HashMap<>();
			int recordCntr = 1;
			for (int i = 1; i <= results.size(); i++) {
				boolean flag = true;
				for (String condition : conditions) {
					if (!results.get(i).contains(condition)) {
						flag = false;
						break;
					}
				}
				if (flag)
					newResults.put(recordCntr++, results.get(i));
			}
			return newResults;
		} else {
			return results;
		}
	}

	/**
	 * 
	 * @param aut
	 *            result set 1 in form of HashMap Integer, String
	 * @param baseline
	 *            result set 2 in form of HashMap Integer, String
	 * @param ignoreColumns
	 *            List of Columns which need to be ignored for comparison
	 * @return HashMap String, Object with keys as match, reasons,
	 *         recordsMissingInAUT, recordsMissingInBaseline
	 */
	public static HashMap<String, Object> compareQueryResults(HashMap<Integer, String> aut, HashMap<Integer, String> baseline,
			String... ignoreColumns) {
		HashMap<String, Object> compareResults = new HashMap<>();
		boolean match = true;
		List<String> reasons = new ArrayList<>();
		List<String> recordsMissingInAUT = new ArrayList<>();
		List<String> recordsMissingInBaseline = new ArrayList<>();

		compareResults.put("match", null);
		compareResults.put("reasons", null);
		compareResults.put("recordsMissingInAUT", null);
		compareResults.put("recordsMissingInBaseline", null);

		if (aut == null && baseline == null) {
			match = new Boolean(true);
			reasons.add("Both aut and baseline are null.");
			compareResults.put("match", match);
			compareResults.put("reasons", reasons);
			return compareResults;
		} else if (aut == null && baseline != null) {
			match = new Boolean(false);
			reasons.add("Aut is null and baseline is not null.");
			compareResults.put("match", match);
			compareResults.put("reasons", reasons);
			return compareResults;
		} else if (aut != null && baseline == null) {
			match = new Boolean(false);
			reasons.add("Aut is not null and baseline is null.");
			compareResults.put("match", match);
			compareResults.put("reasons", reasons);
			return compareResults;
		}

		if (ignoreColumns.length >= 1 && ignoreColumns != null) {
			HashMap<Integer, String> autNew = new HashMap<>();
			HashMap<Integer, String> baselineNew = new HashMap<>();

			for (String ignoreColumn : ignoreColumns) {
				for (int i = 1; i <= aut.size(); i++) {
					String[] columns = aut.get(i).split(rowColumnSeparator);
					for (int j = 0; j < columns.length; j++) {
						String Column_Name = columns[j].split(columnValueSeparator)[0];
						if (Column_Name.equals(ignoreColumn)) {
							columns[j] = Column_Name + columnValueSeparator + "ignored";
						}
					}
					autNew.put(i, StringUtils.join(columns, rowColumnSeparator));
				}
				for (int i = 1; i <= baseline.size(); i++) {
					String[] columns = baseline.get(i).split(rowColumnSeparator);
					for (int j = 0; j < columns.length; j++) {
						String Column_Name = columns[j].split(columnValueSeparator)[0];
						if (Column_Name.equals(ignoreColumn)) {
							columns[j] = Column_Name + columnValueSeparator + "ignored";
						}
					}
					baselineNew.put(i, StringUtils.join(columns, rowColumnSeparator));
				}
			}

			aut = autNew;
			baseline = baselineNew;
		}

		// Check Size first
		if (aut.size() != baseline.size()) {
			match = match && false;
			reasons.add("Resultset size does not match for aut and baseline. [Aut-" + aut.size() + ", Baseline-"
					+ baseline.size() + ".]");
		}

		// Check for rows missing in baseline
		for (int i = 1; i <= aut.size(); i++) {
			boolean flag = false;
			String[] oneRowAUT = aut.get(i).split(rowColumnSeparator);
			for (int j = 1; j <= baseline.size(); j++) {
				String[] oneRowBaseline = baseline.get(j).split(rowColumnSeparator);
				if (Arrays.asList(oneRowAUT).containsAll(Arrays.asList(oneRowBaseline))
						&& Arrays.asList(oneRowBaseline).containsAll(Arrays.asList(oneRowAUT))) {
					flag = true;
					break;
				}
			}
			if (!flag)
				recordsMissingInBaseline.add(aut.get(i));
		}

		// Check for rows missing in aut
		for (int i = 1; i <= baseline.size(); i++) {
			boolean flag = false;
			String[] oneRowBaseline = baseline.get(i).split(rowColumnSeparator);
			for (int j = 1; j <= aut.size(); j++) {
				String[] oneRowAUT = aut.get(j).split(rowColumnSeparator);
				if (Arrays.asList(oneRowAUT).containsAll(Arrays.asList(oneRowBaseline))
						&& Arrays.asList(oneRowBaseline).containsAll(Arrays.asList(oneRowAUT))) {
					flag = true;
					break;
				}
			}
			if (!flag)
				recordsMissingInAUT.add(baseline.get(i));
		}

		if (recordsMissingInAUT.size() > 0) {
			match = match && false;
			reasons.add("There are " + recordsMissingInAUT.size() + " records which are missing in aut result set.");
		}

		if (recordsMissingInBaseline.size() > 0) {
			match = match && false;
			reasons.add("There are " + recordsMissingInBaseline.size()
					+ " records which are missing in baseline result set.");
		}

		if (match)
			reasons.add("Both aut and baseline result set are perfect match. [Aut-" + aut.size() + ", Baseline-"
					+ baseline.size() + "]");

		compareResults.put("match", match);
		compareResults.put("reasons", reasons);
		compareResults.put("recordsMissingInAUT", recordsMissingInAUT);
		compareResults.put("recordsMissingInBaseline", recordsMissingInBaseline);

		return compareResults;
	}

	/**
	 * executeProcOrFunc
	 * 
	 * @param connString
	 * @param connDriver
	 * @param sqlQuery
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public static HashMap<Integer, String> executeProcOrFunc(String connString, String connDriver, String sqlQuery,
			Object... params) throws Exception {
		HashMap<Integer, String> results = new HashMap<>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ResultSetMetaData resultSetMetaData = null;
		int totalColumns = 0;

		Connection connection = establishConnection(connString, connDriver);

		try {
			statement = connection.prepareCall(sqlQuery);
			if (params != null && params.length > 0) {
				int cnt = 1;
				for (Object object : params) {
					if (object != null) {
						statement.setObject(cnt++, object);
					} else {
						statement.setNull(cnt++, Types.NULL);
					}
				}
			}
			resultSet = statement.executeQuery();
			resultSetMetaData = resultSet.getMetaData();
			totalColumns = resultSetMetaData.getColumnCount();

			while (resultSet.next()) {
				String row = "";
				for (int i = 1; i <= totalColumns; i++) {
					if (resultSet.getObject(i) == null)
						row += resultSetMetaData.getColumnName(i) + columnValueSeparator + nullValueRepresent
								+ rowColumnSeparator;
					else
						row += resultSetMetaData.getColumnName(i) + columnValueSeparator + resultSet.getString(i)
								+ rowColumnSeparator;
				}
				row = row.substring(0, (row.length() - 1));
				results.put(resultSet.getRow(), row);
			}

		} catch (SQLException e) {

		} finally {
			resultSet.close();
			resultSet = null;
			statement.close();
			statement = null;
			connection.close();
			connection = null;
		}

		return results;
	}

	/**
	 * 
	 * <b>executeProcOrFuncValues</b><br>
	 * <br>
	 * public HashMap<Integer, String> executeQueryValues(String connString,
	 * String connDriver, String sqlQuery, Object...params) <br>
	 * <br>
	 * This function executes the sql query for a provided connection jdbc
	 * string <br>
	 * and provided driver (eg - com.microsoft.sqlserver.jdbc.SQLServerDriver)
	 * <br>
	 * and returns the results in the form of HashMap<Integer, String> <br>
	 * where Integer identifies the row number and String contains the
	 * concatenated <br>
	 * row (ColumnValue1|ColumnValue2). <br>
	 * Row number starts from 1. This function does not return the ColumnNames
	 * <br>
	 * <br>
	 * 
	 * @param connString
	 *            - jdbc connection string
	 * @param connDriver
	 *            - Driver for the connection string (eg- SQLServer driver)
	 * @param sqlQuery
	 *            - String
	 * @param params
	 *            optional prepared statement parameters
	 * @return HashMap<Integer, String>
	 * @throws Exception
	 */
	public static HashMap<Integer, String> executeProcOrFuncValues(String connString, String connDriver, String sqlQuery,
			Object... params) throws Exception {
		HashMap<Integer, String> results = new HashMap<>();
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		ResultSetMetaData resultSetMetaData = null;
		int totalColumns = 0;

		Connection connection = establishConnection(connString, connDriver);

		try {
			statement = connection.prepareCall(sqlQuery);
			if (params != null && params.length > 0) {
				int cnt = 1;
				for (Object object : params) {
					if (object != null) {
						statement.setObject(cnt++, object);
					} else {
						statement.setNull(cnt++, Types.NULL);
					}
				}
			}
			resultSet = statement.executeQuery(sqlQuery);
			resultSetMetaData = resultSet.getMetaData();
			totalColumns = resultSetMetaData.getColumnCount();

			while (resultSet.next()) {
				String row = "";
				for (int i = 1; i <= totalColumns; i++) {
					if (resultSet.getObject(i) == null)
						row += nullValueRepresent + rowColumnSeparator;
					else
						row += resultSet.getString(i) + rowColumnSeparator;
				}
				row = row.substring(0, (row.length() - 1));
				results.put(resultSet.getRow(), row);
			}

		} catch (SQLException e) {

		} finally {
			resultSet.close();
			resultSet = null;
			statement.close();
			statement = null;
			connection.close();
			connection = null;
		}

		return results;
	}
}