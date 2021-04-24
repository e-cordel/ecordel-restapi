package br.com.itsmemario.ecordel.cordel;

import br.com.itsmemario.ecordel.xilogravura.XilogravuraService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {CordelController.class,}, secure = false)
public class CordelEntityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CordelService cordelService;

    @MockBean
    XilogravuraService xilogravuraService;

    private CordelEntity cordelEntity;

    @Before
    public void setUp() throws Exception {
        cordelEntity = new CordelEntity();
        cordelEntity.setId(1l);
        cordelEntity.setContent("");
        cordelEntity.setTitle("");
        cordelEntity.setTags(Collections.emptySet());
        cordelEntity.setDescription("");
    }

    @Test
    public void getCordel() throws Exception {
        when(cordelService.findById(1l)).thenReturn(Optional.of(cordelEntity));
        mockMvc.perform(get("/cordels/1"))
                .andExpect(status().isOk());
    }
}