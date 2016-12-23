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

public class TestResultSetMetaData {

	private static final String path = System.getProperty("java.io.tmpdir");
	private static final String JDBC = "jdbc", LOCAL_HOST = "//localhost";
	private static final String XML_DB = "xmldb";

	private java.sql.Statement createUseDB() {
		String url = JDBC + ":" + XML_DB + ":" + LOCAL_HOST;
		Properties prop = new Properties();
		String fullPath = path + File.separator + JDBC + File.separator + Math.round((float) Math.random() * 100000);
		prop.setProperty("path", fullPath);
		Driver driver = (Driver) new DBMSDriver();
		try {
			Connection connection = (Connection) driver.connect(url, prop);
			java.sql.Statement statement = connection.createStatement();
			statement.execute("CREATE DATABASE testDatabase");
			statement.execute("USE testDatabase");
			statement.execute("CREATE TABLE tabletest(name varchar, age int, birth date, iq float)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('mariam', 20, '1995-06-07', 543.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('moamen', 20, '1996-01-06', 733.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Rofaida', 20, '1993-03-21', 0.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Salah', 20, '1996-10-23', 121.2)");
			return statement;
		} catch (SQLException e) {
			return null;
		}
	}

	@Test
	public void creation() {
		java.sql.Statement st = createUseDB();
		try {
			java.sql.ResultSet rs = st.executeQuery("SELECT * from tabletest");
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			assertNotNull("Failed to create ResultSetMetaData, method returns null", rsmd);
		} catch (Exception e) {
			Assert.fail("Failed to create ResultSetMetaData");
		}
	}

	@Test
	public void tableDataValidation() {
		java.sql.Statement st = createUseDB();
		try {
			java.sql.ResultSet rs = st.executeQuery("Select * from tabletest");
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i < 4; i++) {
				assertEquals("Wrong table name return by meta data", "tabletest", rsmd.getTableName(i));
			}
			assertEquals("Wrong column count", 4, rsmd.getColumnCount());
			assertEquals("Wrong column type", java.sql.Types.VARCHAR, rsmd.getColumnType(1));
			assertEquals("Wrong column type", java.sql.Types.INTEGER, rsmd.getColumnType(2));
			assertEquals("Wrong column type", java.sql.Types.DATE, rsmd.getColumnType(3));
			assertEquals("Wrong column type", java.sql.Types.FLOAT, rsmd.getColumnType(4));
			assertEquals("Wrong table name return by metadata", null, rsmd.getTableName(0));
			assertEquals("Wrong table name return by metadata", "tabletest", rsmd.getTableName(1));
			assertEquals("Wrong table name return by metadata", null, rsmd.getTableName(5));
		} catch (Exception e) {
			Assert.fail("Failed to test data of ResultSeta metadata");
		}
	}

	@Test
	public void tableDataValidation2() {
		java.sql.Statement st = createUseDB();
		try {
			java.sql.ResultSet rs = st.executeQuery("Select name, age from tabletest");
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			assertEquals("Wrong column count", 2, rsmd.getColumnCount());
			assertEquals("Wrong column name", "name", rsmd.getColumnName(1));
			assertEquals("Wrong column name", "age", rsmd.getColumnName(2));
			rs = st.executeQuery("Select iq, birth from tabletest");
			rsmd = rs.getMetaData();
			assertEquals("Wrong column type", java.sql.Types.FLOAT, rsmd.getColumnType(2));
			assertEquals("Wrong column type", java.sql.Types.DATE, rsmd.getColumnType(1));
			assertEquals("Wrong table name return by metadata", null, rsmd.getTableName(0));
		} catch (Exception e) {
			Assert.fail("Failed to test data of ResultSeta metadata");
		}
	}
}
