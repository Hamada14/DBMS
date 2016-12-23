package data;

public class DataValidator {

	private static final String[] dataTypes = { "int", "varchar", "float", "date" };
	private static final String[] testValue = { "0", "\'m\'", "4.32", "\'1995-06-07\'" };
	public static final int UNSPECIFIED = -1, INT_TYPE = 0, STRING_TYPE = 1, FLOAT_TYPE = 2, DATE_TYPE = 3;
	public static final int TYPE_COUNT = 4;

	private static DataValidator dv;

	private DataValidator() {

	}

	public static DataValidator getInstance() {
		if (dv == null)
			dv = new DataValidator();
		return dv;
	}

	public String getDataType(String type) {
		for (int idx = 0; idx < TYPE_COUNT; idx++) {
			if (dataTypes[idx].equals(type.toLowerCase())) {
				return dataTypes[idx];
			}
		}
		return null;
	}

	public String getTestValue(String type) {
		for (int idx = 0; idx < TYPE_COUNT; idx++) {
			if (dataTypes[idx].equals(type.toLowerCase())) {
				return testValue[idx];
			}
		}
		return null;
	}

	public boolean assertType(String type, String value) {
		switch (type) {
		case "int":
			return IntData.assertInt(value);
		case "float":
			return FloatData.assertFloat(value);
		case "date":
			return DateData.assertDate(value);
		case "varchar":
			return StringData.assertString(value);
		default:
			return false;
		}
	}

	public int getType(String substring) {
		if (substring == null || substring.length() == 0) {
			return UNSPECIFIED;
		}
		if (IntData.assertInt(substring)) {
			return INT_TYPE;
		} else if (FloatData.assertFloat(substring)) {
			return FLOAT_TYPE;
		} else if (DateData.assertDate(substring)) {
			return DATE_TYPE;
		} else if (StringData.assertString(substring)) {
			return STRING_TYPE;
		}
		return UNSPECIFIED;
	}
}
