package thiagodnf.nautilus.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	
	@GetMapping("/")
	public String index(Model model) {
		
		System.out.println("oi");
		
		return "index";
	}
}
