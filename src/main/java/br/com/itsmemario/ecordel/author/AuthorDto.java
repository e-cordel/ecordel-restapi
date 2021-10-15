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

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AuthorDto {

    private Long id;
    private String about;
    @NotNull
    private String name;
    private String email;

    public static AuthorDto of(Author author) {
        var dto = new AuthorDto();
        dto.setName(author.getName());
        dto.setAbout(author.getAbout());
        dto.setEmail(author.getEmail());
        dto.setId(author.getId());
        return dto;
    }

    public Author toEntity() {
        var author = new Author();
        author.setId(id);
        author.setAbout(about);
        author.setName(name);
        author.setEmail(email);
        return author;
    }
}
