package thiagodnf.nautilus.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaii.NSGAIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.IntegerSBXCrossover;
import org.uma.jmetal.operator.impl.mutation.IntegerPolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.IntegerSolution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.comparator.RankingAndCrowdingDistanceComparator;

import thiagodnf.nautilus.plugin.mip.problem.MinimumIntegerProblem;
import thiagodnf.nautilus.web.model.Parameters;
import thiagodnf.nautilus.web.model.Population;
import thiagodnf.nautilus.web.service.PopulationService;
import thiagodnf.nautilus.web.util.Converter;

@Controller
public class OptimizeController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OptimizeController.class);
	
	@Autowired
	private PopulationService parentoFrontService;
	
	//@Autowired
	//private WebSocketEventConfiguration event;
	
	@MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(@Valid Parameters parameters) throws Exception {
        
		Thread.sleep(1000); // simulated delay
		
		System.out.println(parameters);
		
        return "oi";
    }
	
	@PostMapping("/optimize")
	public String optimize(@Valid Parameters parameters) {
		System.out.println("oi");
		
		
		return "result";
	}
	
	
//	@PostMapping("/optimize")
//	public String optimize(@Valid Parameters parameters) {
//		System.out.println("oi");
//		
//		
//		return "result";
//	}

	//@GetMapping("/execute")
	public String index(Model model) {
		
		System.out.println("oi");
		
		Problem<IntegerSolution> problem = new MinimumIntegerProblem(10);
		
		double crossoverProbability = 0.9 ;
	    double crossoverDistributionIndex = 20.0 ;
	    CrossoverOperator<IntegerSolution> crossover = new IntegerSBXCrossover(crossoverProbability, crossoverDistributionIndex) ;

	    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
	    double mutationDistributionIndex = 20.0 ;
	    MutationOperator<IntegerSolution> mutation = new IntegerPolynomialMutation(mutationProbability, mutationDistributionIndex) ;
		
	    SelectionOperator<List<IntegerSolution>, IntegerSolution> selection = new BinaryTournamentSelection<IntegerSolution>(
		        new RankingAndCrowdingDistanceComparator<IntegerSolution>());
		
	    Algorithm<List<IntegerSolution>> algorithm = new NSGAIIBuilder<IntegerSolution>(problem, crossover, mutation)
		        .setSelectionOperator(selection)
		        .setMaxEvaluations(25000)
		        .setPopulationSize(100)
		        .build() ;
		
		AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
		        .execute() ;
		
		Population population = new Population();

		population.setSolutions(Converter.toSolutions(algorithm.getResult()));
		population.setExecutionTime(algorithmRunner.getComputingTime());

		population = parentoFrontService.save(population);
		
		System.out.println(population.getId());
				
		//System.out.println(population);
		
		return "index";
	}
}
