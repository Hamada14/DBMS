package comparator;

import java.util.Comparator;
import comparator.DataComparator;

public class StringComp extends DataComparator implements Comparator<String> {

	private static StringComp stringComparator;

	private StringComp() {
		super();
	}

	public static StringComp getInstance() {
		if (stringComparator == null) {
			stringComparator = new StringComp();
		}
		return stringComparator;
	}

	public boolean compareString(String o1, String o2, char operator) {
		int result = compare(o1, o2);
		return checkOperationValid(result, operator);
	}

	@Override
	public int compare(String o1, String o2) {
		return (o1).compareTo(o2);
	}
}
