package studio.mythsart.hwj.calculator;

import java.util.ArrayList;
import java.util.List;

public class Scientific5Executor implements CalculatorExecutor {

	private String tempExpression;
	private static final char[] ALLOW_OPR_SAFE = new char[] { '*', '/', '%' };
	private static final char[] ALLOW_OPR = new char[] { '+', '-', '*', '/', '(', ')', '%' };

	public Scientific5Executor(String _tempExpression) {
		this.tempExpression = _tempExpression;
		return;
	}

	public void setExpression(String _tempExpression) throws Exception {
		if (_tempExpression == null || _tempExpression.equals(""))
			throw new Exception("Not allow null expression!");
		this.tempExpression = _tempExpression;
	}

	@Override
	public boolean preExecute() {
		return true;
	}

	@Override
	public double execute() throws Exception {
		if (!this.preExecute())
			throw new Exception("Expression error!");
		/* push */
		final List<String> stack = new ArrayList<String>();
		final String tempExpression_ = this.tempExpression;
		String tempNum = new String("");
		for (int i = 0; i < this.tempExpression.length(); i++) {
			/* check is opr */
			boolean oprFlag = false;
			for (int j = 0; j < Scientific5Executor.ALLOW_OPR.length; j++) {
				if (this.tempExpression.toCharArray()[i] == Scientific5Executor.ALLOW_OPR[j]) {
					if (i == 0) {

					} else
						stack.add(tempNum);
					tempNum = "";
					stack.add(this.tempExpression.toCharArray()[i] + "");
					oprFlag = true;
					break;
				}
			}
			if (oprFlag) {
				continue;
			}
			/* join */
			tempNum += this.tempExpression.toCharArray()[i];
			/* check is last */
			if (i == this.tempExpression.length() - 1) {
				stack.add(tempNum);
				tempNum = "";
			}
		}
		/* trim space (bug) */
		for (int i = 0; i < stack.size(); i++) {
			if (stack.get(i) == "") {
				stack.remove(i);
			}
		}
		/* trim parentheses (qnmd fake stack) */
		final List<String> _tempExpression = new ArrayList<String>();
		int firstIndex = 0, lastIndex = 0;
		boolean indexFlag = false;
		int mix = 0;
		for (int i = 0; i < stack.size(); i++) {
			if (indexFlag == true && stack.get(i).equals("(")) {
				mix++;
			} else if (stack.get(i).equals("(")) {
				indexFlag = true;
				firstIndex = i;
				continue;
			}
			if (indexFlag && stack.get(i).equals(")")) {
				if (mix != 0) {
					mix--;
					continue;
				}
				lastIndex = i;
				indexFlag = false;
				String _tempExpressionStr = "";
				for (int j = firstIndex + 1; j < lastIndex; j++) {
					_tempExpressionStr += stack.get(j);
				}
				// remove and calc
				for (int j = 0; j < lastIndex - firstIndex; j++) {
					stack.remove(firstIndex);
				}
				// call self to calc parentheses
				stack.set(firstIndex, Double.toString(new Scientific5Executor(_tempExpressionStr).execute()));
				i = 0;
				// add expression
				_tempExpression.add(_tempExpressionStr);
			}
		}
		/* fix positives & negatives */
		int tempCount = stack.size();
		while (true) {
			boolean execFlag = false;
			// positives
			for (int i = 0; i < tempCount;) {
				if (stack.get(i).equals("+")) {
					if (stack.get(i + 1).equals("-") || stack.get(i + 1).equals("+")) {
						stack.remove(i);
						i = 0;
						tempCount = stack.size();
						execFlag = true;
						continue;
					}
				}
				i++;
			}
			// negatives
			for (int i = 0; i < tempCount;) {
				if (stack.get(i).equals("-")) {
					if (stack.get(i + 1).equals("-")) {
						stack.set(i, "+");
						stack.remove(i + 1);
						i = 0;
						tempCount = stack.size();
						execFlag = true;
						continue;
					} else if (stack.get(i + 1).equals("+")) {
						stack.remove(i + 1);
						i = 0;
						tempCount = stack.size();
						execFlag = true;
						continue;
					}
				}
				i++;
			}
			if (execFlag == false) {
				break;
			}
		}

		// Synthesis
		for (int i = 0; i < tempCount; i++) {
			if (i == 0 && (stack.get(i).equals("-") || stack.get(i).equals("+"))) {
				stack.set(i + 1, stack.get(i) + stack.get(i + 1));
				stack.remove(i);
				i = 0;
				tempCount = stack.size();
			} else if (stack.get(i).equals("-") || stack.get(i).equals("+")) {
				boolean exFlag = false;
				for (int j = 0; j < Scientific5Executor.ALLOW_OPR_SAFE.length; j++) {
					if (stack.get(i - 1).equals(new String(new char[] { ALLOW_OPR_SAFE[j] }))) {
						exFlag = true;
						break;
					}
				}
				if (exFlag) {
					stack.set(i + 1, stack.get(i) + stack.get(i + 1));
					stack.remove(i);
					i = 0;
					tempCount = stack.size();
				}
			}
		}
		/* calclate */
		// * / %
		tempCount = stack.size();
		for (int i = 0; i < tempCount; i++) {
			for (int j = 0; j < Scientific5Executor.ALLOW_OPR_SAFE.length; j++) {
				if (stack.get(i).equals(new String(new char[] { ALLOW_OPR_SAFE[j] }))) {
					double left = Double.parseDouble(stack.get(i - 1)), right = Double.parseDouble(stack.get(i + 1));
					String res = Double.toString(SimpleAdapter.calc(left, right, stack.get(i)));
					stack.set(i, res);
					stack.remove(i + 1);
					stack.remove(i - 1);
					i = 0;
					tempCount = stack.size();
				}
			}
		}
		// + -
		for (int i = 0; i < tempCount; i++) {
			if (stack.get(i).equals(new String(new char[] { '-' }))
					|| stack.get(i).equals(new String(new char[] { '+' }))) {
				double left = Double.parseDouble(stack.get(i - 1)), right = Double.parseDouble(stack.get(i + 1));
				String res = Double.toString(SimpleAdapter.calc(left, right, stack.get(i)));
				stack.set(i, res);
				stack.remove(i + 1);
				stack.remove(i - 1);
				i = 0;
				tempCount = stack.size();
			}
		}
		// final
		this.tempExpression = tempExpression_;
		if(stack.size() != 1)throw new Exception("Error!");
		return Double.parseDouble(stack.get(0));
	}

}
