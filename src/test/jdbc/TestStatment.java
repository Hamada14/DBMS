package test.jdbc;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import JDBC.driver.DBMSDriver;
import JDBC.statement.DBMSStatement;

public class TestStatment {

	private static final String path = System.getProperty("java.io.tmpdir");
	private static final String JDBC = "jdbc", LOCAL_HOST = "//localhost";
	private static final String XML_DB = "xmldb", JSON_DB = "jsondb", PROTO_DB = "protodb";

	private Connection getConnection(String protocolType) {
		String url = JDBC + ":" + protocolType + ":" + LOCAL_HOST;
		Properties prop = new Properties();
		String fullPath = path + File.separator + JDBC + File.separator + Math.round((float) Math.random() * 100000);
		prop.setProperty("path", fullPath);
		Driver driver = (Driver) new DBMSDriver();
		try {
			Connection connection = (Connection) driver.connect(url, prop);
			java.sql.Statement statment = connection.createStatement();
			statment.execute("CREATE DATABASE testDatabase");
			statment.close();
			return connection;
		} catch (SQLException e) {
			return null;
		}
	}

	@Test
	public void createDataBase() throws SQLException {
		Connection connection = getConnection(XML_DB);
		try {
			java.sql.Statement statement = (java.sql.Statement) connection.createStatement();
			statement.execute("CREATE DATABASE dbName");
			statement.execute("CREATE DATABASE Database2");
			statement.execute("CREATE DATABASE DataBase3");
			statement.close();
		} catch (Throwable e) {
			Assert.fail("failed to creat a database");
		}
		try {
			java.sql.Statement statement = (java.sql.Statement) connection.createStatement();
			statement.execute("CREATE DATABASE dbName");
			statement.execute("CREATE DATABASE Database2");
			statement.execute("CREATE DATABASE DataBase3");
			statement.close();
			Assert.fail("created an already existing database");
		} catch (SQLException e) {

		} catch (Throwable e) {
			Assert.fail("creation of already existing database");
		}
		connection.close();
	}

	@Test
	public void addTableProto() throws SQLException {
		Connection connection = getConnection(PROTO_DB);
		try {
			java.sql.Statement statement = (java.sql.Statement) connection.createStatement();
			statement.execute("CREATE TABLE table_firstTable(ID int, name varchar, birth date)");
			statement.execute("CREATE TABLE table_anotherTable(ID int, grade float, birth date)");
			statement.close();
		} catch (Throwable e) {
			Assert.fail("failed in test table creation");
		}
		try {
			java.sql.Statement statement = (java.sql.Statement) connection.createStatement();
			statement.execute("CREATE TABLE table_firstTable(ID int, name varchar, birth date)");
			statement.execute("CREATE TABLE table_anotherTable(ID int, grade float, birth date)");
			statement.close();
			Assert.fail("created an already existing table");
		} catch (SQLException e) {

		} catch (Throwable e) {
			Assert.fail("creation of an already existing table");
		}
		try {
			java.sql.Statement statement = (java.sql.Statement) connection.createStatement();
			statement.execute("CREATE TABLE table_anotherTable");
			statement.close();
			Assert.fail("created incomplete table");
		} catch (SQLException e) {

		} catch (Throwable e) {
			Assert.fail("Incomplete table");
		}
		connection.close();
	}

	@Test
	public void correctInsertionProto() throws SQLException {
		Connection connection = getConnection(PROTO_DB);
		try {
			java.sql.Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE Class(ID int, grade float, name varchar)");
			int count = statement.executeUpdate("INSERT INTO Class(grade, name, ID) VALUES (1.23, 'Hossam', 1)");
			Assert.assertEquals("Wrong Insertion", 1, count);
			count = statement.executeUpdate("INSERT INTO Class(grade, ID) VALUES (1.23, 1)");
			Assert.assertEquals("Wrong Insertion", 1, count);
			count = statement.executeUpdate("INSERT INTO Class(grade, name) VALUES (154.848, 'mo3az')");
			statement.close();
		} catch (Throwable e) {
			Assert.fail("Failed to insert into table");
		}
		connection.close();
	}

	@Test
	public void wrongInsertionMissTable() throws SQLException {
		Connection connection = getConnection(PROTO_DB);
		try {
			java.sql.Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE Class(ID int, grade float, name varchar)");
			statement.executeUpdate("INSERT INTO AbsentTable(grade, name, ID) VALUES (1.23, 'Hossam', 1)");
			Assert.fail("Inserted in absent Table");
			statement.close();
		} catch (SQLException e) {

		} catch (Throwable e) {
			Assert.fail("Wrong insertion");
		}
		connection.close();
	}

	@Test
	public void wrongInsertionMissColumns() throws SQLException {
		Connection connection = getConnection(PROTO_DB);
		try {
			java.sql.Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE Class(ID int, grade float, name varchar)");
			statement.executeUpdate("INSERT INTO AbsentTable(grade, missColumn, ID) VALUES (1.23, 'Hossam', 1)");
			Assert.fail("Inserted in absent column");
			statement.close();
		} catch (SQLException e) {

		} catch (Throwable e) {
			Assert.fail("Wrong insertion");
		}
		try {
			java.sql.Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE Class(ID int, grade float, name varchar)");
			statement.executeUpdate("INSERT INTO AbsentTable(grade) VALUES (1.23, 'Hossam', 1)");
			Assert.fail("More Values Than columns");
			statement.close();
		} catch (SQLException e) {

		} catch (Throwable e) {
			Assert.fail("Wrong insertion");
		}
		connection.close();
	}

