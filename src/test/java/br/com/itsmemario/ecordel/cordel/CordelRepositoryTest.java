package br.com.itsmemario.ecordel.cordel;

import br.com.itsmemario.ecordel.AbstractIntegrationTest;
import br.com.itsmemario.ecordel.author.Author;
import br.com.itsmemario.ecordel.author.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static br.com.itsmemario.ecordel.cordel.CordelUtil.newCordel;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CordelRepositoryTest extends AbstractIntegrationTest {

    private static final Path BIG_FILE = Paths.get("src/test/resources/content.txt");

    @Autowired
    CordelRepository repository;

    @Autowired
    AuthorRepository authorRepository;

    @AfterEach
    void deleteAllCordels() {
        repository.deleteAll();
    }

    @Test
    void saveBigTextAsContent() throws IOException {
        Long id = insertNewCordel(true);

        Cordel byId = repository.findById(id).get();
        String lines = Files.readAllLines(BIG_FILE).stream().collect(Collectors.joining());
        byId.setContent(lines);

        Cordel saved = repository.save(byId);

        assertThat(saved)
                .isNotNull()
                .extracting(Cordel::getContent).asString().isNotEmpty();
    }

    @Test
    void findAllProjectedBy() {
        insertNewCordel(true);
        Page<CordelView> cordelSummaries = repository.findAllProjectedBy(Pageable.unpaged());
        assertThat(cordelSummaries).isNotEmpty();
        assertThat(cordelSummaries.getContent().get(0)).isInstanceOf(CordelView.class);
    }

    @Test
    void findByPublishedTitleLike() {
        insertNewCordel(true);
        Page<CordelSummary> page = repository.findAllByPublishedAndTitleLike(true, "%tit%", PageRequest.of(0,10));
        assertThat(page.getContent().get(0).getAuthorName()).isEqualTo("name");
        assertThat(page).hasSize(1);

        page = repository.findAllByPublishedAndTitleLike(true, "%aaa%", PageRequest.of(0,10));
        assertThat(page).hasSize(0);
    }

    @Test
    void testPaginationResultsByPublishedTitle() throws Exception {
        IntStream.range(0,5).forEach( i -> insertNewCordel(true));

        Page<CordelSummary> page = repository.findAllByPublishedAndTitleLike(true, "%tit%", PageRequest.of(1,3));
        page.getContent().forEach(cordel -> System.out.println(cordel.getTitle()));
        assertThat(page).hasSize(2);
    }

    @Test
    void findNotPublishedWorkTest() {
        insertNewCordel(false);
        Page<CordelSummary> page = repository.findAllByPublishedAndTitleLike(true, "%tit%", PageRequest.of(0, 10));
        assertThat(page).isEmpty();

        page = repository.findAllByPublishedAndTitleLike(false, "%tit%", PageRequest.of(0, 10));
        assertThat(page).isNotEmpty().hasSize(1);
    }

    Long insertNewCordel(boolean published) {
        var author = authorRepository.save(new Author("name"));
        var cordel = newCordel(published, author);
        return repository.save(cordel).getId();
    }

}