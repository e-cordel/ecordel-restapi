package br.com.itsmemario.ecordel.cordel;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("cordel")
public class CordelController {
	
	private CordelService service;
	
	@Autowired
	CordelController(CordelService service) {
		super();
		this.service = service;
	}

	@GetMapping("{id}")
	//@Cacheable(value="")
	public Cordel getCordel(@PathVariable String id) {
		return new Cordel("Mário S.", "This is a cordel", "https://i.pinimg.com/originals/25/9d/47/259d47304bf26a4678cb039b8d8ce7f9.jpg","Description");
	}
	
	@GetMapping("all")
	public Collection<Cordel> getAll(){
		ArrayList<Cordel> arrayList = new ArrayList<>();
		arrayList.add(new Cordel("Mário S.", "This is a cordel", "https://i.pinimg.com/originals/25/9d/47/259d47304bf26a4678cb039b8d8ce7f9.jpg","Description"));
		arrayList.add(new Cordel("Mário S.", "This is a cordel", "https://i.pinimg.com/originals/25/9d/47/259d47304bf26a4678cb039b8d8ce7f9.jpg","Description"));
		arrayList.add(new Cordel("Mário S.", "This is a cordel", "https://i.pinimg.com/originals/25/9d/47/259d47304bf26a4678cb039b8d8ce7f9.jpg","Description"));
		return arrayList;
	}
	
	@GetMapping
	public Page<Cordel> getCordels(Pageable pageable){
		return service.getCordels(pageable);
	}
	
	@PostMapping
	public ResponseEntity<Cordel> create(@RequestBody @Valid Cordel cordel, UriComponentsBuilder uriBuilder){
		
		URI uri = uriBuilder.path("/cordel/{id}").buildAndExpand(cordel.getId()).toUri();
		
		return ResponseEntity.created(uri).body(cordel); 
	}

}
