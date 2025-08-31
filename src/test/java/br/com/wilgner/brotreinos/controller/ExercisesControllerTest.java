package br.com.wilgner.brotreinos.controller;

import br.com.wilgner.brotreinos.model.dto.exercisesdto.ExercisesCreateDTO;
import br.com.wilgner.brotreinos.model.entities.Exercises;
import br.com.wilgner.brotreinos.model.entities.User;
import br.com.wilgner.brotreinos.model.repository.ExercisesRepository;
import br.com.wilgner.brotreinos.model.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;


import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Autowired
    private ExercisesRepository exercisesRepository;

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
        Exercises create = new Exercises();
        create.setName("Agachamento");
        create.setRepeticoes(7);
        create.setSeries(10);
        create.setUser(savedUser);
        Exercises saved = exercisesRepository.save(create);
        jwtToken = JwtTestUtils.generateToken(jwtEncoder, claims);
    }

    @Nested
    class CreateExerciseTest {
        @Test
        void createExercise() throws Exception {
            ExercisesCreateDTO createDTO = new ExercisesCreateDTO("Supino Reto", 4, 8);

            mockMvc.perform(post("/exercicios")
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(createDTO)))
                    .andExpect(status().isCreated());
        }

        @Test
        void createExercise_InvalidDTO_thenReturnsBadRequest() throws Exception {
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
                    .andExpect(jsonPath("$.data.name").value("O nome não pode estar vazio"))
                    .andExpect(jsonPath("$.data.series").value("must be greater than or equal to 1"))
                    .andExpect(jsonPath("$.data.repeticoes").value("must be greater than or equal to 1"));
        }
    }

    @Nested
    class GetExercises {
        @Test
        void getExercisesByName() throws Exception {

            mockMvc.perform(get("/exercicios")
                    .header("Authorization", "Bearer " + jwtToken)
                            .param("name", "Agachamento")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.name").value("Agachamento"))
                    .andExpect(jsonPath("$.data.series").value(10))
                    .andExpect(jsonPath("$.data.repeticoes").value(7));;

        }

        @Test
        void getExercisesByNameWhenExerciseNotExists() throws Exception {
            mockMvc.perform(get("/exercicios")
                    .header("Authorization", "Bearer " + jwtToken)
                    .param("name","Bulgaro")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }


        @Test
        void getExercisesByNameWhenNotAuthenticated() throws Exception {
            mockMvc.perform(get("/exercicios")
                            .param("name","Bulgaro")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        void getExercisesByNameWhenTokenIsInvalid() throws Exception {
            mockMvc.perform(get("/exercicios")
                            .header("Authorization", "Bearer token_invalido")
                            .param("name", "Agachamento")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class UpdateExercise{
        @Test
        void updateExercise() throws Exception {
            ExercisesCreateDTO updateDTO = new ExercisesCreateDTO("Agachamento Livre", 5, 12);
            mockMvc.perform(put("/exercicios/{id}", 1L)
            .header("Authorization", "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updateDTO)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.name").value("Agachamento Livre"))
                    .andExpect(jsonPath("$.data.series").value(5))
                    .andExpect(jsonPath("$.data.repeticoes").value(12));

        }
        @Test
        void updateExerciseWhenNotExists() throws Exception {
            ExercisesCreateDTO updateDTO = new ExercisesCreateDTO("Agachamento Livre", 5, 12);
            mockMvc.perform(put("/exercicios/{id}", 100L)
                            .header("Authorization", "Bearer " + jwtToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateDTO)))
                    .andExpect(status().isNotFound());


        }
    }
}