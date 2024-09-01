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
import br.com.itsmemario.ecordel.security.TokenDto;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static br.com.itsmemario.ecordel.cordel.CordelUtil.newCordel;
import static org.assertj.core.api.Assertions.assertThat;

class CordelControllerTest extends AbstractIntegrationTest {

  @Autowired
  CordelRepository cordelRepository;

  @Autowired
  AuthorRepository authorRepository;

  @AfterEach
  void tearDown() {
    cordelRepository.deleteAll();
    authorRepository.deleteAll();
  }

  @Test
  void ifACordelExists_getRequestMustReturnOkAndTheCordel() {
    Cordel cordel = insertCordel(true);

    ResponseEntity<Cordel> forEntity = restTemplate.getForEntity(getBaseUrl() + "/{id}", Cordel.class, cordel.getId());

    assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    Cordel returnedCordel = forEntity.getBody();
    assertThat(returnedCordel.getContent()).isEqualTo(cordel.getContent());
    assertThat(returnedCordel.getAuthor()).isNotNull();
    assertThat(returnedCordel.getAuthor().getName()).isEqualTo(cordel.getAuthor().getName());
  }

  @Test
  void ifACordelDoesNotExists_ItMustReturn404() {
    Long id = 100L;

    ResponseEntity<Cordel> forEntity = restTemplate.getForEntity(getBaseUrl() + "/{id}", Cordel.class, id);

    assertThat(forEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
  }

  @Test
  void ifPublishedParamIsFalse_theOnlyDraftCordelsMustBeRetrieved() {
    insertCordel(false);

    ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl() + "/summaries?published=false", Map.class);
    assertThat(response.getBody()).containsEntry("totalElements", 1);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  void ifPublishedParamIsNotInformed_itMustBeConsideredTrue() {
    insertCordel(true);

    ResponseEntity<Map> response = restTemplate.getForEntity(getBaseUrl() + "/summaries", Map.class);
    assertThat(response.getBody()).containsEntry("totalElements", 1);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
  }

  @Test
  @Sql(scripts = "classpath:db/data/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "classpath:db/data/clean-user-authorities.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void onlyAdminAndEditorRoles_shouldBeAbleToUpdateEbookUrl() {
    var cordel = insertCordel(true);

    var adminToken = login("admin", "admin");
    var adminHeaders = new HttpHeaders();
    adminHeaders.add(HttpHeaders.AUTHORIZATION, adminToken.toString());
    HttpEntity<String> requestEntity = new HttpEntity<>("http://url.com", adminHeaders);
    ResponseEntity<Void> response = restTemplate.exchange(getBaseUrl() + "/{id}/ebook-url", HttpMethod.PUT, requestEntity, Void.class, cordel.getId());
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    var userToken = login("user", "user");
    var userHeaders = new HttpHeaders();
    userHeaders.add(HttpHeaders.AUTHORIZATION, userToken.toString());
    HttpEntity<String> userRequest = new HttpEntity<>("http://url.com", userHeaders);
    ResponseEntity<Void> userResponse = restTemplate.exchange(getBaseUrl() + "/{id}/ebook-url", HttpMethod.PUT, userRequest, Void.class, cordel.getId());
    assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
  }

  @Test
  @Sql(scripts = "classpath:db/data/add-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "classpath:db/data/clean-user-authorities.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void shouldReturnABadRequest_ifEbookUrlIsInvalid() {
    var cordel = insertCordel(true);

    TokenDto tokenDto = login("admin", "admin");
    var headers = new HttpHeaders();
    headers.add(HttpHeaders.AUTHORIZATION, tokenDto.toString());
    HttpEntity<String> requestEntity = new HttpEntity<>("INVALID_URL", headers);

    ResponseEntity<Void> response = restTemplate.exchange(getBaseUrl() + "/{id}/ebook-url", HttpMethod.PUT, requestEntity, Void.class, cordel.getId());
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
  }

  private String getBaseUrl() {
    return "http://localhost:" + port + "/cordels";
  }

  Cordel insertCordel(boolean published) {
    Author author = authorRepository.save(new Author("name"));
    var cordel = newCordel(published, author);
    return cordelRepository.save(cordel);
  }
}
