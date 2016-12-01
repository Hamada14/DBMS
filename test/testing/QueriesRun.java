package testing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import sqlParsers.SQLParserSingleton;

public class QueriesRun {
  private SQLParserSingleton instance;
  private PathsChecker pathChecker;
  private static final String[] DBNAMES = {"Countries" , "Technologies" , "Mountains", "Family"};
  private static final String[] TABLES = {"AMERICA", "EGYPT", "SAMSUNG", "APPLE", "ALB", "HIMALAYA", "FATHER", "MOTHER"}; 
  private static final String[] COLSNAME = {"Col1" , "Col2" , "Comment"};
  private static final String[] COLSTYPE = {"int", "int", "varchar"};
  private static final String END = ";" , DELETE = "DELETE FROM ", WHERE = " where ";
  private static final String UPDATE = "update ", SET = " SET ";
  @Before
  public void setUp() throws Exception {
    instance = SQLParserSingleton.getInstance();
    pathChecker = PathsChecker.getInstance();
    pathChecker.creatDirectory();
    initializeDBS();
  }
  
  private void initializeDBS() {
    for(int i = 0 ; i < DBNAMES.length ; i++) {
      if(instance.parseSQL(DoCommand.makeSelectCommand(DBNAMES[i]))) {
        instance.parseSQL(DoCommand.makeDropCommand(DBNAMES[i]));
      }
      instance.parseSQL(DoCommand.makeCreationCommand(DBNAMES[i])); 
      instance.parseSQL(DoCommand.makeAddTableCommad(TABLES[2 * i], COLSNAME, COLSTYPE));
      instance.parseSQL(DoCommand.makeAddTableCommad(TABLES[2 * i + 1], COLSNAME, COLSTYPE));
    }
  }
  
  @Test
  public void testValidAddQuery() {
    instance.parseSQL(DoCommand.makeSelectCommand(DBNAMES[0]));
    String[] currCols1 = {"Col1" , "Col2" , "Comment"};
    String[] colsValues1 = {"56", "59", "\'HERE is a comment\'"};
    String[] currCols2 = {"Col1", "Comment"};
    String[] colsValues2 = {"56", "\'DATBASES comment\'"};
    String command1 = DoCommand.makeAddQueryCommand(TABLES[0], currCols1, colsValues1);
    String command2 = DoCommand.makeAddQueryCommand(TABLES[1], currCols2, colsValues2);
    doTest(command1, true);
    doTest(command2, true);
  }
  
  @Test
  public void testInvalidAddQuery() {
    instance.parseSQL(DoCommand.makeSelectCommand(DBNAMES[0]));
    String[] currCols1 = {"Col1" , "Col2" , "Comment"};
    String[] colsValues1 = {"name", "\'\'", "5"};
    String[] currCols2 = {"Col1", "nonePresentOne"};
    String[] colsValues2 = {"56", "\'DATBASES comment\'"};
    String command1 = DoCommand.makeAddQueryCommand(TABLES[0], currCols1, colsValues1);
    String command2 = DoCommand.makeAddQueryCommand(TABLES[1], currCols2, colsValues2);
    doTest(command1, false);
    doTest(command2, false);
  }
  
  @Test
  public void testDeleteQuery() {
    instance.parseSQL(DoCommand.makeSelectCommand(DBNAMES[1]));
    String[] currCols1 = {"Col1" , "Col2" , "Comment"};
    String[] colsValues1 = {"56", "59", "\'HERE is a comment\'"};
    String[] currCols2 = {"Col1", "Comment"};
    String[] colsValues2 = {"56", "\'DATBASES comment\'"};
    String command1 = DoCommand.makeAddQueryCommand(TABLES[2], currCols1, colsValues1);
    String command2 = DoCommand.makeAddQueryCommand(TABLES[3], currCols2, colsValues2);
    instance.parseSQL(command1);
    instance.parseSQL(command2);
    String del1 = DELETE + TABLES[2] + WHERE + COLSNAME[1] + " = 59" + END; //true
    String del2 = DELETE + TABLES[2] + WHERE + COLSNAME[1] + " 59" + END; //false
    String del3 = DELETE + TABLES[3] + COLSNAME[2] + " < \'DATBASES comment\'" + END; //false
    String del4 = DELETE + TABLES[3] + WHERE + COLSNAME[2] + END; //true
    String del5 = DELETE + TABLES[3] + WHERE + COLSNAME[2] + " > \'A\'" + END;//true
    doTest(del1, true);
    doTest(del2, false);
    doTest(del3, false);
    doTest(del4, false);
    doTest(del5, true);
  }
  
