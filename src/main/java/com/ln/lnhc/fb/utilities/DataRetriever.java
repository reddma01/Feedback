package com.ln.lnhc.fb.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.codoid.products.exception.FilloException;
import com.codoid.products.fillo.Connection;
import com.codoid.products.fillo.Fillo;
import com.codoid.products.fillo.Recordset;

public class DataRetriever {

	public static String queryValue;
	public static Connection connection;
	public static Fillo fillo;
	public static String path = System.getProperty("user.dir") + System.getProperty("file.separator")
			+ "src/test/resources/data/FBCQTestData.xlsx";

	public static String errorDesc = "";
	private static Map<String, String> testFilter;
	private static String queryTable;
	private static Map<String, String> data;

	/**
	 * Description: Displays the data from the spreadsheet depending on the
	 * Table(Worksheet) used in the query. It calls the getSheetData method which
	 * returns the recordset that contains the data for that worksheet and uses the
	 * columns as fields Purpose: To retrieve data from a spreadsheet using SQL
	 * Statements and giving the Table name and Where Clause (optional).
	 * 
	 * @param sqlTable    - Name of the Table or the Worksheet being fetched
	 * @param whereClause - Where statement to filter the records/data
	 * @return - true if successful display; false if data was not displayed
	 */
	public static boolean displayData(String sqlTable, String whereClause, String query) {
		boolean passed = true;
		Recordset rs = null;
		ArrayList<String> fields = null;

		if (query.length() > 0) {
			queryValue = query;
		} else {
			if (sqlTable.length() == 0) {
				errorDesc = "Table or Worksheet name not defined.\n";
				passed = false;
			} else {
				queryValue = "SELECT * FROM  \"" + sqlTable + "\"" + " " + whereClause;
			}
		}

		if (passed) {
			if (sqlValid(queryValue)) {
				print("======================== START DATA DISPLAY ======================================");
				rs = getSheetData(sqlTable, whereClause, query);

				if (rs != null) {
					try {
						fields = rs.getFieldNames();
					} catch (FilloException e) {
						errorDesc = "Error in retrieving the field names.\n";
						passed = false;
					}

					int ctr = 0;
					try {
						while (rs.next()) {
							for (int i = 0; i < fields.size(); i++) { // Loop through the data fields/columns
								print(fields.get(i) + " = " + rs.getField("" + fields.get(i) + ""));
							}
							print("");
							ctr++;
							if (ctr == rs.getCount())
								break;
						}
					} catch (FilloException e) {
						errorDesc += "Error in retrieving the data.\n";
						passed = false;
					}
					rs.close();
				} else {
					passed = false;
				}
				print("======================== END DATA DISPLAY ======================================");
			} else {
				passed = false;
			}
		}
		return passed;
	}

	public static Map<String, String> getData() {
		return data;
	}

	public static String getErrorDescription() {
		return errorDesc;
	}

	public static String getQuery(String tableName, Map<String, String> testFilter) {
		StringBuffer query = new StringBuffer();
		query.append("Select * from ").append("\"" + tableName + "\"").append(getWhereClause(testFilter));
		return query.toString();
	}

	public static Map<String, String> getSheetData(String query) {
		System.out.println(query);
		Map<String, String> data = new HashMap<>();
		try {
			Recordset rs = connection.executeQuery(query);
			if (rs != null && rs.next()) {
				ArrayList<String> colNames = rs.getFieldNames();
				for (String colName : colNames) {
					data.put(colName, rs.getField(colName));
				}
			}
		} catch (FilloException e) {
			errorDesc += "No records found for query " + query + ".\n";
		}
		return data;
	}

	public static Recordset getAllData(String query) {

		try {
			Fillo fillo = new Fillo();
			connection = fillo.getConnection(path);
			Recordset rset = connection.executeQuery(query);
			return rset;
		} catch (FilloException e) {

			e.printStackTrace();
			return null;
		}

	}

