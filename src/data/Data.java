package data;

public abstract class Data {

	protected abstract void setValue(String value) throws InvalidDataTypeException;

	public Data(String value) throws InvalidDataTypeException {
		setValue(value);
	}
}
