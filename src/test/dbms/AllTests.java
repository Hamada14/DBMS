package test.dbms;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DataBaseFunctionality.class, QueriesRun.class, Selection.class, TestTableFunctionallity.class })
public class AllTests {

}
