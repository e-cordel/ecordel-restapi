package br.com.itsmemario.ecordel.file;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class LocalFileManager implements FileManager {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${app.xilogravura.path}")
    private String basePath;

    @Override
    public void saveFile(byte[] bytes, String fileName) {
        fileName = format(fileName);
        try (FileOutputStream fos = new FileOutputStream(fileName)){
            fos.write(bytes);
            fos.flush();
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
