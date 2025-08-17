package br.com.wilgner.brotreinos;

import br.com.wilgner.brotreinos.model.dto.workoutsession.WorkoutSessionMapper;
import br.com.wilgner.brotreinos.model.repository.UserRepository;
import br.com.wilgner.brotreinos.model.repository.WorkoutSessionRepository;
import br.com.wilgner.brotreinos.service.AuthService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class WorkoutSessionImplTest {
    @Mock
    private AuthService authService;
    @Mock
    private WorkoutSessionMapper mapper;
    @Mock
    private WorkoutSessionRepository repository;
    @Mock
    private UserRepository userRepository;

}
