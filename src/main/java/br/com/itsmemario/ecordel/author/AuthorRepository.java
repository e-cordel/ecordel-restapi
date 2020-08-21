package br.com.itsmemario.ecordel.author;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    Page<AuthorSummary> findAllProjectedBy(Pageable pageable);

}
