package comparator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class DateComp extends DataComparator implements Comparator<Date> {

	private static DateComp dateComparator;

	private DateComp() {
		super();
	}

	public static DateComp getInstance() {
		if (dateComparator == null) {
			dateComparator = new DateComp();
		}
		return dateComparator;
	}

	public boolean compareDate(String o1, String o2, char operator) {
		try {
			DateFormat df = new SimpleDateFormat("yyyy-MM-ddd");
			o1 = o1.substring(1, o1.length() - 1);
			o2 = o2.substring(1, o2.length() - 1);
			Date date1 = df.parse(o1);
			Date date2 = df.parse(o2);
			int result = compare(date1, date2);
			return checkOperationValid(result, operator);
		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	@Override
	public int compare(Date o1, Date o2) {
		return o1.compareTo(o2);
	}
}
