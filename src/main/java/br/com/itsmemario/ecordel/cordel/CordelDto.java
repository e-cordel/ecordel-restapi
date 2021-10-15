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
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * Class used to represent data sent do the backend.
 */
@Data
public class CordelDto {

    private Long id;
    @NotNull
    private AuthorDto author;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String content;
    private boolean published;
    private Set<String> tags;
    private String xilogravuraUrl;

    public static CordelDto of( Cordel cordel ){
        var dto = new CordelDto();
        var authorDto = AuthorDto.of(cordel.getAuthor());
        dto.setAuthor(authorDto);
        dto.setTags(cordel.getTags());
        dto.setPublished(cordel.isPublished());
        dto.setId(cordel.getId());
        dto.setContent(cordel.getContent());
        dto.setDescription(cordel.getDescription());
        dto.setTitle(cordel.getTitle());
        dto.setXilogravuraUrl(cordel.getXilogravuraUrl());
        return dto;
    }

    public Cordel toEntity() {
        var cordel = new Cordel();
        cordel.setId( id );
        cordel.setAuthor( author.toEntity() );
        cordel.setTitle( title );
        cordel.setDescription( description );
        cordel.setContent( content );
        cordel.setPublished( published );
        cordel.setTags( tags );
        cordel.setXilogravuraUrl( xilogravuraUrl );
        return cordel;
    }

}
