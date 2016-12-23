package test.dbms;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import dBManagement.DBManager;
import sqlParsers.SQLParser;

public class TestTableFunctionallity {

	private static PathsChecker pathChecker;
	private static SQLParser sqlParser;
	private static DBManager dbManager;
	private static String dirPath;
	private static final int  CSED = 0 , CIVIL = 1 , MECHANICAL = 2 , CHEMICAL = 3;
	private static final String[] DBNAMES = {"csed" , "civil" , "mechanical", "chemical"};
	private static final String[] COLSNAME = {"midterm" , "final" , "comment"};
	private static final String[] COLSTYPE = {"int", "int", "varchar"};
	private static final String[] FORBIDDENNAME = { "create", "select", "insert", "delete", "update", "use", "from",
			"drop", "where", "and", "not", "or", "database", "table" };

	@Before
	public void setUp() throws Exception {
		dirPath = System.getProperty("java.io.tmpdir");
		dbManager = new DBManager(dirPath, 1);
		sqlParser = new SQLParser(dbManager);
		pathChecker = PathsChecker.getInstance();
	}

	@Test 
	public void addTableToAlreadyExistingDataBase() {
		createDataBase(CSED);
		String tables[] = {"subject1" , "subject2" , "subject3"};
		for(int i = 0; i < tables.length; i++){
			doTest(DoCommand.makeAddTableCommad(tables[i], COLSNAME, COLSTYPE), true);
			doPathTest(CSED, tables[i], true);
		}
	}
	
	@Test
	public void addTableWithInvalidName() {
		createDataBase(CIVIL);
		for(int i = 0 ; i < FORBIDDENNAME.length ; i++) {
			doTest(DoCommand.makeAddTableCommad(FORBIDDENNAME[i], COLSNAME, COLSTYPE), false);
			doPathTest(CIVIL, FORBIDDENNAME[i], false);
		}
	}
	
	@Test
	public void addAlreadyExistingTable() {
		createDataBase(CIVIL);
		String tables[] = {"firstyear" , "secondyear" , "thirdyear"};
		makeTables(tables);
		for(int i = 0 ; i < tables.length ; i++) {
			doPathTest(CIVIL, tables[i], true);
			doTest(DoCommand.makeAddTableCommad(tables[i], COLSNAME, COLSTYPE), false);
		}
	}
	
	@Test
	public void dropExistingTable() {
		createDataBase(MECHANICAL);
		String[] tables = {"physics", "math", "drawing"};
		makeTables(tables);
		for(int i = 0 ; i < tables.length ; i++) {
			doPathTest(MECHANICAL, tables[i], true);
			doTest(DoCommand.makeDropTableCommand(tables[i]), true);
			doPathTest(MECHANICAL, tables[i], false);
		}
	}
	
	@Test 
	public void dropNonExistingTable() {
		createDataBase(CHEMICAL);
		String[] tables = {"chemistry", "theory", "sub3"};
		for(int i = 0 ; i < tables.length ; i++) {
			doPathTest(CHEMICAL, tables[i], false);
			doTest(DoCommand.makeDropTableCommand(tables[i]), false);
		}
	}
	
	private void makeTables(String[] tables) {
		for(int i = 0 ; i < tables.length ; i++) {
			sqlParser.parseSQL(DoCommand.makeAddTableCommad(tables[i], COLSNAME, COLSTYPE));
		}
	}
	
	private void doTest(String command, boolean expectedResult) {
		boolean testCreat1 = !sqlParser.parseSQL(command).checkOperationFail();
		assertEquals(expectedResult, testCreat1);
	}

	private void doPathTest(int dbName, String tableName, boolean expectedResult) {
		assertEquals(expectedResult, pathChecker.isPresentTablePath(dirPath, DBNAMES[dbName], tableName));
		assertEquals(expectedResult, pathChecker.isPresentFilesPath(dirPath, DBNAMES[dbName], tableName));
	}

	private void createDataBase(int dbName) {
		if(!sqlParser.parseSQL(DoCommand.makeSelectCommand(DBNAMES[dbName])).checkOperationFail()) {
			sqlParser.parseSQL(DoCommand.makeDropCommand(DBNAMES[dbName]));
		}
		sqlParser.parseSQL("create database " + DBNAMES[dbName]);
	}
}