package thiagodnf.nautilus.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import thiagodnf.nautilus.web.service.PluginService;

@Controller
public class IndexController {
	
	@Autowired
	private PluginService pluginService;
	
	@GetMapping("/")
	public String index(Model model) {
		
		model.addAttribute("plugins", pluginService.getAllPlugins());
		
		return "index";
	}
}
