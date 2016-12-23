package JDBC.driver;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

import org.apache.logging.log4j.LogManager;

import JDBC.connection.DBMSConnection;

public class DBMSDriver implements Driver {

	private static final String COLON = ":";
	private static final String JDBCWORD = "jdbc", LOCALHOST = "//localhost", PATH = "path", EMPTY = "";
	private static final int XMLBACKEND = 1, PROTOBUFBACKEND = 2, JSONBACKEND = 3;
	private static final int JDBCPOS = 0, PROTOCOLTYPEPOS = 1, LOCALHOSTPOS = 2;
	private static final String XMLDB = "xmldb", PROTOBUFDB = "protodb", JSONDB = "jsondb", ALTERANTIVEDB = "altdb";
	private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(DBMSDriver.class);

	private String path;

	public DBMSDriver() {
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		try {
			getUrlInformation(url);
			logger.info("URL Accepted: " + url);
		} catch (SQLException e) {
			logger.info("URL Rejected: " + url);
			return false;
		}
		return true;
	}

	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		String[] urlInf;
		try {
			urlInf = getUrlInformation(url);
		} catch (SQLException e) {
			logger.info("Connection failed");
			throw new SQLException();
		}
		Connection connection = null;
		try {
			String directoriesPath = info.get(PATH).toString();
			this.path = directoriesPath;
			int protocolType = getBackEndProtocol(urlInf[PROTOCOLTYPEPOS]);
			connection = new DBMSConnection(directoriesPath, protocolType);
		} catch (SQLException e) {
			logger.info("Connection failed, Invalid protocol type");
			throw new SQLException("Invalid Protocol type");
		}
		logger.info("Connection is open");
		return connection;
	}

	private int getBackEndProtocol(String protocolType) throws SQLException {
		switch (protocolType) {
		case XMLDB:
			return XMLBACKEND;
		case PROTOBUFDB:
			return PROTOBUFBACKEND;
		case JSONDB:
			return JSONBACKEND;
		case ALTERANTIVEDB:
			return PROTOBUFBACKEND;
		default:
			throw new SQLException();
		}
	}

	private String[] getUrlInformation(String url) throws SQLException {
		if (url == null) {
			throw new SQLException();
		}
		String[] urlInf = url.split(COLON);
		if (!isCorrectData(urlInf)) {
			throw new SQLException();
		}
		return urlInf;
	}

	private boolean isCorrectData(String[] urlInf) {
		return urlInf.length == 3 && urlInf[JDBCPOS].equalsIgnoreCase(JDBCWORD)
				&& urlInf[LOCALHOSTPOS].equalsIgnoreCase(LOCALHOST) && !urlInf[PROTOCOLTYPEPOS].equals(EMPTY);
	}

	@Override
	public int getMajorVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMinorVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		throw new UnsupportedOperationException();
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String arg0, Properties arg1) throws SQLException {
		DriverPropertyInfo[] property = new DriverPropertyInfo[1];
		if (this.path != null) {
			property[0] = new DriverPropertyInfo("path", this.path);
		} else {
			property[0] = new DriverPropertyInfo("path", "Not Initialized");
		}
		return property;
	}

	@Override
	public boolean jdbcCompliant() {
		throw new UnsupportedOperationException();
	}

}
