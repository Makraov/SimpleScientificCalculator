package studio.mythsart.hwj.calculator;

public interface CalculatorExecutor {
	public boolean preExecute();

	public double execute() throws Exception;
}
