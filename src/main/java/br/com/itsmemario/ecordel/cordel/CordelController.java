package br.com.itsmemario.ecordel.cordel;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("cordel")
public class CordelController {
	
	@GetMapping("{id}")
	public Cordel getCordel(@PathVariable String id) {
		return new Cordel("Mário S.", "This is a cordel", "");
	}

}
