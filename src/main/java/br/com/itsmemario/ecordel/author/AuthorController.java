package br.com.itsmemario.ecordel.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@CrossOrigin
@RestController
@RequestMapping("authors")
public class AuthorController {

    private AuthorService service;

    public AuthorController(AuthorService service) {
        this.service = service;
    }

    @GetMapping
    public Page<AuthorSummary> getAll(Pageable pageable){
        return service.findAll(pageable);
    }

    @PostMapping
    public ResponseEntity<Author> newAuthor(@RequestBody @Valid Author author, UriComponentsBuilder uriBuilder){
        service.save(author);
        URI uri = uriBuilder.path("/authors/{id}").buildAndExpand(author.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }
}
