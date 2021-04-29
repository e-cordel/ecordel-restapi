package br.com.itsmemario.ecordel.xilogravura;


import br.com.itsmemario.ecordel.AbstractIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class XilogravuraRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    XilogravuraRepository repository;

    @Test
    public void saveTest() {
        XilogravuraEntity xilogravuraEntity = new XilogravuraEntity();
        xilogravuraEntity.setUrl("url");
        xilogravuraEntity.setDescription("description");

        XilogravuraEntity saved = repository.save(xilogravuraEntity);

        assertThat(saved).isNotNull();
        assertThat(saved.getId()).isPositive();
    }
}
