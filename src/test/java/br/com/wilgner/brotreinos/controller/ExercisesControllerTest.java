package br.com.wilgner.brotreinos.controller;

import br.com.wilgner.brotreinos.model.dto.exercisesdto.ExercisesCreateDTO;
import br.com.wilgner.brotreinos.model.entities.User;
import br.com.wilgner.brotreinos.model.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional; // Deixe assim, mas considere usar a versão do Spring
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class ExercisesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private UserRepository userRepository;

    private String jwtToken;
    private Long testUserId;

    @BeforeEach
    void setup() {
        // Crie o usuário e deixe o H2 gerar o ID automaticamente
        User user = new User();
        user.setUsername("usuario-teste");
        user.setPassword("qualquer-senha");
        User savedUser = userRepository.save(user);

        // Obtenha o ID gerado para usar no JWT
        testUserId = savedUser.getId();

        // Gere um JWT de teste com o ID do usuário
        Map<String, Object> claims = new HashMap<>();
        claims.put("sub", testUserId.toString());
        claims.put("scope", "ROLE_USER");

        jwtToken = JwtTestUtils.generateToken(jwtEncoder, claims);
    }

    @Test
    void testCreateExercise() throws Exception {
        ExercisesCreateDTO createDTO = new ExercisesCreateDTO("Supino Reto", 4, 8);

        mockMvc.perform(post("/exercicios")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated());
    }
    @Test
    void testCreateExercise_InvalidDTO_thenReturnsBadRequest() throws Exception {
        // JSON com valores inválidos
        String invalidJson = """
        {
            "name": "",
            "series": 0,
            "repeticoes": 0
        }
        """;

        mockMvc.perform(post("/exercicios")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.name").value("size must be between 1 and 50"))
                .andExpect(jsonPath("$.data.series").value("must be greater than or equal to 1"))
                .andExpect(jsonPath("$.data.repeticoes").value("must be greater than or equal to 1"));
    }
}