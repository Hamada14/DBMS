package test.jdbc;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import JDBC.driver.DBMSDriver;
import test.dbms.PathsChecker;

public class TestDriver {

	private static final String[] WRONGURL = { "jdbc:://localhost", "hp:xmldb://localhost", ":jsondb://localhost" };
	private static final String[] ACCURL = { "jdbc:xmldb://localhost", "jdbc:protodb://localhost",
			"jdbc:jsondb://localhost" };

	private static final String tmp = System.getProperty("java.io.tmpdir");

	@Test
	public void acceptURL() {
		Driver driver = (Driver) new DBMSDriver();
		for (int i = 0; i < 3; i++) {
			try {
				assertEquals(true, driver.acceptsURL(ACCURL[i]));
			} catch (Throwable e) {
				Assert.fail("Wrong Accept URL, Expected to Accept that URL but was refused");
			}
		}
	}

	@Test
	public void refuseURL() {
		Driver driver = (Driver) new DBMSDriver();
		for (int i = 0; i < 3; i++) {
			try {
				assertEquals(false, driver.acceptsURL(WRONGURL[i]));
			} catch (Throwable e) {
				Assert.fail("Wrong Accept URL, Expected to refuse URL");
			}
		}
	}

	@Test
	public void makeUnSuccConnection() {
		Properties prop = new Properties();
		Driver driver = (Driver) new DBMSDriver();
		String path = tmp + File.separator + "jdbc" + File.separator + Math.round((((float) Math.random()) * 100000));
		prop.setProperty("path", path);
		for (int i = 0; i < 3; i++) {
			try {
				Connection connection = driver.connect(WRONGURL[i], prop);
				connection.close();
				Assert.fail("Test connection, shouldnt accept the URL");
			} catch (SQLException e) {

			} catch (Throwable e) {
				Assert.fail("Test connection, shouldnt accept the URL");
			}
		}
	}

	@Test
	public void acceptedConnection() {
		Properties prop = new Properties();
		Driver driver = (Driver) new DBMSDriver();
		PathsChecker pathChecker = PathsChecker.getInstance();
		for (int i = 0; i < 3; i++) {
			try {
				String path = tmp + File.separator + "jdbc" + File.separator
						+ Math.round((((float) Math.random()) * 100000));
				prop.setProperty("path", path);
				Connection connection = driver.connect(ACCURL[i], prop);
				connection.close();
				assertEquals(true, pathChecker.isPresentDir(path));
			} catch (Throwable e) {
				Assert.fail("Fail to make connection");
			}
		}
	}

}