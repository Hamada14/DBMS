package testjdbc;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import javax.print.DocFlavor.STRING;

import org.junit.Assert;
import org.junit.Test;

import onlinetest.TestRunner;
import testing.PathsChecker;

public class TestDriver {

	private static final String[] WRONGURL = {"jdbc:://localhost", "hp:xmldb://localhost", ":jsondb://localhost"};
	private static final String[] ACCURL = {"jdbc:xmldb://localhost", "jdbc:protodb://localhost", "jdbc:jsondb://localhost"};
	private String tmp = System.getProperty("java.io.tmpdir");
	
	@Test
	public void testAcceptURL() {
		Driver driver = (Driver)TestRunner.getImplementationInstance();
		for(int i = 0; i < 3; i++) {
			try {
				assertEquals(true, driver.acceptsURL(ACCURL[i]));
			} catch(Throwable e) {
				TestRunner.fail("Wrong Accept URL, Expected to Accept that URL but was refused", e);
			}
		}
	}
	
	@Test
	public void testRefuseURL() {
		Driver driver = (Driver)TestRunner.getImplementationInstance();
		for(int i = 0; i < 3; i++) {
			try {
				assertEquals(false, driver.acceptsURL(WRONGURL[i]));
			} catch(Throwable e) {
				TestRunner.fail("Wrong Accept URL, Expected to refuse URL", e);
			}
		}
	}
	

	@Test 
	public void testMakeUnSuccConnection() {
		Properties prop = new Properties();
		Driver driver = (Driver)TestRunner.getImplementationInstance();
		String path = tmp + File.separator + "jdbc" + File.separator + Math.round((((float) Math.random()) * 100000));
		Connection connection;
		prop.setProperty("path", path);
		for(int i = 0; i < 3; i++) {
			try {
				connection = driver.connect(WRONGURL[i], prop);
				Assert.fail("Test connection, shouldnt accept the URL");
			} catch (SQLException e) {
				
			} catch (Throwable e) {
				TestRunner.fail("Test connection, shouldnt accept the URL", e);
			}
		}
	}
	
	@Test 
	public void testAcceptedConnection() {
		Properties prop = new Properties();
		Driver driver = (Driver)TestRunner.getImplementationInstance();
		Connection connection;
		PathsChecker pathChecker = PathsChecker.getInstance();
		for(int i = 0; i < 3; i++) {
			try {
				String path = tmp + File.separator + "jdbc" + File.separator + Math.round((((float) Math.random()) * 100000));
				prop.setProperty("path", path);
				connection = driver.connect(ACCURL[i], prop);
				assertEquals(true, pathChecker.isPresentDir(path));
			} catch (Throwable e) {
				TestRunner.fail("Fail to make connection", e);
			}
		}
	}

}