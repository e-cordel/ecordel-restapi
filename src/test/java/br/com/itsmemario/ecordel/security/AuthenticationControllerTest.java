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

package br.com.itsmemario.ecordel.security;

import br.com.itsmemario.ecordel.AbstractIntegrationTest;
import br.com.itsmemario.ecordel.security.jwt.JwtTokenService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class AuthenticationControllerTest extends AbstractIntegrationTest {

    @Autowired
    UserRepository repository;

    @Autowired
    JwtTokenService tokenService;

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    void authenticateWithCorrectCredentials() {
        var test = "test";
        var pass = new BCryptPasswordEncoder().encode(test);
        var cordelUser = new CordelUser(test,pass);
        repository.save(cordelUser);

        var url =  "http://localhost:" + port + "/auth";
        ResponseEntity<TokenDto> response = restTemplate.postForEntity(url, new LoginData(test, test), TokenDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).extracting("token").isNotNull();
    }

    @Test
    void failedAuthenticationMustReturn401() {
        var url =  "http://localhost:" + port + "/auth";
        ResponseEntity<TokenDto> response = restTemplate.postForEntity(url, new LoginData("", ""), TokenDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}