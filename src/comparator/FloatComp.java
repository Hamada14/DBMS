package comparator;

import java.util.Comparator;

public class FloatComp extends DataComparator implements Comparator<Float> {

	private static FloatComp floatComparator;

	private FloatComp() {
		super();
	}

	public static FloatComp getInstance() {
		if (floatComparator == null) {
			floatComparator = new FloatComp();
		}
		return floatComparator;
	}

	public boolean compareFloat(String o1, String o2, char operator) {
		int result = compare(Float.parseFloat(o1), Float.parseFloat(o2));
		return checkOperationValid(result, operator);
	}

	@Override
	public int compare(Float o1, Float o2) {
		return o1.compareTo(o2);
	}
}
