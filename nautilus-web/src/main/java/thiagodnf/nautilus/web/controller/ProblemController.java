package thiagodnf.nautilus.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import thiagodnf.nautilus.web.model.UploadExecution;
import thiagodnf.nautilus.web.model.UploadInstanceFile;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/problem/{pluginId:.+}/{problemId:.+}")
public class ProblemController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProblemController.class);
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	@Autowired
	private ExecutionService executionService;
	
	@GetMapping("")
	public String view(Model model,
			@PathVariable("pluginId") String pluginId,
			@PathVariable("problemId") String problemId){
		
		LOGGER.info("Displaying '{}/{}'", pluginId, problemId);
		
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problem", pluginService.getProblemExtension(pluginId, problemId));
		model.addAttribute("instanceFiles", fileService.getInstanceFiles(pluginId, problemId));
		model.addAttribute("executions", executionService.findByPluginIdAndProblemKey(pluginId, problemId));

		return "problem";
	}
	
	@GetMapping("/upload/instance-file/")
	public String uploadInstanceFile(Model model, 
			@PathVariable("pluginId") String pluginId,
			@PathVariable("problemId") String problemId){
		
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problem", pluginService.getProblemExtension(pluginId, problemId));
		model.addAttribute("uploadInstanceFile", new UploadInstanceFile());
			
		return "upload-instance-file";
	}
	
	@GetMapping("/upload/execution/")
	public String uploadExecution(Model model, 
			@PathVariable("pluginId") String pluginId,
			@PathVariable("problemId") String problemId){
		
		model.addAttribute("uploadExecution", new UploadExecution());
		model.addAttribute("plugin", pluginService.getPluginWrapper(pluginId));
		model.addAttribute("problem", pluginService.getProblemExtension(pluginId, problemId));
			
		return "upload-execution";
	}
}
