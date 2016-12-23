package testing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.LinkedList;

import org.junit.BeforeClass;
import org.junit.Test;

import dBManagement.DBManager;
import sqlParsers.SQLParser;

public class Selection {

	private static SQLParser instance;
	private static PathsChecker pathChecker;
	private static DBManager dbManger;
	private static final int EMPTY = 0;
	private static final String DBNAME = "CLASS19", TABLENAME = "students";
	private static final String[] COLNAME = { "ID", "NAME", "YearWork", "Final" };
	private static final String[] COLTYPE = { "int", "varchar", "int", "int" };
	private static final String[][] ENTRIES = { { "ID", "NAME", "YearWork", "Final" }, { "1", "\'Essam\'", "30", "50" },
			{ "2", "\'Magdy\'", "20", "50" }, { "3", "\'AbdElmegeed\'", "80", "150" },
			{ "4", "\'GotMad\'", "3", "0" } };

	@BeforeClass
	public static void setUp() throws Exception {
		dbManger = DBManager.getInstance();
		instance = SQLParser.getInstance();
		pathChecker = PathsChecker.getInstance();
		pathChecker.creatDirectory();
		initialize();
	}

	private static void initialize() {
		if (instance.parseSQL(DoCommand.makeSelectCommand(DBNAME))) {
			instance.parseSQL(DoCommand.makeDropCommand(DBNAME));
		}
		instance.parseSQL(DoCommand.makeCreationCommand(DBNAME));
		instance.parseSQL(DoCommand.makeAddTableCommad(TABLENAME, COLNAME, COLTYPE));
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
			instance.parseSQL(DoCommand.makeAddQueryCommand(TABLENAME, COLNAME, ENTRIES[i]));
		}
	}

	private void testSelectAllFromTable() {
		LinkedList<LinkedList<String>> selectedQuery = dbManger.selectQuery(TABLENAME, COLNAME, null, false);
		doTest(selectedQuery, ENTRIES);
	}

	private void testSelectWithCondition() {
		String[] colNames = {"ID", "NAME", "YearWork"};
		LinkedList<LinkedList<String>> selectedQuery = dbManger.selectQuery(TABLENAME, colNames,
				"ID > 1 AND YearWork > 3", false);
		String[][] expectedResult = new String[3][3];
		for(int i = 0 ; i < 3 ; i++) {
			expectedResult[0][i] = colNames[i];
		}
		for(int i = 2 ; i <= 3 ; i++) {
			for(int j = 0 ; j < 3 ; j++) {
				expectedResult[i - 1][j] = ENTRIES[i][j];
			}
		}
		doTest(selectedQuery, expectedResult);
	}

	private void testHardCondition() {
		String condition = "ID > 1 AND YearWork < 10 OR NOT Final = 150";
		LinkedList<LinkedList<String>> selectedQuery = dbManger.selectQuery(TABLENAME, COLNAME,
				condition, false);
		String[][] expectedResult = new String[4][4];
		for(int i = 0 ; i <= 2 ; i++) {
			for(int j = 0 ; j < 4 ; j++) {
				expectedResult[i][j] = ENTRIES[i][j];
			}
		}
		for(int i = 0 ; i < 4 ; i++) {
			expectedResult[3][i] = ENTRIES[4][i];
		}
		doTest(selectedQuery, expectedResult);
	}

	private void testMostprcedenceDependencey() {
		String[] colNames = {"ID", "NAME", "YearWork"};
		String condition = "YearWork < 10 OR YearWork > 20 AND NOT Final > 100";
		LinkedList<LinkedList<String>> selectedQuery = dbManger.selectQuery(TABLENAME, colNames,
				condition, false);
		String[][] expectedResult = new String[3][3];
		for(int i = 0 ; i < 3 ; i++) {
			expectedResult[0][i] = ENTRIES[0][i];
			expectedResult[1][i] = ENTRIES[1][i];
			expectedResult[2][i] = ENTRIES[4][i];
		}
		doTest(selectedQuery, expectedResult);
	}

	private void testEmpty() {
		String[] colNames = {"ID", "NAME", "YearWork"};
		String condition = "YearWork < 0";
		LinkedList<LinkedList<String>> selectedQuery = dbManger.selectQuery(TABLENAME, colNames,
				condition, false);
		String[][] expectedResult = new String[1][3];
		for(int i = 0 ; i < 3 ; i++) {
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
