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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import static org.assertj.core.api.Assertions.assertThat;

public class FtpClientsTest {

    static final String FTP_HOME = "/ftp";
    static final String USER = "user";
    static final String PASSWORD = "password";
    static final FakeFtpServer fakeFtpServer = new FakeFtpServer();

    @BeforeClass
    public static void setup() throws IOException {
        setUpMockServer();
    }

    private static void setUpMockServer() {
        fakeFtpServer.addUserAccount(new UserAccount(USER, PASSWORD, FTP_HOME));
        fakeFtpServer.setServerControlPort(0);
        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry(FTP_HOME));
        fileSystem.add(new FileEntry(FTP_HOME+"/file1.txt", "abcdef 1234567890"));
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.start();
    }

    @AfterClass
    public static void teardown() {
        fakeFtpServer.stop();
    }

    @Test
    public void testApacheCommonsFtpClient() throws IOException {

        String file = "/file2.txt";
        String fileContent = "content";
        FileSystem fileSystem = fakeFtpServer.getFileSystem();
        FTPClient ftpClient = createApacheFtpClient();

        ftpClient.storeFile(file, new ByteArrayInputStream(fileContent.getBytes()));
        assertThat(fileSystem.exists(file)).isTrue();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
        ftpClient.retrieveFile(file, outputStream);
        String content = new String(outputStream.toByteArray());
        assertThat(content).isEqualTo(fileContent);

        ftpClient.deleteFile(file);
        assertThat(fileSystem.exists(file)).isFalse();

        ftpClient.disconnect();
    }

    private static FTPClient createApacheFtpClient() throws IOException {
        FTPClient ftpClient = new FTPClient();
        ftpClient.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

        ftpClient.connect("localhost", fakeFtpServer.getServerControlPort());
        ftpClient.enterLocalPassiveMode();
        int reply = ftpClient.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftpClient.disconnect();
            throw new IOException("Exception in connecting to FTP Server");
        }

        ftpClient.login(USER, PASSWORD);

        return ftpClient;
    }

}