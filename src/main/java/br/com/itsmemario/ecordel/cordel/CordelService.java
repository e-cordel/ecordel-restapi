package br.com.itsmemario.ecordel.cordel;

import br.com.itsmemario.ecordel.xilogravura.Xilogravura;
import br.com.itsmemario.ecordel.xilogravura.XilogravuraService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
public class CordelService {

	private final Logger logger = LoggerFactory.getLogger(CordelService.class);

	private CordelRepository repository;
	private XilogravuraService xilogravuraService;

	@Autowired
	public CordelService(CordelRepository repository, XilogravuraService xilogravuraService) {
		super();
		this.repository = repository;
		this.xilogravuraService = xilogravuraService;
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

	public Page<CordelView> findByTitle(String title, Pageable pageable) {
		return repository.findByTitleLike(String.format("%%%s%%",title), pageable);
	}

	public Cordel updateXilogravura(Long cordelId, Xilogravura xilogravura, MultipartFile file) {
		Optional<Cordel> byId = findById(cordelId);

		if(byId.isPresent()) {
			Cordel cordel = byId.get();
			Xilogravura xilogravuraWithFile = xilogravuraService.createXilogravuraWithFile(xilogravura, file);
			cordel.setXilogravura(xilogravuraWithFile);
			return save(cordel);
		}else{
			throw new CordelNotFoundException();
		}
	}
}
