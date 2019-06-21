package thiagodnf.nautilus.web.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import thiagodnf.nautilus.core.util.Converter;
import thiagodnf.nautilus.plugin.extension.ProblemExtension;
import thiagodnf.nautilus.web.dto.ParametersDTO;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.model.User;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.PluginService;
import thiagodnf.nautilus.web.service.SecurityService;
import thiagodnf.nautilus.web.util.Messages;
import thiagodnf.nautilus.web.util.Redirect;

@Controller
@RequestMapping("/optimize/{problemId:.+}/{instance:.+}")
public class OptimizeController {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(OptimizeController.class);
    
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
    private ExecutionService executionService;
	
	@Autowired
    private List<Execution> pendingExecutions;
	
	@Autowired
	private Redirect redirect;
	
	@GetMapping("")
	public String form( 
			@PathVariable String problemId, 
			@PathVariable String instance,
			Model model){
		
		ProblemExtension problem = pluginService.getProblemById(problemId);
		
		User user = securityService.getLoggedUser().getUser(); 
		
		model.addAttribute("problem", problem);
		model.addAttribute("instance", instance);
		model.addAttribute("algorithms", pluginService.getAlgorithms());
		model.addAttribute("crossovers", pluginService.getCrossovers());
		model.addAttribute("mutations", pluginService.getMutations());
		model.addAttribute("selections", pluginService.getSelections());
		model.addAttribute("parametersDTO", new ParametersDTO(user.getId(), problem.getId(), instance));
		
		return "optimize";
	}
	
    @PostMapping("/save")
    public String optimize(@Valid ParametersDTO parametersDTO, BindingResult bindingResult, RedirectAttributes ra,  Model model) {

        if (bindingResult.hasErrors()) {
            return form(parametersDTO.getProblemId(), parametersDTO.getInstance(), model);
        }
        
        LOGGER.debug("Saving {}", Converter.toJson(parametersDTO));
        
        User user = securityService.getLoggedUser().getUser();
        
        Execution execution = new Execution();

        execution.setUserId(user.getId());
        execution.setSolutions(null);
        execution.setAlgorithmId(parametersDTO.getAlgorithmId());
        execution.setProblemId(parametersDTO.getProblemId());
        execution.setInstance(parametersDTO.getInstance());
        execution.setPopulationSize(parametersDTO.getPopulationSize());
        execution.setMaxEvaluations(parametersDTO.getMaxEvaluations());
        execution.setSelectionId(parametersDTO.getSelectionId());
        execution.setCrossoverId(parametersDTO.getCrossoverId());
        execution.setCrossoverProbability(parametersDTO.getCrossoverProbability());
        execution.setCrossoverDistribution(parametersDTO.getCrossoverDistribution());
        execution.setMutationId(parametersDTO.getMutationId());
        execution.setMutationProbability(parametersDTO.getMutationProbability());
        execution.setMutationDistribution(parametersDTO.getMutationDistribution());
        execution.setReferencePoints(parametersDTO.getReferencePoints());
        execution.setEpsilon(parametersDTO.getEpsilon());
        execution.setObjectiveIds(parametersDTO.getObjectiveIds());
        
        execution = executionService.save(execution);
        
        pendingExecutions.add(execution);
        
        return redirect.to("/home").withSuccess(ra, Messages.EXECUTION_SCHEDULED_SUCCESS);
    }
}
