package br.com.wilgner.brotreinos;

import br.com.wilgner.brotreinos.exception.BusinessRuleException;
import br.com.wilgner.brotreinos.exception.ResourceNotFoundException;
import br.com.wilgner.brotreinos.model.dto.exercisesdto.ExerciseMapper;
import br.com.wilgner.brotreinos.model.dto.exercisesdto.ExercisesCreateDTO;
import br.com.wilgner.brotreinos.model.dto.exercisesdto.ExercisesResponseDTO;
import br.com.wilgner.brotreinos.model.entities.Exercises;
import br.com.wilgner.brotreinos.model.entities.User;
import br.com.wilgner.brotreinos.model.repository.ExercisesRepository;
import br.com.wilgner.brotreinos.model.repository.UserRepository;
import br.com.wilgner.brotreinos.service.AuthService;
import br.com.wilgner.brotreinos.service.ExercisesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExerciseServiceImplTest {

    @Mock
    private ExercisesRepository exercisesRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthService authService;

    @Mock
    private ExerciseMapper exerciseMapper;

    @InjectMocks
    private ExercisesServiceImpl exercisesService;

    private ExercisesCreateDTO createDTO;
    private ExercisesResponseDTO responseDTO;
    private Exercises exerciseEntity;
    private Exercises savedExercise;
    private User user;

    @BeforeEach
    void setUp() {
        // Usuário
        user = new User();
        user.setId(1L);

        // DTO de criação
        createDTO = new ExercisesCreateDTO("Agachamento", 3, 10);

        // Entidade de exercício
        exerciseEntity = new Exercises();
        exerciseEntity.setId(10L);
        exerciseEntity.setName(createDTO.name());
        exerciseEntity.setSeries(createDTO.series());
        exerciseEntity.setRepeticoes(createDTO.repeticoes());
        exerciseEntity.setUser(user);

        // Entidade salva
        savedExercise = new Exercises();
        savedExercise.setId(100L);
        savedExercise.setName(createDTO.name());
        savedExercise.setSeries(createDTO.series());
        savedExercise.setRepeticoes(createDTO.repeticoes());
        savedExercise.setUser(user);

        // DTO de resposta
        responseDTO = new ExercisesResponseDTO(
                createDTO.name(),
                createDTO.series(),
                createDTO.repeticoes()
        );
    }

    @Test
    void getExerciseByName_whenExerciseExists_thenReturnDTO() {
        String exerciseName = "Agachamento";

        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(exercisesRepository.findByUser_IdAndName(user.getId(), exerciseName)).thenReturn(Optional.of(exerciseEntity));
        when(exerciseMapper.toDTO(exerciseEntity)).thenReturn(responseDTO);

        ExercisesResponseDTO result = exercisesService.getExerciseByName(exerciseName);

        assertNotNull(result);
        assertEquals(exerciseName, result.name());
        assertEquals(createDTO.series(), result.series());
        assertEquals(createDTO.repeticoes(), result.repeticoes());

        verify(authService).getAuthenticatedUserId();
        verify(exercisesRepository).findByUser_IdAndName(user.getId(), exerciseName);
        verify(exerciseMapper).toDTO(exerciseEntity);
    }

    @Test
    void getExerciseByName_whenExerciseNotFound_thenThrowResourceNotFoundException() {
        String exerciseName = "NãoExiste";

        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(exercisesRepository.findByUser_IdAndName(user.getId(), exerciseName)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            exercisesService.getExerciseByName(exerciseName);
        });

        assertEquals("EXERCISE_NOT_FOUND", thrown.getErrorCode().name());

        verify(authService).getAuthenticatedUserId();
        verify(exercisesRepository).findByUser_IdAndName(user.getId(), exerciseName);
        verifyNoInteractions(exerciseMapper);
    }

    @Test
    void createExercise_whenValidDto_thendReturnResponseDto() {
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(exercisesRepository.findByUser_IdAndName(user.getId(), createDTO.name())).thenReturn(Optional.empty());
        when(exerciseMapper.toEntity(createDTO)).thenReturn(exerciseEntity);
        when(exercisesRepository.save(any(Exercises.class))).thenReturn(savedExercise);
        when(exerciseMapper.toDTO(savedExercise)).thenReturn(responseDTO);

        ExercisesResponseDTO result = exercisesService.createExercise(createDTO);

        assertNotNull(result);
        assertEquals(createDTO.name(), result.name());
        assertEquals(createDTO.series(), result.series());
        assertEquals(createDTO.repeticoes(), result.repeticoes());

        verify(authService).getAuthenticatedUserId();
        verify(userRepository).findById(user.getId());
        verify(exercisesRepository).findByUser_IdAndName(user.getId(), createDTO.name());
        verify(exerciseMapper).toEntity(createDTO);
        verify(exercisesRepository).save(exerciseEntity);
        verify(exerciseMapper).toDTO(savedExercise);
    }

    @Test
    void createExercises_userNotFound_thenThrowBusinessRuleException() {
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        BusinessRuleException thrown = assertThrows(BusinessRuleException.class, () -> {
            exercisesService.createExercise(createDTO);
        });

        assertEquals("USER_NOT_FOUND", thrown.getErrorCode().name());

        verify(authService).getAuthenticatedUserId();
        verify(userRepository).findById(user.getId());
        verifyNoMoreInteractions(exercisesRepository, exerciseMapper);
    }

    @Test
    void createExercises_NameAlreadyExists_thenThrowBusinessRuleException() {
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(exercisesRepository.findByUser_IdAndName(user.getId(), createDTO.name())).thenReturn(Optional.of(savedExercise));

        BusinessRuleException thrown = assertThrows(BusinessRuleException.class, () ->{
            exercisesService.createExercise(createDTO);
        });
        assertEquals("EXERCISE_ALREADY_EXISTS",  thrown.getErrorCode().name());
        verify(authService).getAuthenticatedUserId();
        verify(userRepository).findById(user.getId());
        verify(exercisesRepository).findByUser_IdAndName(user.getId(), createDTO.name());
        verifyNoMoreInteractions(exercisesRepository, exerciseMapper);
    }

    @Test
    void testUpdateExercise_success_thenReturnResponseDTO() {
        Long exerciseId = 10L;
        ExercisesCreateDTO updateDTO = new ExercisesCreateDTO("Puxada", 4, 8);

        Exercises exerciseToUpdate = new Exercises();
        exerciseToUpdate.setId(exerciseId);
        exerciseToUpdate.setName("Agachamento");
        exerciseToUpdate.setSeries(3);
        exerciseToUpdate.setRepeticoes(10);
        exerciseToUpdate.setUser(user);

        ExercisesResponseDTO expectedResponse = new ExercisesResponseDTO(
                updateDTO.name(),
                updateDTO.series(),
                updateDTO.repeticoes()
        );

        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(exercisesRepository.findByUser_IdAndId(user.getId(), exerciseId)).thenReturn(Optional.of(exerciseToUpdate));
        when(exercisesRepository.findByUser_IdAndName(user.getId(), updateDTO.name())).thenReturn(Optional.empty());
        doNothing().when(exerciseMapper).toEntityUpdate(exerciseToUpdate, updateDTO);
        when(exercisesRepository.save(any(Exercises.class))).thenReturn(exerciseToUpdate);
        when(exerciseMapper.toDTO(exerciseToUpdate)).thenReturn(expectedResponse);

        ExercisesResponseDTO result = exercisesService.updateExercise(exerciseId, updateDTO);

        assertNotNull(result);
        assertEquals(updateDTO.name(), result.name());
        assertEquals(updateDTO.series(), result.series());
        assertEquals(updateDTO.repeticoes(), result.repeticoes());

        verify(authService).getAuthenticatedUserId();
        verify(userRepository).findById(user.getId());
        verify(exercisesRepository).findByUser_IdAndId(user.getId(), exerciseId);
        verify(exercisesRepository).findByUser_IdAndName(user.getId(), updateDTO.name());
        verify(exerciseMapper).toEntityUpdate(exerciseToUpdate, updateDTO);
        verify(exercisesRepository).save(exerciseToUpdate);
        verify(exerciseMapper).toDTO(exerciseToUpdate);
        verifyNoMoreInteractions(exercisesRepository, exerciseMapper);
    }

    @Test
    void testUpdateExercises_UserNotFound_thenThrowBusinessRuleException() {
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        BusinessRuleException thrown = assertThrows(BusinessRuleException.class, () -> {
            exercisesService.updateExercise(user.getId(), createDTO);
        });
        assertEquals("USER_NOT_FOUND", thrown.getErrorCode().name());
        verify(authService).getAuthenticatedUserId();
        verify(userRepository).findById(user.getId());
        verifyNoMoreInteractions(exercisesRepository, exerciseMapper);
    }

    @Test
    void testUpdateExercise_duplicateName_thenThrowsBusinessRuleException() {
        Long exerciseId = 10L;
        ExercisesCreateDTO updateDTO = new ExercisesCreateDTO("Puxada", 4, 8);

        Exercises exerciseToUpdate = new Exercises();
        exerciseToUpdate.setId(exerciseId);
        exerciseToUpdate.setName("Remada");
        exerciseToUpdate.setUser(user);

        Exercises differentExerciseWithSameName = new Exercises();
        differentExerciseWithSameName.setId(20L);
        differentExerciseWithSameName.setName(updateDTO.name());
        differentExerciseWithSameName.setUser(user);

        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(exercisesRepository.findByUser_IdAndId(user.getId(), exerciseId)).thenReturn(Optional.of(exerciseToUpdate));
        when(exercisesRepository.findByUser_IdAndName(user.getId(), updateDTO.name())).thenReturn(Optional.of(differentExerciseWithSameName));

        BusinessRuleException thrown = assertThrows(BusinessRuleException.class, () -> {
            exercisesService.updateExercise(exerciseId, updateDTO);
        });

        assertEquals("EXERCISE_ALREADY_EXISTS", thrown.getErrorCode().name());

        verify(authService).getAuthenticatedUserId();
        verify(userRepository).findById(user.getId());
        verify(exercisesRepository).findByUser_IdAndId(user.getId(), exerciseId);
        verify(exercisesRepository).findByUser_IdAndName(user.getId(), updateDTO.name());
        verifyNoMoreInteractions(exerciseMapper);
    }

    @Test
    void testUpdateExercise_sameNameAsItself_thenNotThrowException() {
        Long exerciseId = 10L;
        ExercisesCreateDTO updateDTO = new ExercisesCreateDTO("Agachamento", 4, 8);

        Exercises exerciseToUpdate = new Exercises();
        exerciseToUpdate.setId(exerciseId);
        exerciseToUpdate.setName("Agachamento");
        exerciseToUpdate.setUser(user);

        // O "existing" é o próprio exercício
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(exercisesRepository.findByUser_IdAndId(user.getId(), exerciseId)).thenReturn(Optional.of(exerciseToUpdate));
        when(exercisesRepository.findByUser_IdAndName(user.getId(), updateDTO.name())).thenReturn(Optional.of(exerciseToUpdate));
        doNothing().when(exerciseMapper).toEntityUpdate(exerciseToUpdate, updateDTO);
        when(exercisesRepository.save(any(Exercises.class))).thenReturn(exerciseToUpdate);
        when(exerciseMapper.toDTO(exerciseToUpdate)).thenReturn(new ExercisesResponseDTO(updateDTO.name(), updateDTO.series(), updateDTO.repeticoes()));

        ExercisesResponseDTO result = exercisesService.updateExercise(exerciseId, updateDTO);

        assertEquals(updateDTO.name(), result.name());
    }

    @Test
    void testUpdateExercise_NotFound_thenThrowExerciseNotFoundException() {
        Long exerciseId = 10L;
        ExercisesCreateDTO updateDTO = new ExercisesCreateDTO("Agachamento", 4, 8);

        Exercises exerciseToUpdate = new Exercises();
        exerciseToUpdate.setId(exerciseId);
        exerciseToUpdate.setName("Puxada");
        exerciseToUpdate.setUser(user);

        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(exercisesRepository.findByUser_IdAndId(user.getId(), exerciseId)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            exercisesService.updateExercise(exerciseId, updateDTO);
        });
        assertEquals("EXERCISE_NOT_FOUND", thrown.getErrorCode().name());
        verify(authService).getAuthenticatedUserId();
        verify(userRepository).findById(user.getId());
        verify(exercisesRepository).findByUser_IdAndId(user.getId(), exerciseId);
        verifyNoMoreInteractions(exercisesRepository, exerciseMapper);

    }

}
