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

import br.com.itsmemario.ecordel.author.AuthorDto;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CordelDtoTest {

    @Test
    void toEntity() {
        var dto = new CordelDto();
        AuthorDto author = new AuthorDto();
        author.setId(1l);
        dto.setAuthor(author);
        dto.setTitle("title");
        dto.setContent("content");
        dto.setDescription("description");
        dto.setId(1l);
        dto.setPublished(true);
        dto.setTags(Collections.emptySet());
        var cordel = dto.toEntity();

        assertAll(
                () -> assertThat(dto.getAuthor().getId()).isEqualTo(cordel.getAuthor().getId()),
                () -> assertThat(dto.getContent()).isEqualTo(cordel.getContent()),
                () -> assertThat(dto.getDescription()).isEqualTo(cordel.getDescription()),
                () -> assertThat(dto.getId()).isEqualTo(cordel.getId()),
                () -> assertThat(dto.getTitle()).isEqualTo(cordel.getTitle()),
                () -> assertThat(dto.getTags()).isEqualTo(cordel.getTags())
        );
    }
}