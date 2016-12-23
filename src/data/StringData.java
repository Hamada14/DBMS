package data;

public class StringData extends Data {

	private String value;

	public StringData(String value) throws InvalidDataTypeException {
		super(value);
	}

	@Override
	protected void setValue(String value) throws InvalidDataTypeException {
		try {
			this.value = value;
		} catch (Exception e) {
			throw new InvalidDataTypeException();
		}
	}

	@Override
	public String toString() {
		return value.toString();
	}

	public static boolean assertString(String substring) {
		char firstInd = substring.charAt(0);
		char lastInd = substring.charAt(substring.length() - 1);
		if (firstInd == lastInd && (firstInd == '\'' || firstInd == '\"')) {
			return true;
		}
		return false;
	}
}
