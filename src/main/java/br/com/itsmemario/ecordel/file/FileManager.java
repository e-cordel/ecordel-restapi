package br.com.itsmemario.ecordel.file;

import java.io.InputStream;

public interface FileManager {
    String saveFile(byte[] bytes, String fileName);
    InputStream getFile(String fileName);
    void deleteFile(String fileName);
}
