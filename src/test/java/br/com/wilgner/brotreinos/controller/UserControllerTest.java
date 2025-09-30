//package br.com.wilgner.brotreinos.controller;
//
//import br.com.wilgner.brotreinos.model.dto.userdto.UserRegisterDTO;
//import br.com.wilgner.brotreinos.model.entities.User;
//import br.com.wilgner.brotreinos.model.repository.UserRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//@ActiveProfiles("test")
//public class UserControllerTest {
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    @Autowired
//    private JwtEncoder jwtEncoder;
//    @Autowired
//    private UserRepository userRepository;
//
//    @Test
//    void createUser_whenSuccess_shouldReturn201Created() throws Exception {
//        UserRegisterDTO userDTO = new UserRegisterDTO("WilgnerTest", "SenhaTest");
//
//        mockMvc.perform(post("/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(userDTO)))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void createUser_whenFieldsAreBlank_shouldReturn400BadRequest() throws Exception {
//        // Cenário com dados inválidos para acionar a validação do DTO
//        UserRegisterDTO userDTO = new UserRegisterDTO("", "");
//
//        mockMvc.perform(post("/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDTO)))
//                .andExpect(status().isBadRequest()); // 400 é o status correto para erro de validação
//    }
//
//    @Test
//    void createUser_whenUsernameAlreadyExists_shouldReturn400BadRequest() throws Exception {
//
//        User newUser = new User();
//        newUser.setUsername("WilgnerTest");
//        newUser.setPassword("outraSenha");
//        userRepository.save(newUser);
//
//        UserRegisterDTO userDTO = new UserRegisterDTO("WilgnerTest", "SenhaTest");
//
//        mockMvc.perform(post("/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(userDTO)))
//                .andExpect(status().isBadRequest());
//    }
//}
