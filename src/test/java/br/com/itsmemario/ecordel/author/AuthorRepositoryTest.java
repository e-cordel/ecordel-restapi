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
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.shaded.org.apache.commons.lang.RandomStringUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Slf4j
@SpringBootTest
class AuthorRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    AuthorRepository repository;

    @Test
    void shouldSaveAnAuthorWithAboutTextBiggerThan255() {
        Author author = new Author();
        String longText = RandomStringUtils.randomAlphabetic(500);
        log.info("Long text: {}", longText);
        author.setName("name");
        author.setAbout(longText);

        Author saved = repository.save(author);

        assertAll(
                () -> assertThat(saved).isNotNull(),
                () -> assertThat(saved.getAbout()).isNotEmpty().isEqualTo(longText)
        );

    }

}