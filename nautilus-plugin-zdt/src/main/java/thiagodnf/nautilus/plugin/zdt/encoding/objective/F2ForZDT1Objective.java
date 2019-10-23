package thiagodnf.nautilus.plugin.zdt.encoding.objective;

import org.nautilus.core.model.Instance;
import org.nautilus.core.objective.AbstractObjective;
import org.uma.jmetal.solution.DoubleSolution;
import org.uma.jmetal.solution.Solution;

public class F2ForZDT1Objective extends AbstractObjective {

	@Override
	public double calculate(Instance data, Solution<?> sol) {

		DoubleSolution solution = (DoubleSolution) sol;

		double f0 = solution.getVariableValue(0);

		double g = this.evalG(solution);
		double h = this.evalH(f0, g);

		return h * g;
	}
	
	/**
	 * Returns the value of the ZDT1 function G.
	 *
	 * @param solution Solution
	 */
	private double evalG(DoubleSolution solution) {
		double g = 0.0;
		for (int i = 1; i < solution.getNumberOfVariables(); i++) {
			g += solution.getVariableValue(i);
		}
		double constant = 9.0 / (solution.getNumberOfVariables() - 1);
		g = constant * g;
		g = g + 1.0;
		return g;
	}

	/**
	 * Returns the value of the ZDT1 function H.
	 *
	 * @param f First argument of the function H.
	 * @param g Second argument of the function H.
	 */
	public double evalH(double f, double g) {
		double h;
		h = 1.0 - Math.sqrt(f / g);
		return h;
	}

	@Override
	public String getName() {
		return "F2";
	}

	@Override
	public String getGroupName() {
		return "General";
	}
	
	@Override
	public boolean isDisabled() {
		return true;
	}
}
