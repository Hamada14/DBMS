package evaluator;

import comparator.DateComp;
import comparator.FloatComp;
import comparator.IntComp;
import comparator.StringComp;

import data.DataValidator;

public class BooleanExpression {

	private String expression;

	public BooleanExpression(String expression) {
		this.expression = expression;
	}

	public int getValue() {
		expression = expression.trim();
		int compPos = getCompPos(expression);
		if (compPos == -1) {
			return -1;
		}
		String firstVal = expression.substring(0, compPos).trim();
		String secondVal = expression.substring(compPos + 1).trim();
		int type1 = DataValidator.getInstance().getType(firstVal);
		int type2 = DataValidator.getInstance().getType(secondVal);
		int compareType = getCompareType(type1, type2);
		if (compareType == -1)
			return -1;
		return booleanToInt(calculateBoolean(firstVal, secondVal, type1, expression.charAt(compPos)));
	}

	private int booleanToInt(boolean boolVal) {
		if (boolVal) {
			return 1;
		}
		return 0;
	}

	private boolean isComparator(char curChar) {
		if (curChar == '<' || curChar == '>' || curChar == '=') {
			return true;
		}
		return false;
	}

	private int getCompareType(int type1, int type2) {
		if (Math.min(type1, type2) == DataValidator.INT_TYPE && Math.max(type1, type2) == DataValidator.FLOAT_TYPE) {
			return DataValidator.FLOAT_TYPE;
		} else if (type1 == type2) {
			return type1;
		}
		return -1;
	}

	private int getCompPos(String expression) {
		int singleQ = 0, doubleQ = 0;
		int pos = -1;
		for (int idx = 0; idx < expression.length(); idx++) {
			char curChar = expression.charAt(idx);
			if (curChar == '\'') {
				singleQ++;
			} else if (curChar == '\"') {
				doubleQ++;
			} else if (isComparator(curChar) && (singleQ % 2 == 0) && (doubleQ % 2 == 0)) {
				if (pos == -1) {
					pos = idx;
				} else {
					return -1;
				}
			}
		}
		return pos;
	}

	private boolean calculateBoolean(String firstVal, String secondVal, int type, char operator) {
		switch (type) {
		case DataValidator.DATE_TYPE:
			return compareDate(firstVal, secondVal, operator);
		case DataValidator.INT_TYPE:
			return compareInt(firstVal, secondVal, operator);
		case DataValidator.STRING_TYPE:
			firstVal = firstVal.substring(1, firstVal.length() - 1);
			secondVal = secondVal.substring(1, secondVal.length() - 1);
			return compareString(firstVal, secondVal, operator);
		case DataValidator.FLOAT_TYPE:
			return compareFloat(firstVal, secondVal, operator);
		}
		return false;
	}

	private boolean compareString(String firstVal, String secondVal, char compChar) {
		return StringComp.getInstance().compareString(firstVal, secondVal, compChar);
	}

	private boolean compareInt(String firstVal, String secondVal, char compChar) {
		return IntComp.getInstance().compareInt(firstVal, secondVal, compChar);
	}

	private boolean compareFloat(String firstVal, String secondVal, char compChar) {
		return FloatComp.getInstance().compareFloat(firstVal, secondVal, compChar);
	}

	private boolean compareDate(String firstVal, String secondVal, char compChar) {
		return DateComp.getInstance().compareDate(firstVal, secondVal, compChar);
	}
}
