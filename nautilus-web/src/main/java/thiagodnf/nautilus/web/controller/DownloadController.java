package thiagodnf.nautilus.web.controller;

import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.List;

import org.pf4j.PluginWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import thiagodnf.nautilus.core.model.GenericSolution;
import thiagodnf.nautilus.web.model.Execution;
import thiagodnf.nautilus.web.service.ExecutionService;
import thiagodnf.nautilus.web.service.FileService;
import thiagodnf.nautilus.web.service.PluginService;

@Controller
@RequestMapping("/download")
public class DownloadController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DownloadController.class);
	
	@Autowired
	private ExecutionService executionService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("/execution/{executionId:.+}/json")
	@ResponseBody
	public ResponseEntity<Resource> downloadExecutionAsJsonFile(
			@PathVariable("executionId") String executionId) {

		LOGGER.info("Downloading as json file the execution id " + executionId);

		Execution execution = executionService.findById(executionId);

		String content = execution.toString();

		Resource file = new ByteArrayResource(content.getBytes());

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + executionId + ".json\"")
				.header(HttpHeaders.CONTENT_TYPE, "application/json")
				.header(HttpHeaders.CONTENT_ENCODING, "UTF-8")
				.body(file);
	}
	
	@GetMapping("/execution/{executionId:.+}/fun")
	@ResponseBody
	public ResponseEntity<Resource> downloadExecutionAsFunFile(
			@PathVariable("executionId") String executionId) {

		LOGGER.info("Downloading as fun file the execution id " + executionId);

		Execution execution = executionService.findById(executionId);
		
		StringBuffer buffer = new StringBuffer();
		
//		List<? extends Solution<?>> solutions = execution.getSolutions();
		List<GenericSolution> solutions = execution.getSolutions();
		
		//solutions = new ByObjectivesDuplicatesRemover().execute(solutions);
		
		for (GenericSolution s : solutions) {

			for (int i = 0; i < s.getNumberOfObjectives(); i++) {
				buffer.append(s.getObjective(i));

				if (i + 1 != s.getNumberOfObjectives()) {
					buffer.append(" ");
				}
			}

			buffer.append("\n");
		}
	   
		String content = buffer.toString();

		Resource file = new ByteArrayResource(content.getBytes());

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + executionId + ".fun\"")
				.header(HttpHeaders.CONTENT_TYPE, "text/fun")
				.header(HttpHeaders.CONTENT_ENCODING, "UTF-8")
				.body(file);
	}
	
	@GetMapping("/execution/{executionId:.+}/var")
	@ResponseBody
	public ResponseEntity<Resource> downloadExecutionAsVarFile(
			@PathVariable("executionId") String executionId) {

		LOGGER.info("Downloading as json file the execution id " + executionId);

		Execution execution = executionService.findById(executionId);
		
		StringBuffer buffer = new StringBuffer();
		
		List<GenericSolution> solutions = execution.getSolutions();
		
		//solutions = new ByObjectivesDuplicatesRemover().execute(solutions);
		
		for (GenericSolution s : solutions) {

			for (int i = 0; i < s.getNumberOfVariables(); i++) {
				
				//buffer.append(s.getVariableValue(i));

				if (i + 1 != s.getNumberOfVariables()) {
					buffer.append(";");
				}
			}

			buffer.append("\n");
		}
	   
		String content = buffer.toString();

		Resource file = new ByteArrayResource(content.getBytes());

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + executionId + ".var\"")
				.header(HttpHeaders.CONTENT_TYPE, "text/var")
				.header(HttpHeaders.CONTENT_ENCODING, "UTF-8")
				.body(file);
	}
	
	@GetMapping("/instance-file/{pluginId:.+}/{problemId:.+}/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> downloadInstanceFile(
			@PathVariable("pluginId") String pluginId,
			@PathVariable("problemId") String problemId,
			@PathVariable("filename") String filename) throws IOException {

		LOGGER.info("Downloading the instance file " + filename);

		Resource file = fileService.getInstanceFileAsResource(pluginId, problemId, filename);
		
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		
		String contentType = fileNameMap.getContentTypeFor(file.getFile().getName());
		  
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFilename())
				.header(HttpHeaders.CONTENT_TYPE, contentType)
				.body(file);
	}
	
	@GetMapping("/plugin/{pluginId:.+}")
	@ResponseBody
	public ResponseEntity<Resource> downloadPlugin(
			@PathVariable("pluginId") String pluginId){

		LOGGER.info("Downloading the plugin {}", pluginId);
		
		PluginWrapper plugin = pluginService.getPluginWrapper(pluginId);

		Path path = plugin.getPluginPath();
		
		Resource file = fileService.loadAsResource(path);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFilename())
				.header(HttpHeaders.CONTENT_TYPE, "application/java-archive")
				.body(file);
	}
}
