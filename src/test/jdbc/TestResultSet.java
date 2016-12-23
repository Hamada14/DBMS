package test.jdbc;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import JDBC.driver.DBMSDriver;

public class TestResultSet {

	private static final String path = System.getProperty("java.io.tmpdir");
	private static final String JDBC = "jdbc", LOCAL_HOST = "//localhost";
	private static final String XML_DB = "xmldb";

	private Statement createUseDB() {
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
			return statement;
		} catch (SQLException e) {
			return null;
		}
	}

	@Test
	public void creation() throws SQLException {
		java.sql.Statement statement = createUseDB();
		try {
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('mariam', 20, '1995-06-07', 543.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('moamen', 20, '1996-01-06', 733.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Rofaida', 20, '1993-03-21', 0.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Salah', 20, '1996-10-23', 121.2)");
			java.sql.ResultSet rs = statement.executeQuery("SELECT * from tabletest");
			assertEquals("Wrong connection between resultSet and statement", rs.getStatement(), statement);
		} catch (Throwable e) {
			Assert.fail("failed to creat a proper result set");
		} finally {
			statement.close();
		}
	}

	@Test
	public void findColumn() throws SQLException {
		java.sql.Statement statement = createUseDB();
		try {
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('mariam', 20, '1995-06-07', 543.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('moamen', 20, '1996-01-06', 733.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Rofaida', 20, '1993-03-21', 0.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Salah', 20, '1996-10-23', 121.2)");
			java.sql.ResultSet rs = statement.executeQuery("SELECT * from tabletest");
			assertEquals("Wrong return by findColumn", rs.findColumn("name"), 1);
			assertEquals("Wrong return by findColumn", rs.findColumn("iq"), 4);
			assertEquals("Wrong return by findColumn", rs.findColumn("birth"), 3);

		} catch (Throwable e) {
		} finally {
			statement.close();
		}
	}

	@Test
	public void iteratorBehavior() throws SQLException {
		java.sql.Statement statement = createUseDB();
		try {
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('mariam', 20, '1995-06-07', 543.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('moamen', 20, '1996-01-06', 733.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Rofaida', 20, '1993-03-21', 0.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Salah', 20, '1996-10-23', 121.2)");
			java.sql.ResultSet rs = statement.executeQuery("SELECT * from tabletest");
			assertEquals("Wrong beginning reference", true, rs.isBeforeFirst());
			assertEquals("Wrong last reference", false, rs.isLast());
			assertEquals("Wrong after last reference", false, rs.isAfterLast());
			rs.next();
			assertEquals("Wrong beginning reference", true, rs.isFirst());
			int countRows = 1;
			while (rs.next()) {
				assertEquals("Wrong beginning reference", false, rs.isBeforeFirst());
				countRows++;
			}
			assertEquals("Wrong number of rows", 4, countRows);
			assertEquals("Wrong after last reference", true, rs.isAfterLast());
			rs.previous();
			assertEquals("Wrong last reference", true, rs.isLast());
		} catch (Throwable e) {
			Assert.fail("failed to test resultset iterator behavior");
		} finally {
			statement.close();
		}
	}

	@Test
	public void stringData() throws SQLException {
		java.sql.Statement statement = createUseDB();
		try {
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('mariam', 20, '1995-06-07', 543.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('moamen', 20, '1996-01-06', 733.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Rofaida', 20, '1993-03-21', 0.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Salah', 20, '1996-10-23', 121.2)");
			java.sql.ResultSet rs = statement.executeQuery("SELECT * from tabletest");
			rs.next();
			String result = rs.getString(1);
			assertEquals("Wrong return", new String("mariam"), result);
			rs.next();
			result = rs.getString(1);
			assertEquals("Wrong return", new String("moamen"), result);
			result = rs.getString("name");
			assertEquals("Wrong return", new String("moamen"), result);
		} catch (Throwable e) {
			Assert.fail("failed to test resultset string result behavior, wrong return type");
		} finally {
			statement.close();
		}
	}

	@Test
	public void dateData() throws SQLException {
		java.sql.Statement statement = createUseDB();
		try {
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('mariam', 20, '1995-06-07', 543.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('moamen', 20, '1996-01-06', 733.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Rofaida', 20, '1993-03-21', 0.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Salah', 20, '1996-10-23', 121.2)");
			java.sql.ResultSet rs = statement.executeQuery("SELECT * from tabletest");
			rs.next();
			java.sql.Date result = rs.getDate(3);
			assertEquals("Wrong return", new String("1995-06-07"), result.toString());
			rs.next();
			result = rs.getDate(3);
			assertEquals("Wrong return", new String("1996-01-06"), result.toString());
			result = rs.getDate("birth");
			assertEquals("Wrong return", new String("1996-01-06"), result.toString());
		} catch (Throwable e) {
			Assert.fail("failed to test resultset date result behavior");
		} finally {
			statement.close();
		}
	}

	@Test
	public void floatData() throws SQLException {
		java.sql.Statement statement = createUseDB();
		try {
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('mariam', 20, '1995-06-07', 543.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('moamen', 20, '1996-01-06', 733.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Rofaida', 20, '1993-03-21', 0.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Salah', 20, '1996-10-23', 121.2)");
			java.sql.ResultSet rs = statement.executeQuery("SELECT * from tabletest");
			rs.next();
			Float result = rs.getFloat(4);
			assertEquals("Wrong return", new String("543.2"), result.toString());
			rs.next();
			result = rs.getFloat(4);
			assertEquals("Wrong return", new String("733.2"), result.toString());
			result = rs.getFloat("iq");
			assertEquals("Wrong return", new String("733.2"), result.toString());
		} catch (Throwable e) {
			Assert.fail("failed to test resultset float result behavior");
		} finally {
			statement.close();
		}
	}

	@Test
	public void intData() throws SQLException {
		java.sql.Statement statement = createUseDB();
		try {
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('mariam', 20, '1995-06-07', 543.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('moamen', 21, '1996-01-06', 733.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Rofaida', 20, '1993-03-21', 0.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Salah', 20, '1996-10-23', 121.2)");
			java.sql.ResultSet rs = statement.executeQuery("SELECT * from tabletest");
			rs.next();
			int result = rs.getInt(2);
			assertEquals("Wrong return", new String("20"), Integer.toString(result));
			rs.next();
			result = rs.getInt(2);
			assertEquals("Wrong return", new String("21"), Integer.toString(result));
			result = rs.getInt("age");
			assertEquals("Wrong return", new String("21"), Integer.toString(result));
		} catch (Throwable e) {
			Assert.fail("failed to test resultset int result behavior");
		} finally {
			statement.close();
		}
	}

	private java.sql.Date makeDate(String date) {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date utilDate;
		try {
			utilDate = sdf1.parse(date);
			java.sql.Date sqlStartDate = new Date(utilDate.getTime());
			return sqlStartDate;
		} catch (ParseException e) {
			return null;
		}
	}

	@Test
	public void objectData() throws SQLException {
		java.sql.Statement statement = createUseDB();
		try {
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('mariam', 20, '1995-06-07', 543.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('moamen', 20, '1996-01-06', 733.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Rofaida', 20, '1993-03-21', 0.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Salah', 25, '1996-10-23', 121.2)");
			java.sql.ResultSet rs = statement.executeQuery("SELECT * from tabletest");
			rs.next();
			String[] name = new String[] { "mariam", "Salah" };
			int[] age = new int[] { 20, 25 };
			float[] iq = new float[] { 543.2F, 121.2F };
			Date[] date = new java.sql.Date[2];
			date[0] = makeDate("1995-06-07");
			date[1] = makeDate("1996-10-23");
			Object result = rs.getObject(1);
			assertEquals("string data types aren't equals", result, name[0]);
			result = rs.getObject(2);
			assertEquals("int data types aren't equals", result, age[0]);
			result = rs.getObject(3);
			assertEquals("date data types aren't equals", result, date[0]);
			result = rs.getObject(4);
			assertEquals("float data types aren't equals", result, iq[0]);
			rs.next();
			rs.next();
			rs.next();
			result = rs.getObject("name");
			assertEquals("string data types aren't equals", result, name[1]);
			result = rs.getObject("age");
			assertEquals("int data types aren't equals", result, age[1]);
			result = rs.getObject("birth");
			assertEquals("date data types aren't equals", result, date[1]);
			result = rs.getObject("iq");
			assertEquals("float data types aren't equals", result, iq[1]);
		} catch (Throwable e) {
			Assert.fail("failed to test resultset object result behavior");
		} finally {
			statement.close();
		}
	}

	@Test
	public void complexIteratorBehavior() throws SQLException {
		java.sql.Statement statement = createUseDB();
		try {
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('mariam', 20, '1995-06-07', 543.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('moamen', 20, '1996-01-06', 733.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Rofaida', 20, '1993-03-21', 0.2)");
			statement.execute("INSERT INTO tabletest(name, age, birth, iq) VALUES ('Salah', 20, '1996-10-23', 121.2)");
			java.sql.ResultSet rs = statement.executeQuery("SELECT * from tabletest");
			String firstError = "Wrong first reference";
			String lastError = "Wrong last reference";
			assertEquals(firstError, false, rs.previous());
			assertEquals(firstError, false, rs.isFirst());
			rs.next();
			assertEquals(firstError, true, rs.isFirst());
			rs.next();
			assertEquals(firstError, false, rs.isFirst());
			rs.previous();
			assertEquals(firstError, true, rs.isFirst());
			rs.previous();
			assertEquals(firstError, false, rs.isFirst());
			rs.absolute(5);
			assertEquals("Wrong after last reference", true, rs.isAfterLast());
			assertEquals(lastError, false, rs.isLast());
			rs.previous();
			assertEquals(lastError, true, rs.isLast());
			rs.previous();
			assertEquals(lastError, false, rs.isLast());
			rs.next();
			assertEquals(lastError, true, rs.isLast());
		} catch (Throwable e) {
			Assert.fail("failed to test resultset iterator behavior");
		} finally {
			statement.close();
		}
	}

	@Test
	public void close() throws SQLException {
		java.sql.Statement statement = createUseDB();
		try {
			java.sql.ResultSet rs = statement.executeQuery("SELECT * from tabletest");
			rs.close();
			rs.next();
			Assert.fail("ResultSet isn't closed properly");
		} catch (SQLException e) {

		} catch (Throwable e) {
			Assert.fail("failed to test resultset closing behavior");
		} finally {
			statement.close();
		}
	}
}
