package br.com.itsmemario.ecordel.cordel;

import java.net.URI;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@CrossOrigin
@RestController
@RequestMapping("cordels")
public class CordelController {
	
	private CordelService service;
	
	@Autowired
	CordelController(CordelService service) {
		this.service = service;
	}

	@GetMapping("{id}")
	public ResponseEntity<Cordel> getCordel(@PathVariable Long id) {
		Optional<Cordel> cordel = service.findById(id);
		if(cordel.isPresent()){
			return ResponseEntity.ok(cordel.get());
		}else {
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping
	public Page<CordelSummary> getCordels(Pageable pageable){
		return service.getCordels(pageable);
	}
	
	@PostMapping
	public ResponseEntity<Cordel> create(@RequestBody @Valid Cordel cordel, UriComponentsBuilder uriBuilder){
		service.save(cordel);
		URI uri = uriBuilder.path("/cordel/{id}").buildAndExpand(cordel.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@PutMapping("{id}")
	public ResponseEntity<Cordel> update(@RequestBody @Valid Cordel cordel, @PathVariable Long id){
		Optional<Cordel> existingCordel = service.findById(id);
		if(!existingCordel.isPresent()){
			return ResponseEntity.notFound().build();
		}
		cordel.setId(id);
		Cordel newCordel = service.save(cordel);
		return ResponseEntity.ok(newCordel);
	}

}
