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

import jakarta.validation.Valid;
import java.net.URI;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@CrossOrigin
@RestController
@RequestMapping("authors")
public class AuthorController {

  private final AuthorService service;

  public AuthorController(AuthorService service) {
    this.service = service;
  }

  @GetMapping
  public Page<AuthorView> getAll(Pageable pageable) {
    return service.findAll(pageable);
  }

  @PostMapping
  public ResponseEntity<Author> create(@RequestBody @Valid Author author, UriComponentsBuilder uriBuilder) {
    var saved = service.save(author);
    URI uri = uriBuilder.path("/authors/{id}").buildAndExpand(saved.getId()).toUri();
    return ResponseEntity.created(uri).build();
  }

  @GetMapping("{id}")
  public ResponseEntity<Author> getAuthor(@PathVariable Long id) {
    Optional<Author> author = service.findById(id);
    return author.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PutMapping("{id}")
  public ResponseEntity<Author> update(@RequestBody @Valid Author author, @PathVariable Long id) {
    Optional<Author> authorById = service.findById(id);
    if (authorById.isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    author.setId(id);
    var saved = service.save(author);
    return ResponseEntity.ok(saved);
  }
}
