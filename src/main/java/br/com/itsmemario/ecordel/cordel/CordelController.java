package br.com.itsmemario.ecordel.cordel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
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
	public Page<CordelView> getCordels(@RequestParam(required = false) String title,
									   Pageable pageable){
		logger.info("request received get cordels");
		if(title != null && !title.isEmpty()){
			return service.findByTitle(title, pageable);
		}
		return service.getCordels(pageable);
	}
	
	@PostMapping
	public ResponseEntity<Cordel> create(@RequestBody @Valid Cordel cordel, UriComponentsBuilder uriBuilder){
		logger.info("request received for create cordel: {}", cordel);
		service.save(cordel);
		URI uri = uriBuilder.path("/cordels/{id}").buildAndExpand(cordel.getId()).toUri();
		logger.info("new cordel Location header: {}", uri.getPath());
		return ResponseEntity.created(uri).build();
	}

	@PutMapping("{id}/xilogravura")
	public ResponseEntity<Cordel> putImage(@PathVariable Long id, @RequestParam("file") MultipartFile file){
		logger.info("request received, update xilogravura for cordel: {}", id);
		Optional<Cordel> byId = service.findById(id);
		if(byId.isPresent()){
			Cordel cordel =byId.get();
			String xilogravura = service.updateXilogravura(file, cordel);
			Cordel response = new Cordel();
			response.setId(id);
			response.setXilogravura(xilogravura);
			return ResponseEntity.ok(response);
		}else{
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping(value = "{id}/xilogravura", produces = MediaType.IMAGE_JPEG_VALUE)
	public byte[] getImage(@PathVariable Long id){
		return service.getImage(Cordel.of(id));
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
