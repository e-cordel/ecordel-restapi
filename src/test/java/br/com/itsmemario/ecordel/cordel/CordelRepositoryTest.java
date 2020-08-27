package br.com.itsmemario.ecordel.cordel;

import br.com.itsmemario.ecordel.AbstractIntegrationTest;
import br.com.itsmemario.ecordel.author.Author;
import br.com.itsmemario.ecordel.author.AuthorRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CordelRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    CordelRepository repository;

    @Autowired
    AuthorRepository authorRepository;

    @Before
    public void setUp() throws Exception {
        Author author = authorRepository.save(new Author());
        Cordel cordel = new Cordel();
        cordel.setDescription("description");
        cordel.setTitle("title");
        cordel.setAuthor(author);
        cordel.setContent("content");
        cordel.setTags(new HashSet<>(Arrays.asList("tag1","tag2")));
        repository.save(cordel);
    }

    @After
    public void tearDown() throws Exception {
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
        Page<Cordel> cordels = repository.findByTags(Arrays.asList("tag1", "tag2"), PageRequest.of(0,1));
        assertThat(cordels).hasSize(1);
        assertThat(cordels.getContent().get(0)).extracting(Cordel::getDescription).isEqualTo("description");
    }
}