  //UPDATE Persons SET PersonID = 15, FirstName = 'updateOK' WHERE PersonID = 14;
  //Test update with and or not works well
  @Test 
  public void testUpDateQuery1() {
    instance.parseSQL(DoCommand.makeSelectCommand(DBNAMES[2]));
    String[] currCols1 = {"Col1" , "Col2" , "Comment"};
    String[] colsValues1 = {"56", "59", "\'X\'"};
    String command1 = DoCommand.makeAddQueryCommand(TABLES[4], currCols1, colsValues1);
    colsValues1[0] = "1000"; colsValues1[1] = "10"; colsValues1[2] = "\'A\'";
    String command3 = DoCommand.makeAddQueryCommand(TABLES[4], currCols1, colsValues1);
    String queries[] = {command1, command3};
    makeQueries(queries);
    String update1 = UPDATE + TABLES[4] + SET + COLSNAME[0] + " = 300" + WHERE + COLSNAME[0] + " = 56 AND " + COLSNAME[1] + " > 40;";
    String update2 = UPDATE + TABLES[4] + SET + COLSNAME[2] + " = \'Z\'" + WHERE + COLSNAME[0] + " > 3 OR " + COLSNAME[2] + " > \'A\';";
    String update3 = UPDATE + TABLES[4] + COLSNAME[0] + " = 300" + WHERE + COLSNAME[0] + " = 56 AND " + COLSNAME[1] + " > 40;";
    String update4 = UPDATE + SET + COLSNAME[2] + " = \'Z\'" + WHERE + COLSNAME[0] + " > 3 OR " + COLSNAME[2] + " > \'A\';";
    doTest(update1, true);
    doTest(update2, true);
    doTest(update3, false);
    doTest(update4, false);
  }
  
  @Test
  public void updateQuery2() {
    instance.parseSQL(DoCommand.makeSelectCommand(DBNAMES[2]));
    String[] currCols2 = {"Col1", "Comment"};
    String[] colsValues2 = {"56", "\'E\'"};
    String command2 = DoCommand.makeAddQueryCommand(TABLES[5], currCols2, colsValues2);
    colsValues2[0] = "200"; colsValues2[1] = "\'X\'";
    String command4 = DoCommand.makeAddQueryCommand(TABLES[5], currCols2, colsValues2);
    String queries[] = {command2, command4};
    makeQueries(queries);
    String update1 = UPDATE + TABLES[5] + SET + COLSNAME[2] + " = \'essam\'" + WHERE + "nottt " + COLSNAME[0] +  " = 5500;";
    String update2 = UPDATE + TABLES[5] + SET + COLSNAME[2] + " = \'ESSA\'" + WHERE + "not " + COLSNAME[0] +  " !@#= 0;";
    String update3 = UPDATE + TABLES[5] + SET + COLSNAME[2] + " = \'Q\'" + WHERE + "not " + COLSNAME[0] +  " = 200;";
    String update4 = UPDATE + TABLES[5] + SET + COLSNAME[2] + " = \'Q\'" + WHERE + "not " + COLSNAME[0] +  " = 0;";
    doTest(update1, false);
    doTest(update2, false);
    doTest(update3, true);
    doTest(update4, true);
  }
  private void makeQueries(String[] queries) {
    for(int i = 0 ; i < queries.length ; i++) {
      instance.parseSQL(queries[i]);
    }
  }
  
  private void doTest(String command, boolean expectedResult) {
    boolean testCreat1 = instance.parseSQL(command);
    System.out.println(command + " " + testCreat1);
    assertEquals(expectedResult, testCreat1);
  }
  
}