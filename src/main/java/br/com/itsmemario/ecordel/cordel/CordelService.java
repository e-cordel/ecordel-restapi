package br.com.itsmemario.ecordel.cordel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class CordelService {
	
	private CordelRepository repository;
	private final Logger logger = LoggerFactory.getLogger(CordelService.class);
	private final String XILOGRAVURA_NAME_PATTERN = "/cordels/%s/xilogravura";

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

	public String updateXilogravura(MultipartFile file, Cordel cordel) {
		String fileName = cordel.getId() + ".jpg";
		String xilogravura = String.format(XILOGRAVURA_NAME_PATTERN, cordel.getId());

		logger.info("saving xilogravura with name: {}", fileName);
		//TODO move this logic of handling files to another class
		try (FileOutputStream fos = new FileOutputStream(fileName)){
			fos.write(file.getBytes());
			fos.flush();
			cordel.setXilogravura(xilogravura);
			save(cordel);
		} catch (IOException e) {
			//TODO handle exception
			e.printStackTrace();
		}

		return xilogravura;
	}
}
