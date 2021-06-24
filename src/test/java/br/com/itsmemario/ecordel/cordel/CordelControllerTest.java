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
class CordelControllerTest extends AbstractIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    CordelRepository cordelRepository;

    @Autowired
    AuthorRepository authorRepository;


    @AfterEach
    void tearDown() {
        cordelRepository.deleteAll();
        authorRepository.deleteAll();
    }

    Cordel insertCordel(boolean published) {
        Author author = authorRepository.save(new Author("name"));
        var cordel = newCordel(published, author);
        return cordelRepository.save(cordel);
    }

    @Test
    void ifACordelExists_ItMustReturnOkAndTheCordel() {
        Cordel cordel = insertCordel(true);

        ResponseEntity<Cordel> forEntity = restTemplate.getForEntity(getBaseUrl() + "/{id}", Cordel.class, cordel.getId());

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(forEntity.getBody()).hasFieldOrPropertyWithValue("content", cordel.getContent());
    }

    @Test
    void ifACordelDoesNotExists_ItMustReturn404() {
        Long id = 100l;

        ResponseEntity<Cordel> forEntity = restTemplate.getForEntity(getBaseUrl() + "/{id}", Cordel.class, id);

        assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void ifPublishedParamIsFalse_theOnlyDraftCordelsMustBeRetrieved() {
        insertCordel(false);

        ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl()+"?published=false", Map.class);
        assertThat(response.getBody()).containsEntry("totalElements",1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void ifPublishedParamIsNotInformed_itMustBeConsideredTrue() {
        insertCordel(true);

        ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl(), Map.class);
        assertThat(response.getBody()).containsEntry("totalElements",1);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/cordels";
    }
}
