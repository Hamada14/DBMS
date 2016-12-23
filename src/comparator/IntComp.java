package comparator;

import java.util.Comparator;

public class IntComp extends DataComparator implements Comparator<Integer> {

	private static IntComp intComparator;

	private IntComp() {
		super();
	}

	public static IntComp getInstance() {
		if (intComparator == null) {
			intComparator = new IntComp();
		}
		return intComparator;
	}

	public boolean compareInt(String o1, String o2, char operator) {
		int result = compare(Integer.parseInt(o1), Integer.parseInt(o2));
		return checkOperationValid(result, operator);
	}

	@Override
	public int compare(Integer o1, Integer o2) {
		return o1 - o2;
	}
}
