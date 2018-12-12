package calculator;

public class SimpleAdapter {
	private SimpleAdapter() {
		return;
	}

	public static double calc(double arg0, double arg1, String opr) throws Exception {
		switch (opr) {
		case "+": {
			return arg0 + arg1;
		}
		case "-": {
			return arg0 - arg1;
		}
		case "*": {
			return arg0 * arg1;
		}
		case "/": {
			if (arg1 == 0)
				throw new Exception("Not allow denominator zero!");
			return arg0 / arg1;
		}
		case "%": {
			return arg0 % arg1;
		}
		default: {
			throw new Exception("Operator not found!");
		}
		}
	}
}
