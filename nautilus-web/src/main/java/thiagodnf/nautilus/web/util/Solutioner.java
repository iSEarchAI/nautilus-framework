package thiagodnf.nautilus.web.util;

import java.util.ArrayList;
import java.util.List;

import thiagodnf.nautilus.core.model.Solution;
import thiagodnf.nautilus.core.util.Normalizer;

public class Solutioner {

	/**
	 * This method returns an array contains the minimum values 
	 * for each objective
	 * 
	 * @param solutions a list of solutions
	 * @return a list of minimum values for each objective
	 */
	public static double[] getMinimumValues(List<Solution> solutions) {

		int numberOfObjectives = solutions.get(0).getObjectives().size();

		double[] minimumValues = new double[numberOfObjectives];

		for (int i = 0; i < numberOfObjectives; i++) {

			double minValue = Double.MAX_VALUE;

			for (Solution solution : solutions) {

				if (solution.getObjectives().get(i) < minValue) {
					minValue = solution.getObjectives().get(i);
				}
			}

			minimumValues[i] = minValue;
		}

		return minimumValues;
	}
	
	/**
	 * This method returns an array contains the maximum values 
	 * for each objective
	 * 
	 * @param solutions a list of solutions
	 * @return a list of maximum values for each objective
	 */
	public static double[] getMaximumValues(List<Solution> solutions) {

		int numberOfObjectives = solutions.get(0).getObjectives().size();

		double[] maximumValues = new double[numberOfObjectives];

		for (int i = 0; i < numberOfObjectives; i++) {

			double maxValue = Double.NEGATIVE_INFINITY;
			
			for (Solution solution : solutions) {

				if (solution.getObjectives().get(i) > maxValue) {
					maxValue = solution.getObjectives().get(i);
				}
			}

			maximumValues[i] = maxValue;
		}

		return maximumValues;
	}
	
	/**
	 * This method returns a copy of a given list of solutions with normalized objective values.
	 * 
	 * @param solutions a list of solutions
	 * @return a copy of a given list of solutions with normalized objective values
	 */
	public static List<Solution> normalize(List<Solution> solutions){
		
		if (solutions.isEmpty()) {
			return new ArrayList<>(solutions.size());
		}

		if (solutions.size() == 1) {
			return new ArrayList<>(solutions);
		}
		
		double[] minValues = getMinimumValues(solutions);
		double[] maxValues = getMaximumValues(solutions);
		
		List<Solution> normalizedSolutions = new ArrayList<>(solutions.size());

		int numberOfObjectives = solutions.get(0).getObjectives().size();
		
		for (int i = 0; i < solutions.size(); i++) {
			
			Solution copy = solutions.get(i).copy();
			
			for (int j = 0; j < numberOfObjectives; j++) {
				copy.getObjectives().set(j, Normalizer.normalize(solutions.get(i).getObjectives().get(j), minValues[j], maxValues[j]));
			}
			
			normalizedSolutions.add(copy);
		}
		
		return normalizedSolutions;
	}
}
