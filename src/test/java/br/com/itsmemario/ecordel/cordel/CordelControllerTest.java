package br.com.itsmemario.ecordel.cordel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {CordelController.class}, secure = false)
public class CordelControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CordelService cordelService;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void getCordel() throws Exception {
        when(cordelService.findById(1l)).thenReturn(Optional.of(new Cordel()));
        mockMvc.perform(get("/cordels/1"))
                .andExpect(status().isOk());
    }
}