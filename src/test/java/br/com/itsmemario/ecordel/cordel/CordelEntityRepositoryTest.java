package br.com.itsmemario.ecordel.cordel;

import br.com.itsmemario.ecordel.AbstractIntegrationTest;
import br.com.itsmemario.ecordel.author.AuthorEntity;
import br.com.itsmemario.ecordel.author.AuthorRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CordelEntityRepositoryTest extends AbstractIntegrationTest {

    private static final Path BIG_FILE = Paths.get("src/test/resources/content.txt");
    @Autowired
    CordelRepository repository;

    @Autowired
    AuthorRepository authorRepository;
    private Long id;

    @Before
    public void insertNewCordel() throws Exception {
        AuthorEntity authorEntity = authorRepository.save(new AuthorEntity());
        CordelEntity cordelEntity = new CordelEntity();
        cordelEntity.setDescription("description");
        cordelEntity.setTitle("title");
        cordelEntity.setAuthor(authorEntity);
        cordelEntity.setContent("content");
        cordelEntity.setTags(new HashSet<>(Arrays.asList("tag1","tag2")));
        id = repository.save(cordelEntity).getId();
    }

    @Test
    public void saveBigTextAsContent() throws IOException {
        CordelEntity byId = repository.findById(id).get();
        String lines = Files.readAllLines(BIG_FILE).stream().collect(Collectors.joining());
        byId.setContent(lines);

        CordelEntity saved = repository.save(byId);

        assertThat(saved)
                .isNotNull()
                .extracting(CordelEntity::getContent).asString().isNotEmpty();
    }

    @After
    public void deleteAllCordels() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void findAllProjectedBy() {
        Page<CordelView> cordelSummaries = repository.findAllProjectedBy(Pageable.unpaged());
        assertThat(cordelSummaries).isNotEmpty();
        assertThat(cordelSummaries.getContent().get(0)).isInstanceOf(CordelView.class);
    }

    @Test
    public void findByTagsProjectedBy() {
        Page<CordelView> cordels = repository.findByTags(Arrays.asList("tag1", "tag2"), PageRequest.of(0,1));
        assertThat(cordels).hasSize(1);
        assertThat(cordels.getContent().get(0)).extracting(CordelView::getDescription).isEqualTo("description");
    }

    @Test
    public void findByTitleLike() {
        Page<CordelSummary> page = repository.findByTitleLike("tit", PageRequest.of(0,10));
        page.getContent().forEach(cordel -> System.out.println(cordel.getTitle()));
        assertThat(page).hasSize(1);

        page = repository.findByTitleLike("aaa", PageRequest.of(0,10));
        assertThat(page).hasSize(0);
    }

    @Test
    public void testPaginationResultsByTitle() throws Exception {
        deleteAllCordels();
        for(int i = 0; i<5;i++) insertNewCordel();

        Page<CordelSummary> page = repository.findByTitleLike("tit", PageRequest.of(1,3));
        page.getContent().forEach(cordel -> System.out.println(cordel.getTitle()));
        assertThat(page).hasSize(2);
    }

}