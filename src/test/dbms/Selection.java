package test.dbms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;

import org.junit.BeforeClass;
import org.junit.Test;

import dBManagement.DBManager;
import dBManagement.QueryData;
import query.SelectQuery;
import sqlParsers.SQLParser;

public class Selection {

	private static SQLParser sqlParser;
	private static DBManager dbManager;
	private static String dirPath;
	private static final int EMPTY = 0;
	private static final String DBNAME = "CLASS19", TABLENAME = "students";
	private static final String[] COLNAME = { "id", "name", "yearwork", "final" };
	private static final String[] COLTYPE = { "int", "varchar", "int", "int" };
	private static final String[][] ENTRIES = { { "id", "name", "yearwork", "final" }, { "1", "\'essam\'", "30", "50" },
			{ "2", "\'magdy\'", "20", "50" }, { "3", "\'abdelmegeed\'", "80", "150" },
			{ "4", "\'gotmad\'", "3", "0" } };

	@BeforeClass
	public static void setUp() throws Exception {
		dirPath = System.getProperty("java.io.tmpdir");
		dbManager = new DBManager(dirPath, 1);
		sqlParser = new SQLParser(dbManager);
		initialize();
	}

	private static void initialize() {
		if (!sqlParser.parseSQL(DoCommand.makeSelectCommand(DBNAME)).checkOperationFail()) {
			sqlParser.parseSQL(DoCommand.makeDropCommand(DBNAME));
		}
		sqlParser.parseSQL(DoCommand.makeCreationCommand(DBNAME));
		sqlParser.parseSQL(DoCommand.makeAddTableCommad(TABLENAME, COLNAME, COLTYPE));
	}

	@Test
	public void testing() {
		fillTable();
		testSelectAllFromTable();
		testSelectWithCondition();
		testHardCondition();
		testMostprcedenceDependencey();
		testEmpty();
	}

	private void fillTable() {
		for (int i = 0; i < ENTRIES.length; i++) {
			sqlParser.parseSQL(DoCommand.makeAddQueryCommand(TABLENAME, COLNAME, ENTRIES[i]));
		}
	}

	private void testSelectAllFromTable() {
		String[] allCols = { "*" };
		SelectQuery resQuery = (SelectQuery) dbManager.selectQuery(TABLENAME, allCols, null, false);
		LinkedList<LinkedList<String>> selectedQuery = new LinkedList<LinkedList<String>>(
				getResult(resQuery.getQueryData()));
		doTest(selectedQuery, ENTRIES);
	}

	private LinkedList<LinkedList<String>> getResult(QueryData queryData) {
		LinkedList<LinkedList<String>> selectedQuery = new LinkedList<LinkedList<String>>();
		int rows = queryData.rowsCount();
		int cols = queryData.columnCount();
		selectedQuery.add(new LinkedList<String>());
		for (int i = 1; i <= cols; i++) {
			selectedQuery.getLast().add(queryData.getColumnName(i));
		}
		for (int i = 1; i <= rows; i++) {
			selectedQuery.add(new LinkedList<String>());
			for (int j = 1; j <= cols; j++) {
				selectedQuery.getLast().add(queryData.getCell(i, j));
			}
		}
		return selectedQuery;
	}

	private void testSelectWithCondition() {
		String[] colNames = { "id", "name", "yearwork" };
		SelectQuery resQuery = (SelectQuery) dbManager.selectQuery(TABLENAME, colNames, "id > 1 and yearwork > 3",
				false);
		LinkedList<LinkedList<String>> selectedQuery = new LinkedList<LinkedList<String>>(
				getResult(resQuery.getQueryData()));
		String[][] expectedResult = new String[3][3];
		for (int i = 0; i < 3; i++) {
			expectedResult[0][i] = colNames[i];
		}
		for (int i = 2; i <= 3; i++) {
			for (int j = 0; j < 3; j++) {
				expectedResult[i - 1][j] = ENTRIES[i][j];
			}
		}
		doTest(selectedQuery, expectedResult);
	}

	private void testHardCondition() {
		String condition = "id > 1 and yearwork < 10 or not final = 150";
		SelectQuery resQuery = (SelectQuery) dbManager.selectQuery(TABLENAME, COLNAME, condition, false);
		LinkedList<LinkedList<String>> selectedQuery = new LinkedList<LinkedList<String>>(
				getResult(resQuery.getQueryData()));
		String[][] expectedResult = new String[4][4];
		for (int i = 0; i <= 2; i++) {
			for (int j = 0; j < 4; j++) {
				expectedResult[i][j] = ENTRIES[i][j];
			}
		}
		for (int i = 0; i < 4; i++) {
			expectedResult[3][i] = ENTRIES[4][i];
		}
		doTest(selectedQuery, expectedResult);
	}

	private void testMostprcedenceDependencey() {
		String[] colNames = { "id", "name", "yearwork" };
		String condition = "yearwork < 10 or yearwork > 20 and not final > 100";
		SelectQuery resQuery = (SelectQuery) dbManager.selectQuery(TABLENAME, colNames, condition, false);
		LinkedList<LinkedList<String>> selectedQuery = new LinkedList<LinkedList<String>>(
				getResult(resQuery.getQueryData()));
		String[][] expectedResult = new String[3][3];
		for (int i = 0; i < 3; i++) {
			expectedResult[0][i] = ENTRIES[0][i];
			expectedResult[1][i] = ENTRIES[1][i];
			expectedResult[2][i] = ENTRIES[4][i];
		}
		doTest(selectedQuery, expectedResult);
	}

	private void testEmpty() {
		String[] colNames = { "id", "name", "yearwork" };
		String condition = "yearwork < 0";
		SelectQuery resQuery = (SelectQuery) dbManager.selectQuery(TABLENAME, colNames, condition, false);
		LinkedList<LinkedList<String>> selectedQuery = new LinkedList<LinkedList<String>>(
				getResult(resQuery.getQueryData()));
		String[][] expectedResult = new String[1][3];
		for (int i = 0; i < 3; i++) {
			expectedResult[0][i] = colNames[i];
		}
		doTest(selectedQuery, expectedResult);
	}

	private void doTest(LinkedList<LinkedList<String>> selectedQuery, String[][] expectedResult) {
		assertNotNull(selectedQuery);
		int numOfEntries = selectedQuery.size();
		assertEquals(expectedResult.length, numOfEntries);
		if (numOfEntries == EMPTY) {
			return;
		}
		for (int i = 0; i < numOfEntries; i++) {
			assertNotNull(selectedQuery.get(i));
			assertEquals(expectedResult[i].length, selectedQuery.get(i).size());
			for (int j = 0; j < expectedResult[i].length; j++) {
				assertNotNull(selectedQuery.get(i).get(j));
				assertEquals(expectedResult[i][j], selectedQuery.get(i).get(j));
			}
		}
	}

}
