package br.com.wilgner.brotreinos.services;

import br.com.wilgner.brotreinos.exception.BusinessRuleException;
import br.com.wilgner.brotreinos.exception.ErrorCode;
import br.com.wilgner.brotreinos.model.dto.userdto.UserRegisterDTO;
import br.com.wilgner.brotreinos.model.dto.userdto.UserResponseDTO;
import br.com.wilgner.brotreinos.model.entities.User;
import br.com.wilgner.brotreinos.model.repository.UserRepository;
import br.com.wilgner.brotreinos.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Captor
    private ArgumentCaptor<User> argumentCaptor;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private UserRegisterDTO userDto;
    private User userEntity;

    @BeforeEach
    void setUp() {
    userDto = new UserRegisterDTO("wilgn", "123456");
    userEntity = new User();
    userEntity.setId(1L);
    userEntity.setUsername(userDto.username());
    userEntity.setPassword("hashedPassword");
    }

    @Test
    void createUser_whenUsernameNotExists_thenReturnResponseDTO(){
        when(userRepository.findByusername(userDto.username())).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(userDto.password())).thenReturn("hashedPassword");
        when(userRepository.save(argumentCaptor.capture())).thenReturn(userEntity);

        UserResponseDTO result = userServiceImpl.createUser(userDto);

        assertNotNull(result);
        assertEquals(userDto.username(), result.username());

        User userCaptured = argumentCaptor.getValue();
        assertEquals(userDto.username(), userCaptured.getUsername());
        assertEquals("hashedPassword", userCaptured.getPassword());

        verify(userRepository).findByusername(userDto.username());
        verify(bCryptPasswordEncoder).encode(userDto.password());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_whenUsernameExists_thenThrowBusinessRuleException() {
        when(userRepository.findByusername(userDto.username())).thenReturn(Optional.of(userEntity));

        BusinessRuleException thrown = assertThrows(BusinessRuleException.class, () -> {
            userServiceImpl.createUser(userDto);
        });

        assertEquals(ErrorCode.USER_ALREADY_EXISTS, thrown.getErrorCode());

        verify(userRepository).findByusername(userDto.username());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(bCryptPasswordEncoder);
    }
}
