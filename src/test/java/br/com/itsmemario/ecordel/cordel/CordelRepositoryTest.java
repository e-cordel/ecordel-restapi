package br.com.itsmemario.ecordel.cordel;

import br.com.itsmemario.ecordel.AbstractIntegrationTest;
import br.com.itsmemario.ecordel.author.Author;
import br.com.itsmemario.ecordel.author.AuthorRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static br.com.itsmemario.ecordel.cordel.CordelUtil.newCordel;
import static org.assertj.core.api.Assertions.assertThat;

class CordelRepositoryTest extends AbstractIntegrationTest {

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
        Long id = insertNewCordel(true).getId();

        Cordel byId = repository.findById(id).get();
        String lines = Files.readAllLines(BIG_FILE).stream().collect(Collectors.joining());
        byId.setContent(lines);

        Cordel saved = repository.save(byId);

        assertThat(saved)
                .isNotNull()
                .extracting(Cordel::getContent).asString().isNotEmpty();
    }

    @Test
    void findByPublishedTitleLikeWithTitleMatch() {

        //arrange
        insertNewCordel(true);
        CordelSummaryRequest params = CordelSummaryRequest.builder().published(true).title("").build();
        PageRequest pageRequest = PageRequest.of(0, 10);

        //act
        Page<CordelSummary> page = repository.findAllByPublishedAndTitleLike(params, pageRequest);

        //test
        assertThat(page.getContent().get(0).getAuthorName()).isEqualTo("name");
        assertThat(page).hasSize(1);

    }

    @Test
    void findPublishedTitlesWithoutMatchingTitle() {

        //arrange
        insertNewCordel(true);
        CordelSummaryRequest params = CordelSummaryRequest.builder().published(true).title("aaa").build();
        PageRequest pageRequest = PageRequest.of(0, 10);

        //act
        Page<CordelSummary> page = repository.findAllByPublishedAndTitleLike(params, pageRequest);

        //test
        assertThat(page).isEmpty();

    }

    @Test
    void testPaginationResultsByPublishedTitle() {

        //arrange
        IntStream.range(0, 5).forEach(i -> insertNewCordel(true));
        CordelSummaryRequest params = CordelSummaryRequest.builder().published(true).title("tit").build();

        //act
        Page<CordelSummary> page = repository.findAllByPublishedAndTitleLike(params, PageRequest.of(1, 3));

        //test
        assertThat(page).hasSize(2);
    }

    @Test
    void testPaginationResultsByAuthor()  {

        //arrange
        Author a = insertNewCordel(true).getAuthor();
        CordelSummaryRequest params = CordelSummaryRequest.builder().published(true).authorId(a.getId()).build();

        //act
        Page<CordelSummary> page = repository.findAllByPublished(params, PageRequest.of(0, 3));

        //test
        assertThat(page).hasSize(1);
    }

    @Test
    void testPaginationResultsByWithoutMatchingAuthor()  {

        //arrange
        CordelSummaryRequest params = CordelSummaryRequest.builder().published(true).authorId(2L).build();

        //act
        Page<CordelSummary> page = repository.findAllByPublished(params, PageRequest.of(0, 3));

        //test
        assertThat(page).isEmpty();
    }

    @Test
    void findNotPublishedWorkTest() {

        //arrange
        insertNewCordel(false);
        CordelSummaryRequest params0 = CordelSummaryRequest.builder().published(true).title("tit").build();

        //act
        Page<CordelSummary> page = repository.findAllByPublishedAndTitleLike(params0, PageRequest.of(0, 10));

        //test
        assertThat(page).isEmpty();

        //arrange
        CordelSummaryRequest params1 = CordelSummaryRequest.builder().published(false).title("tit").build();

        //act
        page = repository.findAllByPublishedAndTitleLike(params1, PageRequest.of(0, 10));

        //test
        assertThat(page).isNotEmpty().hasSize(1);
    }

    Author insertAuthor() {
        Author a = new Author("name");
        return authorRepository.save(a);
    }

    Cordel insertNewCordel(boolean published) {
        var author = insertAuthor();
        var cordel = newCordel(published, author);
        return repository.save(cordel);
    }

}