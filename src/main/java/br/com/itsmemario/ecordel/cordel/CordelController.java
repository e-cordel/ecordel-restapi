package br.com.itsmemario.ecordel.cordel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("cordels")
public class CordelController {
	
	private CordelService service;
	private final Logger logger = LoggerFactory.getLogger(CordelController.class);

	@Autowired
	CordelController(CordelService service) {
		this.service = service;
	}

	@GetMapping("{id}")
	public ResponseEntity<Cordel> getCordel(@PathVariable Long id) {
		logger.info("request received get cordel by id: {}", id);

		Optional<Cordel> cordel = service.findById(id);
		if(cordel.isPresent()){
			return ResponseEntity.ok(cordel.get());
		}else {
			logger.info("cordel with id {} not fond",id);
			return ResponseEntity.notFound().build();
		}
	}
	
	@GetMapping
	public Page<CordelView> getCordels(@RequestParam(required = false) List<String> tags, Pageable pageable){
		logger.info("request received get cordels");
		if(tags != null && !tags.isEmpty()){
			return service.findByTags(tags, pageable);
		}
		return service.getCordels(pageable);
	}
	
	@PostMapping
	public ResponseEntity<Cordel> create(@RequestBody @Valid Cordel cordel, UriComponentsBuilder uriBuilder){
		logger.info("request received for create cordel: {}", cordel);
		service.save(cordel);
		URI uri = uriBuilder.path("/cordel/{id}").buildAndExpand(cordel.getId()).toUri();
		logger.info("new cordel Location header: {}", uri.getPath());
		return ResponseEntity.created(uri).build();
	}

	@PutMapping("{id}")
	public ResponseEntity<Cordel> update(@RequestBody @Valid Cordel cordel, @PathVariable Long id){
		logger.info("request received update cordel with id: {}", id);
		Optional<Cordel> existingCordel = service.findById(id);
		if(!existingCordel.isPresent()){
			return ResponseEntity.notFound().build();
		}
		cordel.setId(id);
		Cordel newCordel = service.save(cordel);
		return ResponseEntity.ok(newCordel);
	}

}
