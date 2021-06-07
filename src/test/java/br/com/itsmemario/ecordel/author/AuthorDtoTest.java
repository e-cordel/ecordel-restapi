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

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AuthorDtoTest {

    @Test
    void toEntity() {
        AuthorDto authorDto = new AuthorDto();
        authorDto.setId(1L);
        authorDto.setAbout("about the author");

        Author author = authorDto.toEntity();

        assertAll(
                () -> assertThat(author.getId()).isEqualTo(authorDto.getId()),
                () -> assertThat(author.getAbout()).isEqualTo(authorDto.getAbout())
        );
    }
}
