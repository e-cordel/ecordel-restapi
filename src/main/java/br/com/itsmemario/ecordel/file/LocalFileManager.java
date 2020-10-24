package br.com.itsmemario.ecordel.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.PostConstruct;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class LocalFileManager implements FileManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String basePath;

    public LocalFileManager(@Value("${app.xilogravura.path}")String basePath) {
        this.basePath = basePath;
    }

    @PostConstruct
    private void setup(){
        try {
            if(basePath.charAt(basePath.length()-1)!=File.separatorChar){
                basePath += "/";
            }
            Files.createDirectories(Paths.get(basePath));
            logger.info("{} created successfully", basePath);
        } catch (IOException e) {
            logger.info("Directory {} already created", basePath);
        }
    }

    @Override
    public String saveFile(byte[] bytes, String fileName) {
        String filePath = format(fileName);
        try (FileOutputStream fos = new FileOutputStream(filePath)){
            fos.write(bytes);
            fos.flush();
            return filePath;
        } catch (IOException e) {
            throw new FileProcessException("Not possible to save image", e);
        }
    }

    @Override
    public InputStream getFile(String fileName) {
        try {
            fileName = format(fileName);
            return new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            throw new FileProcessException("Not possible to load "+fileName, e);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            fileName = format(fileName);
            Files.delete(Paths.get(fileName));
        } catch (IOException e) {
            throw new FileProcessException("Not possible to delete "+fileName, e);
        }
    }

    private String format(String fileName) {
        return basePath+fileName;
    }

}
