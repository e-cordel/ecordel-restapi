package br.com.itsmemario.ecordel.xilogravura;

import br.com.itsmemario.ecordel.AbstractIntegrationTest;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockftpserver.fake.FakeFtpServer;
import org.mockftpserver.fake.UserAccount;
import org.mockftpserver.fake.filesystem.DirectoryEntry;
import org.mockftpserver.fake.filesystem.FileEntry;
import org.mockftpserver.fake.filesystem.FileSystem;
import org.mockftpserver.fake.filesystem.UnixFakeFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XilogravuraServiceTest extends AbstractIntegrationTest {

    public static final int PORT = 10021;
    @Autowired
    XilogravuraService service;

    @Autowired
    XilogravuraRepository repository;

    static final String FTP_HOME = "/ftp";
    static final String USER = "user";
    static final String PASSWORD = "pass";
    static final FakeFtpServer fakeFtpServer = new FakeFtpServer();

    @BeforeClass
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

    @AfterClass
    public static void afterClass() throws Exception {
        fakeFtpServer.stop();
    }

    @After
    public void tearDown() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void createXilogravuraWithFile() {
        XilogravuraEntity xilogravuraEntity = new XilogravuraEntity();
        xilogravuraEntity.setDescription("Description");

        MockMultipartFile file = new MockMultipartFile("file.txt", "content".getBytes());

        XilogravuraEntity xilogravuraEntityWithFile = service.createXilogravuraWithFile(xilogravuraEntity, file);
        System.out.println(xilogravuraEntityWithFile.getUrl());

        assertThat(xilogravuraEntityWithFile.getUrl()).isNotEmpty();
        assertThat(xilogravuraEntityWithFile.getId()).isPositive();
        assertThat(repository.findById(1l)).isNotEmpty();
    }
}