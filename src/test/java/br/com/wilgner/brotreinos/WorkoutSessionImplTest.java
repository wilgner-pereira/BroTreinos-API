package br.com.wilgner.brotreinos;

import br.com.wilgner.brotreinos.exception.BusinessRuleException;
import br.com.wilgner.brotreinos.exception.ErrorCode;
import br.com.wilgner.brotreinos.exception.ResourceNotFoundException;
import br.com.wilgner.brotreinos.model.dto.workoutsession.WorkoutSessionCreateDTO;
import br.com.wilgner.brotreinos.model.dto.workoutsession.WorkoutSessionMapper;
import br.com.wilgner.brotreinos.model.dto.workoutsession.WorkoutSessionResponseDTO;
import br.com.wilgner.brotreinos.model.entities.User;
import br.com.wilgner.brotreinos.model.entities.workout.WorkoutSession;
import br.com.wilgner.brotreinos.model.repository.UserRepository;
import br.com.wilgner.brotreinos.model.repository.WorkoutSessionRepository;
import br.com.wilgner.brotreinos.service.AuthService;
import br.com.wilgner.brotreinos.service.WorkoutSessionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutSessionImplTest {
    @Mock
    private AuthService authService;
    @Mock
    private WorkoutSessionMapper mapper;
    @Mock
    private WorkoutSessionRepository wkRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WorkoutSessionServiceImpl  workoutSessionServiceImpl;

    private User  user;
    private WorkoutSession workoutSession;
    private WorkoutSession setUpWkEntity;
    private WorkoutSession setUpWkSavedEntity;
    private WorkoutSessionCreateDTO setUpWkCreateDTO;
    private WorkoutSessionResponseDTO setUpWkResponseDTO;

    @BeforeEach
    public void setUp(){
        user = new User();
        user.setId(1L);

        setUpWkCreateDTO = new WorkoutSessionCreateDTO(DayOfWeek.MONDAY, LocalDate.now());

        setUpWkEntity = new WorkoutSession();
        setUpWkEntity.setUser(user);
        setUpWkEntity.setDayOfWeek(setUpWkCreateDTO.dayOfWeek());
        setUpWkEntity.setWorkoutDate(setUpWkCreateDTO.localdate());

        setUpWkSavedEntity = new WorkoutSession();
        setUpWkSavedEntity.setId(10L);
        setUpWkSavedEntity.setUser(user);
        setUpWkSavedEntity.setDayOfWeek(setUpWkEntity.getDayOfWeek());
        setUpWkSavedEntity.setWorkoutDate(setUpWkEntity.getWorkoutDate());

        setUpWkResponseDTO = new WorkoutSessionResponseDTO(setUpWkSavedEntity.getId(), setUpWkSavedEntity.getDayOfWeek(), setUpWkSavedEntity.getWorkoutDate());
    }

    @Test
    void testCreateWorkoutSession_whenSucess_thenReturnWorkoutSessionResponseDTO(){

        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mapper.toWorkout(setUpWkCreateDTO)).thenReturn(setUpWkEntity);
        when(wkRepository.save(setUpWkEntity)).thenReturn(setUpWkSavedEntity);
        when(mapper.toWorkoutResponseDTO(setUpWkSavedEntity)).thenReturn(setUpWkResponseDTO);

        WorkoutSessionResponseDTO result = workoutSessionServiceImpl.createSession(setUpWkCreateDTO);

        assertEquals(setUpWkResponseDTO, result);
        verify(authService).getAuthenticatedUserId();
        verify(userRepository).findById(user.getId());
        verify(mapper).toWorkout(setUpWkCreateDTO);
        verify(wkRepository).save(setUpWkEntity);
        verify(mapper).toWorkoutResponseDTO(setUpWkSavedEntity);
        verifyNoMoreInteractions(authService, userRepository, wkRepository);
    }

    @Test
    void testCreateWorkoutSession_whenFail_thenReturnBusinessRuleException(){
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        BusinessRuleException thrown = assertThrows(BusinessRuleException.class, () -> {
            workoutSessionServiceImpl.createSession(setUpWkCreateDTO);
        });
        assertEquals("USER_NOT_FOUND", thrown.getErrorCode().name());

        verify(authService).getAuthenticatedUserId();
        verify(userRepository).findById(user.getId());
        verifyNoMoreInteractions(authService, userRepository, wkRepository);
    }

    @Test
    void testlistAllSessions_whenSucess_thenReturnListOfWorkoutSessions(){
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());

        List<WorkoutSession> workoutList = List.of(setUpWkSavedEntity);
        when(wkRepository.findAllByUser_Id(user.getId())).thenReturn(workoutList);

        when(mapper.toWorkoutResponseDTO(setUpWkSavedEntity)).thenReturn(setUpWkResponseDTO);

        List<WorkoutSessionResponseDTO> result = workoutSessionServiceImpl.listAllSessions();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(setUpWkResponseDTO.id(), result.get(0).id());
        assertEquals(setUpWkResponseDTO.dayOfWeek(), result.get(0).dayOfWeek());
        assertEquals(setUpWkResponseDTO.localdate(), result.get(0).localdate());

        verify(authService).getAuthenticatedUserId();
        verify(wkRepository).findAllByUser_Id(user.getId());
        verify(mapper).toWorkoutResponseDTO(setUpWkSavedEntity);
    }

    @Test
    void testlistAllSessions_whenFailure_thenReturn(){
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());

        when(wkRepository.findAllByUser_Id(user.getId())).thenReturn(List.of());

        List<WorkoutSessionResponseDTO> result = workoutSessionServiceImpl.listAllSessions();
        assertNotNull(result, "O resultado não deve ser nulo");
        assertTrue(result.isEmpty(), "A lista deve estar vazia quando não houver sessões");


        verify(authService).getAuthenticatedUserId();
        verify(wkRepository).findAllByUser_Id(user.getId());
    }

    @Test
    void testGetSessionById_whenSucess_thenReturnWorkoutResponseDTO(){
        Long wkId = setUpWkSavedEntity.getId();
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(wkRepository.findByIdAndUser_Id(wkId, user.getId())).thenReturn(Optional.of(setUpWkSavedEntity));
        when(mapper.toWorkoutResponseDTO(setUpWkSavedEntity)).thenReturn(setUpWkResponseDTO);

        WorkoutSessionResponseDTO result = workoutSessionServiceImpl.getSessionById(wkId);
        assertEquals(setUpWkResponseDTO.id(), result.id());
        assertEquals(setUpWkResponseDTO.dayOfWeek(), result.dayOfWeek());
        assertEquals(setUpWkResponseDTO.localdate(), result.localdate());
        verify(authService).getAuthenticatedUserId();
        verify(wkRepository).findByIdAndUser_Id(wkId, user.getId());
        verify(mapper).toWorkoutResponseDTO(setUpWkSavedEntity);
        verifyNoMoreInteractions(authService, userRepository, wkRepository);
    }

    @Test
    void testGetSessionById_whenFailure_thenReturnResourceNotFoundException(){
        Long wkId = setUpWkSavedEntity.getId();
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(wkRepository.findByIdAndUser_Id(wkId, user.getId())).thenReturn(Optional.empty());
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            workoutSessionServiceImpl.getSessionById(wkId);
        });
        assertEquals("WORKOUT_SESSION_NOT_FOUND", thrown.getErrorCode().name());
        verify(authService).getAuthenticatedUserId();
        verify(wkRepository).findByIdAndUser_Id(wkId, user.getId());
        verifyNoMoreInteractions(authService, userRepository, wkRepository);
    }

    @Test
    void testUpdateSession_whenSucess_thenReturnWorkoutResponseDTO() {
        // Arrange
        WorkoutSessionCreateDTO toUpdateDTO = new WorkoutSessionCreateDTO(DayOfWeek.SATURDAY, LocalDate.now());

        Long wkId = setUpWkSavedEntity.getId();

        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(wkRepository.findByIdAndUser_Id(wkId, user.getId())).thenReturn(Optional.of(setUpWkSavedEntity));
        doNothing().when(mapper).toEntityUpdate(setUpWkSavedEntity, toUpdateDTO);
        when(wkRepository.save(setUpWkSavedEntity)).thenReturn(setUpWkSavedEntity);

        WorkoutSessionResponseDTO expectedResponseDTO =
                new WorkoutSessionResponseDTO(
                        setUpWkSavedEntity.getId(),
                        toUpdateDTO.dayOfWeek(),
                        toUpdateDTO.localdate()
                );

        when(mapper.toWorkoutResponseDTO(setUpWkSavedEntity))
                .thenReturn(expectedResponseDTO);

        WorkoutSessionResponseDTO result =
                workoutSessionServiceImpl.updateSession(wkId, toUpdateDTO);

        assertNotNull(result);
        assertEquals(expectedResponseDTO.id(), result.id());
        assertEquals(expectedResponseDTO.dayOfWeek(), result.dayOfWeek());
        assertEquals(expectedResponseDTO.localdate(), result.localdate());
    }

    @Test
    void testUpdateSession_whenFailure_thenReturnResourceNotFoundException() {
        Long wkId = setUpWkSavedEntity.getId();
        WorkoutSessionCreateDTO toUpdateDTO = new WorkoutSessionCreateDTO(DayOfWeek.SATURDAY, LocalDate.now());
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(wkRepository.findByIdAndUser_Id(wkId, user.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            workoutSessionServiceImpl.updateSession(wkId, toUpdateDTO);
        });

        assertEquals("WORKOUT_SESSION_NOT_FOUND", thrown.getErrorCode().name());
        verify(authService).getAuthenticatedUserId();
        verify(wkRepository).findByIdAndUser_Id(wkId, user.getId());
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(authService, userRepository, wkRepository);
    }

    @Test
    void testDeleteSession_whenSucess (){
        Long wkId = setUpWkSavedEntity.getId();
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(wkRepository.findByIdAndUser_Id(wkId, user.getId())).thenReturn(Optional.of(setUpWkSavedEntity));
        doNothing().when(wkRepository).delete(setUpWkSavedEntity);

    }
}
