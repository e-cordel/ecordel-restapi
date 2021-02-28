/*
 * Copyright 2021 Projeto e-cordel (http://ecordel.com.br)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package br.com.itsmemario.ecordel.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Primary
@Component
public class FtpFileManager implements FileManager {

    private final FtpConfig ftpConfig;
    private final FTPClient ftpClient;

    public FtpFileManager(FtpConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
        ftpClient = new FTPClient();
    }

    @Override
    public String saveFile(byte[] bytes, String fileName){
        try {
            connect();

            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            ftpClient.setFileTransferMode(FTP.BINARY_FILE_TYPE);
            boolean success = ftpClient.storeFile(fileName, new ByteArrayInputStream(bytes));
            if(success) log.info("File uploaded");

            disconnect();

            return ftpConfig.getBaseUrl()+fileName;
        } catch (IOException e) {
            throw new FileProcessException("Error when saving file", e);
        }
    }

    @Override
    public InputStream getFile(String fileName) {
       try {
           connect();
           InputStream inputStream = ftpClient.retrieveFileStream(fileName);
           disconnect();
           return inputStream;
       } catch (IOException e) {
           throw new FileProcessException("Error when saving file", e);
       }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            connect();
            boolean deleted = ftpClient.deleteFile(fileName);
            if(deleted) log.info("File deleted");
            disconnect();
        } catch (IOException e) {
            throw new FileProcessException("Error when deleting file", e);
        }
    }

    private void connect() throws IOException {
        ftpClient.connect(ftpConfig.getServer(), ftpConfig.getPort());
        ftpClient.enterLocalPassiveMode();
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new IOException("Not possible to connect to FTP server");
        }

        ftpClient.login(ftpConfig.getUser(), ftpConfig.getPassword());
        log.trace("Connected to ftp server");
    }

    void disconnect(){
        try {
            ftpClient.disconnect();
            log.trace("Disconnected from ftp server");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
