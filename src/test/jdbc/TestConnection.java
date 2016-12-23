package test.jdbc;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import JDBC.connection.DBMSConnection;
import JDBC.driver.DBMSDriver;
import JDBC.statement.DBMSStatement;

public class TestConnection {

	private static final String path = System.getProperty("java.io.tmpdir");
	private static final String JDBC = "jdbc", LOCAL_HOST = "//localhost";

	@Test
	public void connectionXML() {
		try {
			String protocol = "xmldb";
			Driver driver = new DBMSDriver();
			Properties info = new Properties();
			File dbDir = new File(path + "/jdbc/" + Math.round((((float) Math.random()) * 100000)));
			info.put("path", dbDir.getAbsoluteFile());
			DBMSConnection connection = (DBMSConnection) driver.connect(JDBC + ":" + protocol + ":" + LOCAL_HOST, info);
			connection.close();
		} catch (Exception e) {
			fail("Refuse XML connection");
		}
	}

	@Test
	public void connectionJSON() {
		try {
			String protocol = "jsondb";
			Driver driver = new DBMSDriver();
			Properties info = new Properties();
			File dbDir = new File(path + "/jdbc/" + Math.round((((float) Math.random()) * 100000)));
			info.put("path", dbDir.getAbsoluteFile());
			DBMSConnection connection = (DBMSConnection) driver.connect(JDBC + ":" + protocol + ":" + LOCAL_HOST, info);
			connection.close();
		} catch (Exception e) {
			fail("Refuse JSON connection");
		}
	}

	@Test
	public void connectionProtcolBuffer() {
		try {
			String protocol = "protodb";
			Driver driver = new DBMSDriver();
			Properties info = new Properties();
			File dbDir = new File(path + "/jdbc/" + Math.round((((float) Math.random()) * 100000)));
			info.put("path", dbDir.getAbsoluteFile());
			DBMSConnection connection = (DBMSConnection) driver.connect(JDBC + ":" + protocol + ":" + LOCAL_HOST, info);
			connection.close();
		} catch (Exception e) {
			fail("Refuse XML connection");
		}
	}

	@Test
	public void statementCreation() {
		try {
			String protocol = "xmldb";
			Driver driver = new DBMSDriver();
			Properties info = new Properties();
			File dbDir = new File(path + "/jdbc/" + Math.round((((float) Math.random()) * 100000)));
			info.put("path", dbDir.getAbsoluteFile());
			DBMSConnection connection = (DBMSConnection) driver.connect(JDBC + ":" + protocol + ":" + LOCAL_HOST, info);
			assertNotNull("Unable to create statement", connection.createStatement());
			connection.close();
		} catch (Exception e) {
			fail("creation of statement fail");
		}
	}

	@Test
	public void connectionClose() {
		try {
			String protocol = "xmldb";
			Driver driver = new DBMSDriver();
			Properties info = new Properties();
			File dbDir = new File(path + "/jdbc/" + Math.round((((float) Math.random()) * 100000)));
			info.put("path", dbDir.getAbsoluteFile());
			DBMSConnection connection = (DBMSConnection) driver.connect(JDBC + ":" + protocol + ":" + LOCAL_HOST, info);
			assertFalse("Connection automatically closed", connection.isClosed());
			connection.close();
			assertTrue("Connection refuse closing", connection.isClosed());
		} catch (SQLException e) {
			fail("Conection can't be closed");
		}
	}

	@Test
	public void connectionClose2() {
		try {
			String protocol = "xmldb";
			Driver driver = new DBMSDriver();
			Properties info = new Properties();
			File dbDir = new File(path + "/jdbc/" + Math.round((((float) Math.random()) * 100000)));
			info.put("path", dbDir.getAbsoluteFile());
			DBMSConnection connection = (DBMSConnection) driver.connect(JDBC + ":" + protocol + ":" + LOCAL_HOST, info);
			connection.close();
			connection.createStatement();
			Assert.fail("Statement shouldn't be created when statement is closed");
		} catch (SQLException e) {

		} catch (Exception e) {
			Assert.fail("Invalid Exception throw");
		}
	}

	@Test
	public void connectionStatementClose() {
		try {
			String protocol = "xmldb";
			Driver driver = new DBMSDriver();
			Properties info = new Properties();
			File dbDir = new File(path + "/jdbc/" + Math.round((((float) Math.random()) * 100000)));
			info.put("path", dbDir.getAbsoluteFile());
			DBMSConnection connection = (DBMSConnection) driver.connect(JDBC + ":" + protocol + ":" + LOCAL_HOST, info);
			DBMSStatement st = (DBMSStatement) connection.createStatement();
			connection.close();
			assertTrue("Statement isn't closed due to closing connection", st.isClosed());
		} catch (Exception e) {
			fail("Refuse XML connection");
		}
	}
}
