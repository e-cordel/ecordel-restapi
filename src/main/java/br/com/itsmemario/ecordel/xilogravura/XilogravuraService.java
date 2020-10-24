package br.com.itsmemario.ecordel.xilogravura;

import br.com.itsmemario.ecordel.cordel.Cordel;
import br.com.itsmemario.ecordel.file.FileManager;
import br.com.itsmemario.ecordel.file.FileProcessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;

@Service
public class XilogravuraService {

    private static final String JPG = ".jpg";
    private final String XILOGRAVURA_NAME_PATTERN = "/xilogravura/{file}";

    private XilogravuraRepository repository;
    private FileManager fileManager;

    @Autowired
    public XilogravuraService(XilogravuraRepository repository, FileManager fileManager) {
        this.repository = repository;
        this.fileManager = fileManager;
    }

    public Xilogravura save(Xilogravura xilogravura){
        return repository.save(xilogravura);
    }

    public Xilogravura createXilogravuraWithFile(Xilogravura xilogravura, MultipartFile file){
        try {
            String fileName = generateRandomFileName();
            fileManager.saveFile(file.getBytes(), fileName);
            String url = ServletUriComponentsBuilder.fromCurrentContextPath().path(XILOGRAVURA_NAME_PATTERN).buildAndExpand(fileName).toUriString();
            xilogravura.setUrl(url);
            return repository.save(xilogravura);
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileProcessException("Error while saving file", e);
        }
    }

    private String generateRandomFileName() {
        return UUID.randomUUID().toString() + JPG;
    }

}
