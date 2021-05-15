package br.com.itsmemario.ecordel.cordel;

import br.com.itsmemario.ecordel.security.AuthenticationService;
import br.com.itsmemario.ecordel.xilogravura.XilogravuraService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(CordelController.class)
public class CordelControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CordelService cordelService;

    @MockBean
    XilogravuraService xilogravuraService;

    @MockBean
    AuthenticationService authenticationService;

    private Cordel cordel;

    @BeforeEach
    public void setUp() throws Exception {
        cordel = new Cordel();
        cordel.setId(1L);
        cordel.setContent("");
        cordel.setTitle("");
        cordel.setTags(Collections.emptySet());
        cordel.setDescription("");
    }

    @Test
    public void getCordel() throws Exception {
        when(cordelService.findById(1l)).thenReturn(Optional.of(cordel));
        mockMvc.perform(get("/cordels/1"))
                .andExpect(status().isOk());
    }
}