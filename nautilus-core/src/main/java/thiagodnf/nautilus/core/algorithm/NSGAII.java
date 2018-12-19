package thiagodnf.nautilus.core.algorithm;

import java.util.List;

import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.comparator.DominanceComparator;
import org.uma.jmetal.util.evaluator.impl.SequentialSolutionListEvaluator;

import thiagodnf.nautilus.core.listener.AlgorithmListener;
import thiagodnf.nautilus.core.listener.OnProgressListener;

@SuppressWarnings("unchecked")
public class NSGAII<S extends Solution<?>> extends org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAII<S> implements AlgorithmListener{

	private static final long serialVersionUID = -3996332429840079517L;
	
	private OnProgressListener onProgressListener;
	
	protected List<S> initialPopulation;
	
	public NSGAII(Builder builder) {
		super(builder.getProblem(), 
				builder.getMaxEvaluations(), 
				builder.getPopulationSize(), 
				builder.getCrossover(), 
				builder.getMutation(), 
				builder.getSelection(),
			new DominanceComparator<>(), 
			new SequentialSolutionListEvaluator<S>());
		
		this.initialPopulation = (List<S>) builder.getInitialPopulation();
	}
	
	@Override
	protected List<S> createInitialPopulation() {

		if (this.initialPopulation == null) {
			return super.createInitialPopulation();
		}

		while (initialPopulation.size() != maxPopulationSize) {
			((List<S>) this.initialPopulation).add(problem.createSolution());
		}

		return (List<S>) initialPopulation;
	}
	
	@Override
	protected void updateProgress() {
		
		double progress = (((double) evaluations) / ((double) maxEvaluations)) * 100.0;
		
		if (onProgressListener != null) {
			onProgressListener.onProgress(progress);
		}
		
		super.updateProgress();
	}

	public void setOnProgressListener(OnProgressListener onProgressListener) {
		this.onProgressListener = onProgressListener;
	}
}
