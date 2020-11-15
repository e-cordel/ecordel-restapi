package br.com.itsmemario.ecordel.xilogravura;

import br.com.itsmemario.ecordel.AbstractIntegrationTest;
import br.com.itsmemario.ecordel.author.Author;
import br.com.itsmemario.ecordel.file.FileManager;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XilogravuraServiceTest extends AbstractIntegrationTest {

    @Autowired
    XilogravuraService service;

    @Autowired
    XilogravuraRepository repository;

    @Autowired
    FileManager fileManager;

    @After
    public void tearDown() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void createXilogravuraWithFile() {
        Xilogravura xilogravura = new Xilogravura();
        xilogravura.setDescription("Description");

        MockMultipartFile file = new MockMultipartFile("file.txt", "content".getBytes());

        Xilogravura xilogravuraWithFile = service.createXilogravuraWithFile(xilogravura, file);
        System.out.println(xilogravuraWithFile.getUrl());

        assertThat(xilogravuraWithFile.getUrl()).isNotEmpty();
        assertThat(xilogravuraWithFile.getId()).isGreaterThan(0);
        assertThat(repository.findById(1l)).isNotEmpty();
    }
}