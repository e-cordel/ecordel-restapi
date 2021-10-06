package br.com.itsmemario.ecordel.xilogravura;

import br.com.itsmemario.ecordel.AbstractIntegrationTest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class XilogravuraServiceTest extends AbstractIntegrationTest {

    public static final int PORT = 10021;
    @Autowired
    XilogravuraService service;

    static final String FTP_HOME = "/ftp";
    static final String USER = "user";
    static final String PASSWORD = "pass";
    static final FakeFtpServer fakeFtpServer = new FakeFtpServer();

    @BeforeAll
    public static void setup() throws IOException {
        setUpMockServer();
    }

    private static void setUpMockServer() {
        fakeFtpServer.addUserAccount(new UserAccount(USER, PASSWORD, FTP_HOME));
        fakeFtpServer.setServerControlPort(PORT);
        FileSystem fileSystem = new UnixFakeFileSystem();
        fileSystem.add(new DirectoryEntry(FTP_HOME));
        fileSystem.add(new FileEntry(FTP_HOME+"/file1.txt", "abcdef 1234567890"));
        fakeFtpServer.setFileSystem(fileSystem);
        fakeFtpServer.start();
    }

    @AfterAll
    public static void afterClass() throws Exception {
        fakeFtpServer.stop();
    }

    @Test
    public void createXilogravuraWithFile() {
        MockMultipartFile file = new MockMultipartFile("file.txt", "content".getBytes());

        String xilogravuraUrl = service.createXilogravuraWithFile(file);
        System.out.println(xilogravuraUrl);

        assertThat(xilogravuraUrl).isNotEmpty();
        //TODO check if file exists
    }
}