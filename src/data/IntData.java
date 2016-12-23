package data;

public class IntData extends Data {

	private Integer value;

	public IntData(String value) throws InvalidDataTypeException {
		super(value);
	}

	@Override
	protected void setValue(String value) throws InvalidDataTypeException {
		try {
			this.value = new Integer(value);
		} catch (Exception e) {
			throw new InvalidDataTypeException();
		}
	}

	@Override
	public String toString() {
		return value.toString();
	}

	public static boolean assertInt(String substring) {
		try {
			new Integer(substring);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
