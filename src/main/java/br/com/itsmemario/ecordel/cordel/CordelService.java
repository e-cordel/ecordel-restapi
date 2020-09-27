package br.com.itsmemario.ecordel.cordel;

import br.com.itsmemario.ecordel.file.FileManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
public class CordelService {

	public static final String JPG = ".jpg";
	private final Logger logger = LoggerFactory.getLogger(CordelService.class);
	private final String XILOGRAVURA_NAME_PATTERN = "/cordels/%s/xilogravura";

	private CordelRepository repository;
	private FileManager fileManager;

	@Autowired
	public CordelService(CordelRepository repository, FileManager fileManager) {
		super();
		this.repository = repository;
		this.fileManager = fileManager;
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
		String fileName = getFielName(cordel);
		String xilogravura = String.format(XILOGRAVURA_NAME_PATTERN, cordel.getId());

		logger.info("saving xilogravura with name: {}", fileName);
		try {
			fileManager.saveFile(file.getBytes(), fileName);
			cordel.setXilogravura(xilogravura);
			save(cordel);
		} catch (IOException e) {
			//TODO handle exception
			e.printStackTrace();
		}

		return xilogravura;
	}

	public byte[] getImage(Cordel cordel) {
		String fileName = getFielName(cordel);

		try {
			return Files.readAllBytes(Paths.get(fileName));
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}

	}

	private String getFielName(Cordel cordel) {
		return cordel.getId() + JPG;
	}

	public Page<CordelView> findByTitle(String title, Pageable pageable) {
		return repository.findByTitleLike(String.format("%%%s%%",title), pageable);
	}
}
