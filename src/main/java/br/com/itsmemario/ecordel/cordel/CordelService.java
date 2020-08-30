package br.com.itsmemario.ecordel.cordel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CordelService {
	
	private CordelRepository repository;

	@Autowired
	public CordelService(CordelRepository repository) {
		super();
		this.repository = repository;
	}

	public Page<CordelView> getCordels(Pageable pageable) {
		return repository.findAllProjectedBy(pageable);
	}
	
	public Cordel save(Cordel cordel) {
		return repository.save(cordel);
	}

	public Page<CordelView> findByTags(List<String> tags, Pageable pageable){
		return repository.findByTags(tags, pageable);
	}


	public Optional<Cordel> findById(Long id) {
		return repository.findById(id);
	}
}
