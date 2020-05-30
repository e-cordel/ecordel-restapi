package br.com.itsmemario.ecordel.cordel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CordelService {
	
	private CordelRepository repository;

	@Autowired
	public CordelService(CordelRepository repository) {
		super();
		this.repository = repository;
	}

	public Page<CordelSummary> getCordels(Pageable pageable) {
		return repository.findAllProjectedBy(pageable);
	}
	
	public Cordel save(Cordel cordel) {
		return repository.save(cordel);
	}


	public Optional<Cordel> findById(Long id) {
		return repository.findById(id);
	}
}
