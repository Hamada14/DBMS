package testing;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import sqlParsers.SQLParserSingleton;

public class DataBaseFunctionality {

	private static final String[] FORBIDDENNAME = { "create", "select", "insert", "delete", "update", "use", "from",
			"drop", "where", "and", "not", "or", "database", "table" };
	private static PathsChecker pathChecker;
	private static SQLParserSingleton instance;

	@BeforeClass
	public static void setUp() throws Exception {
		instance = SQLParserSingleton.getInstance();
		pathChecker = PathsChecker.getInstance();
		pathChecker.creatDirectory();
	}

	@Test
	public void testInvalidNames() {
		for(int i = 0 ; i < FORBIDDENNAME.length ; i++) {
			doTest(DoCommand.makeCreationCommand(FORBIDDENNAME[i]), false);
		}
	}
	
	@Test
	public void testCreatValidNames() {
		String[] names = {"School", "Students", "Country", "Projects", "University"};
		for(int i = 0 ; i < names.length ; i++) {
			if(instance.parseSQL(DoCommand.makeSelectCommand(names[i]))) {
				instance.parseSQL(DoCommand.makeDropCommand(names[i]));
			}
			doTest(DoCommand.makeCreationCommand(names[i]), true);
			doPathTest(names[i], true);
		}
	}
	
	@Test
	public void testCreatAlreadyTakenDBName() {
		String[] names = {"College", "Home", "Entity", "Staff"};
		creatDBFolder(names);
		for(int i = 0 ; i < names.length ; i++) {
			doTest(DoCommand.makeCreationCommand(names[i]), false);
			doPathTest(names[i], true);
		}
	}
	
	@Test
	public void testSelectAlreadyExistingDataBase() {
		String[] names = {"Town", "City", "PlaySchool"};
		creatDBFolder(names);
		for(int i = 0 ; i < names.length ; i++) {
			doTest(DoCommand.makeSelectCommand(names[i]), true);
		}
	}
	
	@Test
	public void testSelectNonExistingDataBase() {
		String names[] = {"Town123" , "City123" , "PlaySchool123"};
		for(int i = 0 ; i < names.length ; i++) {
			doTest(DoCommand.makeSelectCommand(names[i]), false);
			doPathTest(names[i], false);
		}
	}
	
	@Test
	public void testDropExistingDataBase() {
		String[] names = {"Actors", "Actress", "Singers"};
		creatDBFolder(names);
		for(int i = 0 ; i < names.length ; i++) {
			doPathTest(names[i], true);
			doTest(DoCommand.makeDropCommand(names[i]), true);
			doPathTest(names[i], false);
		}
	}
	
	@Test
	public void testDropNonExistingDataBase() {
		String[] names = {"Actors111" , "Actress1111" , "Singersss11"};
		for(int i = 0 ; i < names.length ; i++) {
			doPathTest(names[i], false);
			doTest(DoCommand.makeDropCommand(names[i]), false);
		}
	}
	
	private void creatDBFolder(String[] dbName) {
		for(int i = 0 ; i < dbName.length ; i++) {
			if(instance.parseSQL(DoCommand.makeSelectCommand(dbName[i]))) {
				instance.parseSQL(DoCommand.makeDropCommand(dbName[i]));
			}
			instance.parseSQL(DoCommand.makeCreationCommand(dbName[i]));
		}
	}

	private void doTest(String command, boolean expectedResult) {
		boolean testCreat1 = instance.parseSQL(command);
		assertEquals(expectedResult, testCreat1);
	}
	
	private void doPathTest(String dbName , boolean expectedResult) {
		assertEquals(expectedResult, pathChecker.isPresentDBPath(dbName));
	}

}