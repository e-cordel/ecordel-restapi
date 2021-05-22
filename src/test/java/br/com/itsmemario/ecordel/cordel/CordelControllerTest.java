package br.com.itsmemario.ecordel.cordel;

import br.com.itsmemario.ecordel.AbstractIntegrationTest;
import br.com.itsmemario.ecordel.author.Author;
import br.com.itsmemario.ecordel.author.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static br.com.itsmemario.ecordel.cordel.CordelUtil.newCordel;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CordelControllerTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    CordelRepository cordelRepository;

    @Autowired
    AuthorRepository authorRepository;


    @AfterEach
    public void tearDown() {
        cordelRepository.deleteAll();
        authorRepository.deleteAll();
    }

    public Cordel insertCordel(boolean published) {
        Author author = authorRepository.save(new Author());
        var cordel = newCordel(published, author);
        return cordelRepository.save(cordel);
    }

    @Test
    public void ifACordelExists_ItMustReturnOkAndTheCordel() {
        Cordel cordel = insertCordel(true);

        ResponseEntity<Cordel> forEntity = restTemplate.getForEntity(getBaseUrl() + "/{id}", Cordel.class, cordel.getId());

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(forEntity.getBody()).hasFieldOrPropertyWithValue("content", cordel.getContent());
    }

    @Test
    public void ifACordelDoesNotExists_ItMustReturn404() {
        Long id = 100l;

        ResponseEntity<Cordel> forEntity = restTemplate.getForEntity(getBaseUrl() + "/{id}", Cordel.class, id);

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void ifPublishedParamIsFalse_theOnlyDraftCordelsMustBeRetrieved() {
        insertCordel(false);

        ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl()+"?published=false", Map.class);
        assertThat(response.getBody().get("totalElements")).isEqualTo(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void ifPublishedParamIsNotInformed_itMustBeConsideredTrue() {
        insertCordel(true);

        ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl(), Map.class);
        assertThat(response.getBody().get("totalElements")).isEqualTo(1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/cordels";
    }
}
