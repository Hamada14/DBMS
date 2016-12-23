package comparator;

public class DataComparator {

	public DataComparator(){
		
	}
	
	protected boolean checkOperationValid(int compareValue, char operator) {
		if ((operator == '>' && compareValue > 0) || (operator == '<' && compareValue < 0)
				|| (operator == '=' && compareValue == 0)) {
			return true;
		}
		return false;
	}
}
