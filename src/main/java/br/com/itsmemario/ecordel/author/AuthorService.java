package br.com.itsmemario.ecordel.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {

    private AuthorRepository repository;

    public AuthorService(AuthorRepository repository) {
        this.repository = repository;
    }

    public Page<AuthorSummary> findAll(Pageable pageable) {
        return repository.findAllProjectedBy(pageable);
    }

    public Author save(Author author) {
        return repository.save(author);
    }
}
