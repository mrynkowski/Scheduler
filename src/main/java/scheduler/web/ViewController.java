package scheduler.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {
	@RequestMapping({
		"/user/{\\w+}/{\\d+}",
		"/user/{\\w+}",
		"/user",
		"/login",
		"/signin",
	    "/"
	})
	public String index() {
	    return "forward:/index.html";
	}
}
