package data;

public class FloatData extends Data {

	private Float value;

	public FloatData(String value) throws InvalidDataTypeException {
		super(value);
	}

	@Override
	protected void setValue(String value) throws InvalidDataTypeException {
		try {
			this.value = new Float(value);
		} catch (Exception e) {
			throw new InvalidDataTypeException();
		}
	}

	@Override
	public String toString() {
		return value.toString();
	}

	public static boolean assertFloat(String substring) {
		try {
			new Float(substring);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}