	/**
	 * Description: Fetches data from a spreadsheet using a combination of Table
	 * name and Where Clause where Table name is the worksheet name and Where clause
	 * is to filter the data. If Table is not defined then the query parameter
	 * should not be empty, this means that a Full SQL Statement is passed Purpose:
	 * To fetch data from a spreadsheet using SQL statements in 2 ways, first is the
	 * combination of table and where clause, second is passing the entire SQL
	 * statement.
	 * 
	 * @param sqlTable    - Table name or Worksheet name to query
	 * @param whereClause - Where statement to filter records
	 * @param query       - Full SQL Statement
	 * @return recordset of fetched rows
	 */
	public static Recordset getSheetData(String sqlTable, String whereClause, String query) {
		Recordset rs = null;
		String queryValue = "";

		if (query.length() > 0) {
			queryValue = query;
		} else {
			if (sqlTable.length() == 0) {
				errorDesc = "Table or Worksheet name not defined.\n";
			} else {
				queryValue = "SELECT * FROM  \"" + sqlTable + "\"" + " " + whereClause;
			}
		}

		try {
			rs = connection.executeQuery(queryValue);
		} catch (FilloException e) {
			if (query.length() > 0) {
				errorDesc += "No records found for query " + query + ".\n";
			} else {
				errorDesc += "No records found for table/worksheet " + sqlTable + ".\n";
			}
			rs = null;
		}
		return rs;
	}

	public static List<Map<String, String>> getSheetDataAll(String query) {
		List<Map<String, String>> dataList = new ArrayList<>();
		try {
			Recordset rs = connection.executeQuery(query);
			if (rs != null) {
				while (rs.next()) {
					Map<String, String> data = new HashMap<>();
					ArrayList<String> colNames = rs.getFieldNames();
					for (String colName : colNames) {
						data.put(colName, rs.getField(colName));
					}
					dataList.add(data);
				}
			}
		} catch (FilloException e) {
			errorDesc += "No records found for query " + query + ".\n";
		}
		return dataList;
	}

	public static boolean getTestDetails() {
		boolean passed = true;
		String query = "";

		if (connection == null)
			passed = readExcel();

		if (passed) {
			query = getQuery(queryTable, testFilter);
			data = getSheetData(query);
		} else {
			passed = false;
		}

		return passed;
	}

	public static String getWhereClause(Map<String, String> filters) {
		StringBuffer whereClause = new StringBuffer();
		whereClause.append(" Where ");

		Iterator<String> it = filters.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			whereClause.append(" \"").append(key).append("\" = '").append(filters.get(key)).append("' ");
			if (it.hasNext()) {
				whereClause.append(" AND ");
			}
		}
		whereClause.append("AND \"ExecuteTestCase\"= \'Y\'");
		return whereClause.toString();
	}

	private static void print(String msg) {
		System.out.println(msg);
	}

	/**
	 * Description: Reads the Excel file to create the fillo connection. No need to
	 * close the connection, this will make the spreadsheet available in the
	 * project. Purpose: To establish connection to the test data spreadsheet
	 */
	public static boolean readExcel() {
		boolean passed = true;
		fillo = new Fillo();
		connection = null;

		// print("======================== START READ EXCEL
		// ======================================");
		try {
			connection = fillo.getConnection(path);
		} catch (FilloException e) {
			errorDesc = e.getMessage() + "\n";
			passed = false;
		}
		// print("======================== END READ EXCEL
		// ======================================");
		return passed;
	}

	public static void setQueryTable(String pQueryTable) {
		queryTable = pQueryTable;
	}

	public static void setTestFilter(Map<String, String> pTestFilter) {
		testFilter = pTestFilter;
	}

	/**
	 * Description: Checks if the statement contains the expected keywords from an
	 * SQL statement Purpose: To filter the query statement by checking if keywords
	 * are present.
	 * 
	 * @param query - Entire query statement
	 * @return - True - valid, False - invalid
	 */
	private static boolean sqlValid(String query) {
		boolean passed = true;

		if (!query.toLowerCase().contains("select"))
			passed = false;
		if (!query.toLowerCase().contains("from"))
			passed = false;

		if (!passed)
			errorDesc = "Invalid SQL Statement. " + query + ". Please try again.\n";
		return passed;
	}

}