	@Test
	public void deleteTable() throws SQLException {
		Connection connection = getConnection(JSON_DB);
		try {
			java.sql.Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE Class(ID int, grade float, name varchar)");
			int count1 = statement.executeUpdate("INSERT INTO Class(ID, name, grade) VALUES (5, 'Hamada', 5.6)");
			Assert.assertEquals("Insert returned a number != 1", 1, count1);
			int count2 = statement.executeUpdate("INSERT INTO Class(ID, name, grade) VALUES (60, 'Hamada', 10.63)");
			Assert.assertEquals("Insert returned a number != 1", 1, count2);
			int count3 = statement.executeUpdate("INSERT INTO Class(ID, name, grade) VALUES (5, 'Hamada', 6.5)");
			Assert.assertEquals("Insert returned a number != 1", 1, count3);
			int count4 = statement.executeUpdate("DELETE From Class WHERE grade > 6.4");
			Assert.assertEquals("Delete returned wrong number", 2, count4);
			statement.close();
		} catch (Throwable e) {
			Assert.fail("Failed to delete from table");
		}
		connection.close();
	}

	@Test
	public void deleteMissTable() throws SQLException {
		Connection connection = getConnection(JSON_DB);
		try {
			java.sql.Statement statement = connection.createStatement();
			statement.execute("DELETE From Class");
			Assert.fail("Delete Missing Table :)");
			statement.close();
		} catch (SQLException e) {

		} catch (Throwable e) {
			Assert.fail("Failed to delete from table");
		}
		connection.close();
	}

	@Test
	public void alterTable() throws SQLException {
		Connection connection = getConnection(PROTO_DB);
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE SCHOOL(Students varchar, age int)");
			boolean alter = statement.execute("ALTER TABLE SCHOOL ADD entryDate date");
			Assert.assertEquals("Wrong alter", false, alter);
			statement.close();
		} catch (Throwable e) {
			Assert.fail("Failed to test ALTER TABLE from table");
		}
		connection.close();
	}

	@Test
	public void updateQuery() throws SQLException {
		Connection connection = getConnection(XML_DB);
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE Class(ID int, grade float, name varchar)");
			int count1 = statement.executeUpdate("INSERT INTO Class(ID, name, grade) VALUES (5, 'Hamada', 5.6)");
			int count2 = statement.executeUpdate("INSERT INTO Class(ID, name, grade) VALUES (5, 'Hamada', 9.6)");
			int count3 = statement.executeUpdate("INSERT INTO Class(ID, name, grade) VALUES (5, 'Hamada', 3.6)");
			Assert.assertEquals("Cannot Insert", 1, count1);
			Assert.assertEquals("Cannot Insert", 1, count2);
			Assert.assertEquals("Cannot Insert", 1, count3);
			int count4 = statement.executeUpdate("UPDATE Class SET ID = 5 WHERE grade < 9.6");
			int count5 = statement.executeUpdate("UPDATE Class SET ID = 5 WHERE grade > 9.6");
			Assert.assertEquals("failed to update", 2, count4);
			Assert.assertEquals("failed to update", 0, count5);
			statement.close();
		} catch (Throwable e) {
			Assert.fail("failed to update");
		}
		connection.close();
	}

	@Test
	public void invalidUpdate() throws SQLException {
		Connection connection = getConnection(JSON_DB);
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE Class(ID int, grade float, name varchar)");
			int count1 = statement.executeUpdate("INSERT INTO Class(ID, name, grade) VALUES (5, 'Hamada', 5.6)");
			Assert.assertEquals("Cannot Insert", 1, count1);
			statement.executeUpdate("UPDATE Class SET WRONG_COL = 5 WHERE grade < 9.6");
			Assert.fail("updated an absent column");
			statement.close();
		} catch (SQLException e) {

		} catch (Throwable e) {
			Assert.fail("failed to update");
		}
		connection.close();
	}

	@Test
	public void batch() throws SQLException {
		Connection connection = getConnection(JSON_DB);
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE Class(ID int, grade float, name varchar)");
			statement.addBatch("INSERT INTO Class(ID, name, grade) VALUES (5, 'Hamada', 5.6)");
			statement.addBatch("INSERT INTO Class(ID, name, grade) VALUES (5, 'Hamada', 9.6)");
			statement.addBatch("INSERT INTO Class(ID, name, grade) VALUES (5, 'Hamada', 3.6)");
			statement.addBatch("UPDATE Class SET ID = 5 WHERE grade < 9.6");
			statement.addBatch("UPDATE Class SET ID = 5 WHERE grade > 9.6");
			int[] counts = statement.executeBatch();
			Assert.assertEquals("Failed to execute batch", 5, counts.length);
			for (int i = 0; i < 3; i++)
				Assert.assertEquals("failed to insert with batch", counts[i], DBMSStatement.SUCCESS_NO_INFO);
			Assert.assertEquals("Failed to update with batch", 2, counts[3]);
			Assert.assertEquals("Failed to update with batch", 0, counts[4]);
			statement.clearBatch();
			counts = statement.executeBatch();
			Assert.assertEquals("Tested empty batch", 0, counts.length);
			statement.close();
		} catch (Throwable e) {
			Assert.fail("Failed to test Batch");
		}
		connection.close();
	}

	@Test
	public void closedStatement() throws SQLException {
		Connection connection = getConnection(XML_DB);
		try {
			Statement statement = connection.createStatement();
			statement.close();
			statement.execute("CREATE TABLE Class(ID int, grade float, name varchar)");
			Assert.fail("Executed closet statement");
		} catch (SQLException e) {

		} catch (Throwable e) {
			Assert.fail("Failed test closed statement");
		}
		connection.close();
	}
}
