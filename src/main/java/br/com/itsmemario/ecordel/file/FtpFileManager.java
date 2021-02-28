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

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;

public class FtpFileManager implements FileManager {

    @Value("${ftp.server}")
    private String server;

    @Value("${fpt.port}")
    private int port;

    @Value("${fpt.user}")
    private String user;

    @Value("${fpt.password}")
    private String password;

    @Value("${ftp.directory}")
    private String directory;

    private final FTPClient ftpClient;

    public FtpFileManager(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }

    void connectServer() throws IOException {
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftpClient.connect(server, port);

        ftpClient.enterLocalPassiveMode();
        ftpClient.changeWorkingDirectory(directory);

        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new IOException("Exception in connecting to FTP server");
        }

        ftpClient.login(user, password);
    }

    void closeServer(){
        try {
            ftpClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String saveFile(byte[] bytes, String fileName){
        try {
            connectServer();

            ftpClient.storeFile(fileName, new ByteArrayInputStream(bytes));

            closeServer();
            return fileName;
        } catch (IOException e) {
            throw new FileProcessException("Error when saving file", e);
        }
    }

    @Override
    public InputStream getFile(String fileName) {
       try {
           connectServer();

           InputStream inputStream = ftpClient.retrieveFileStream(fileName);

           closeServer();

           return inputStream;

       } catch (IOException e) {
           throw new FileProcessException("Error when saving file", e);
       }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            connectServer();

            ftpClient.deleteFile(fileName);

            closeServer();
        } catch (IOException e) {
            throw new FileProcessException("Error when deleting file", e);
        }
    }
}
