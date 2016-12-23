package data;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateData extends Data {

	private Date value;
	private static String validFormats[] = { "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd" };

	public DateData(String value) throws InvalidDataTypeException {
		super(value);
	}

	@Override
	protected void setValue(String value) throws InvalidDataTypeException {
		if (checkFormat(value, 0) == null) {
			throw new InvalidDataTypeException();
		}
		this.value = checkFormat(value, 0);
	}

	@Override
	public String toString() {
		return value.toString();
	}

	public static boolean assertDate(String dateStr) {
		if (invalidDateQuote(dateStr)) {
			return false;
		}
		dateStr = dateStr.substring(1, dateStr.length() - 1);
		if (checkFormat(dateStr, 0) == null) {
			return false;
		}
		return true;
	}

	public static Date checkFormat(String dateStr, int formatIdx) {
		if (formatIdx == validFormats.length) {
			return yearFormat(dateStr);
		}
		try {
			DateFormat df = new SimpleDateFormat(validFormats[formatIdx]);
			df.setLenient(false);
			return new Date(df.parse(dateStr).getTime());
		} catch (ParseException e1) {
			return checkFormat(dateStr, formatIdx + 1);
		}
	}

	private static Date yearFormat(String date) {
		date = date.trim();
		if (date.matches("[0-9]+") && (date.length() == 2 || date.length() == 4)) {
			try {
				DateFormat df = new SimpleDateFormat("Y");
				df.setLenient(false);
				return new Date(df.parse(date).getTime());
			} catch (ParseException e1) {
				return null;
			}
		}
		return null;
	}

	private static boolean invalidDateQuote(String dateStr) {
		char dateBegin = dateStr.charAt(0);
		char dateEnd = dateStr.charAt(dateStr.length() - 1);
		if (dateBegin != dateEnd || (dateBegin != '\'')) {
			return true;
		}
		return false;
	}
}
