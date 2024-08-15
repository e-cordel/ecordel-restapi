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

package br.com.itsmemario.ecordel.author;

import br.com.itsmemario.ecordel.AbstractIntegrationTest;
import br.com.itsmemario.ecordel.security.TokenDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class AuthorControllerTest extends AbstractIntegrationTest {

  public static final String AUTHORS = "/authors";

  @Autowired
  MockMvc mockMvc;
  @Autowired
  AuthorRepository repository;

  @Test
  void requestWithoutValidTokenMustReturnStatusCodeForbidden() throws Exception {
    mockMvc.perform(post(AUTHORS)).andExpect(status().isForbidden());
  }

  @Test
  @Sql(scripts = "classpath:db/data/add-admin-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "classpath:db/data/clean-user-authorities.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void requestWithValidTokenMustReturnStatusCodeCreated() throws Exception {
    TokenDto token = getAdminToken(mockMvc);
    AuthorDto dto = new AuthorDto();
    dto.setName("name");
    String json = new ObjectMapper().writer().writeValueAsString(dto);
    mockMvc.perform(
            post(AUTHORS)
                    .header(HttpHeaders.AUTHORIZATION, token.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
    ).andExpect(status().isCreated());
  }

  @Test
  void getAuthorsReturnStatusCodeOK() throws Exception {
    mockMvc.perform(get(AUTHORS)).andExpect(status().isOk());
  }

  @Test
  void getAuthorReturnStatusCodeOKIfItExists() throws Exception {
    var dummyAuthor = repository.save(new Author("Dummy Author"));
    mockMvc.perform(get(AUTHORS.concat("/" + dummyAuthor.getId())))
            .andExpect(status().isOk());
  }

  @Test
  void getAuthorReturnNotFoundIfItDoesNotExist() throws Exception {
    mockMvc.perform(get(AUTHORS.concat("/100")))
            .andExpect(status().isNotFound());
  }

  @Test
  @Sql(scripts = "classpath:db/data/add-admin-user.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  @Sql(scripts = "classpath:db/data/clean-user-authorities.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
  void putRequestWithValidTokenMustReturnStatusCodeOk() throws Exception {
    TokenDto token = getAdminToken(mockMvc);
    var dummyAuthor = repository.save(new Author("Dummy Author"));
    AuthorDto dto = AuthorMapper.INSTANCE.toDto(dummyAuthor);
    String json = new ObjectMapper().writer().writeValueAsString(dto);
    mockMvc.perform(
            put(AUTHORS.concat("/" + dummyAuthor.getId()))
                    .header(HttpHeaders.AUTHORIZATION, token.toString())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
    ).andExpect(status().isOk());
  }

  @Test
  void requestWithoutTokenMustReturnForbidden() throws Exception {
    AuthorDto dto = new AuthorDto();
    dto.setId(1L);
    dto.setName("Author 1 - Ed");
    String json = new ObjectMapper().writer().writeValueAsString(dto);
    mockMvc.perform(
            put(AUTHORS.concat("/1"))
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
    ).andExpect(status().isForbidden());
  }

  private TokenDto getAdminToken(MockMvc mockMvc) throws Exception {
    Map<String, String> login = Map.of("username", "admin", "password", "admin");
    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writer().writeValueAsString(login);
    String response = mockMvc.perform(
            post("/auth")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json)
    ).andReturn().getResponse().getContentAsString();
    return objectMapper.readValue(response, TokenDto.class);
  }
}
