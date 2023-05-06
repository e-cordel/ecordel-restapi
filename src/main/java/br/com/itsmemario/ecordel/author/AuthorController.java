/*
 * Copyright 2020 Projeto e-cordel (http://ecordel.com.br)
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("authors")
public class AuthorController {

    private final Logger logger = LoggerFactory.getLogger(AuthorController.class);

    private AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @GetMapping
    public Page<AuthorView> getAll(Pageable pageable){
        return service.findAll(pageable);
    }

    @PostMapping
    public ResponseEntity<Author> create(@RequestBody @Valid AuthorDto author, UriComponentsBuilder uriBuilder){
        logger.info("request received for create author: {}", author);
        var saved = service.save(author.toEntity());
        URI uri = uriBuilder.path("/authors/{id}").buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("{id}")
    public ResponseEntity<AuthorDto> getAuthor(@PathVariable Long id){
        logger.info("request received get author by id: {}", id);
        Optional<Author> author = service.findById(id);
        if (author.isPresent()) {
            AuthorDto body = AuthorDto.of(author.get());
            return ResponseEntity.ok(body);
        }
        throw new AuthorNotFoundException();
    }

    @PutMapping("{id}")
    public ResponseEntity<AuthorDto> update(@RequestBody @Valid AuthorDto dto, @PathVariable Long id){
        logger.info("request received for update author with id: {}", id);
        Optional<Author> byId = service.findById(id);
        if(byId.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Author newAuthor = dto.toEntity();
        newAuthor.setId(id);
        var saved = service.save(newAuthor);
        return ResponseEntity.ok(AuthorDto.of(saved));
    }
}
