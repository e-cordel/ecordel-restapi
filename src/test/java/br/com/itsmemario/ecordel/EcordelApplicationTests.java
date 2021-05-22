package br.com.itsmemario.ecordel;

import br.com.itsmemario.ecordel.cordel.CordelService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EcordelApplicationTests extends AbstractIntegrationTest{

	@Autowired
	CordelService service;

	@Test
	public void contextLoads() {
		assertThat(service).isNotNull();
	}

}
