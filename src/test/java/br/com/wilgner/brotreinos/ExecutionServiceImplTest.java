package br.com.wilgner.brotreinos;

import br.com.wilgner.brotreinos.exception.ResourceNotFoundException;
import br.com.wilgner.brotreinos.model.dto.executiondto.ExecutionCreateDTO;
import br.com.wilgner.brotreinos.model.dto.executiondto.ExecutionMapper;
import br.com.wilgner.brotreinos.model.dto.executiondto.ExecutionResponseDTO;
import br.com.wilgner.brotreinos.model.dto.executiondto.SerieCreateDTO;
import br.com.wilgner.brotreinos.model.entities.workout.Execution;
import br.com.wilgner.brotreinos.model.entities.workout.WorkoutSession;
import br.com.wilgner.brotreinos.model.repository.ExecutionRepository;
import br.com.wilgner.brotreinos.model.repository.WorkoutSessionRepository;
import br.com.wilgner.brotreinos.service.AuthService;
import br.com.wilgner.brotreinos.service.ExecutionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ExecutionServiceImplTest {

    @Mock
    private AuthService authService;
    @Mock
    private WorkoutSessionRepository workoutSessionRepository;
    @Mock
    private ExecutionMapper mapper;
    @Mock
    private ExecutionRepository executionRepository;

    @InjectMocks
    private ExecutionServiceImpl executionService;

    private ExecutionCreateDTO executionCreateDTO;
    private ExecutionResponseDTO executionResponseDTO;
    private WorkoutSession workoutSession;
    private Execution executionEntity;

    @BeforeEach
    void seUp(){
        executionCreateDTO = new ExecutionCreateDTO(
                "Supino",
                List.of(new SerieCreateDTO(1, 10, 50.0))
        );
        workoutSession = new WorkoutSession();
        workoutSession.setId(1L);


        executionEntity = new Execution();
        executionEntity.setId(100L);
        executionEntity.setWorkoutSession(workoutSession);

        executionResponseDTO = new ExecutionResponseDTO(
                100L, "Supino", List.of()
        );
    }

    @Test
    void executionCreate_whenWorkoutSessionExists_thenReturnResponseDTO(){
        Long userId = 10L;
        when(authService.getAuthenticatedUserId()).thenReturn(userId);
        when(workoutSessionRepository.findByIdAndUser_Id(1L, userId))
                .thenReturn(Optional.of(workoutSession));
        when(mapper.toEntity(executionCreateDTO)).thenReturn(executionEntity);
        when(executionRepository.save(executionEntity)).thenReturn(executionEntity);
        when(mapper.toDto(executionEntity)).thenReturn(executionResponseDTO);

        ExecutionResponseDTO result = executionService.executionCreate(1L, executionCreateDTO);

        assertNotNull(result);
        assertEquals("Supino", result.exerciseName());
        assertEquals(100L, result.Id());

        verify(authService).getAuthenticatedUserId();
        verify(workoutSessionRepository).findByIdAndUser_Id(1L, userId);
        verify(mapper).toEntity(executionCreateDTO);
        verify(executionRepository).save(executionEntity);
        verify(mapper).toDto(executionEntity);

    }

    @Test
    void executionCreate_whenWorkoutSessionNotFound_thenThrowResourceNotFoundException(){
        Long userId = 10L;

        when(authService.getAuthenticatedUserId()).thenReturn(userId);
        when(workoutSessionRepository.findByIdAndUser_Id(1L, userId))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                executionService.executionCreate(1L, executionCreateDTO)
        );
        verify(workoutSessionRepository).findByIdAndUser_Id(1L, userId);
        verify(mapper, never()).toEntity(any());
        verify(executionRepository, never()).save(any());
    }

    @Test
    void listBySession_whenWorkoutSessionsAndUserExists_thenReturnResponseDTO(){
        Long userId = 10L;
        when(authService.getAuthenticatedUserId()).thenReturn(userId);
        when(executionRepository.findByWorkoutSession_IdAndWorkoutSession_User_Id(1L, userId)).thenReturn(List.of(executionEntity));
        when(mapper.toDto(executionEntity)).thenReturn(executionResponseDTO);

        List<ExecutionResponseDTO> result = executionService.listBySession(1L);

        assertEquals(1, result.size());
        assertEquals(executionResponseDTO, result.get(0));

        verify(authService).getAuthenticatedUserId();
        verify(executionRepository).findByWorkoutSession_IdAndWorkoutSession_User_Id(1L, userId);
        verify(mapper).toDto(executionEntity);
        verifyNoMoreInteractions(authService, executionRepository, mapper);
    }

    @Test
    void listBySession_whenNoExecutionsFound_thenReturnEmptyList(){
        Long userId = 10L;
        when(authService.getAuthenticatedUserId()).thenReturn(userId);
        when(executionRepository.findByWorkoutSession_IdAndWorkoutSession_User_Id(1L, userId))
                .thenReturn(List.of());

        assertTrue(executionService.listBySession(1L).isEmpty());

        verify(authService).getAuthenticatedUserId();
        verify(executionRepository).findByWorkoutSession_IdAndWorkoutSession_User_Id(1L, userId);
        verifyNoMoreInteractions(authService, executionRepository, mapper);
    }

    @Test
    void getById_whenWorkoutSessionExists_thenReturnResponseDTO() {
        Long userId = 10L;
        when(authService.getAuthenticatedUserId()).thenReturn(userId);
        when(executionRepository.findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(
                executionEntity.getId(),
                workoutSession.getId(),
                userId
        )).thenReturn(Optional.of(executionEntity));
        when(mapper.toDto(executionEntity)).thenReturn(executionResponseDTO);

        ExecutionResponseDTO dto = executionService.getById(executionEntity.getId(), workoutSession.getId());

        assertNotNull(dto);
        assertEquals(executionResponseDTO, dto);

        verify(authService).getAuthenticatedUserId();
        verify(executionRepository).findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(
                executionEntity.getId(),
                workoutSession.getId(),
                userId
        );
        verify(mapper).toDto(executionEntity);
        verifyNoMoreInteractions(authService, executionRepository, mapper);
    }

    @Test
    void getById_whenWorkoutSessionNotExists_thenThrowResourceNotFoundException(){
        Long userId = 10L;
        when(authService.getAuthenticatedUserId()).thenReturn(userId);
        when(executionRepository.findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(
                executionEntity.getId(),
                workoutSession.getId(),
                userId)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () ->{
            executionService.getById(executionEntity.getId(), workoutSession.getId());
        });
        assertEquals("EXECUTION_NOT_FOUND", thrown.getErrorCode().name());
        assertNotNull(thrown.getMessage());

        verify(authService).getAuthenticatedUserId();
        verify(executionRepository).findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(executionEntity.getId(),
                workoutSession.getId(),
                userId);
        verify(mapper, never()).toDto(executionEntity);
        verifyNoMoreInteractions(authService, executionRepository, mapper);
    }

    @Test
    void updateWhenExecutionExists_thenReturnResponseDTO() {
        Long userId = 10L;
        when(authService.getAuthenticatedUserId()).thenReturn(userId);
        when(executionRepository.findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(executionEntity.getId(),
                workoutSession.getId(),
                userId)).thenReturn(Optional.of(executionEntity));
        doNothing().when(mapper).toEntityUpdate(executionEntity, executionCreateDTO);
        when(executionRepository.save(executionEntity)).thenReturn(executionEntity);
        when(mapper.toDto(executionEntity)).thenReturn(executionResponseDTO);

        ExecutionResponseDTO result = executionService.update(workoutSession.getId(), executionEntity.getId(), executionCreateDTO);

        assertNotNull(result);
        assertEquals(executionResponseDTO, result);
        verify(authService).getAuthenticatedUserId();
        verify(executionRepository).findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(executionEntity.getId(),
                workoutSession.getId(),
                userId);
        verify(mapper).toEntityUpdate(executionEntity, executionCreateDTO);
        verify(executionRepository).save(executionEntity);
        verify(mapper).toDto(executionEntity);
        verifyNoMoreInteractions(authService, executionRepository, mapper);
    }

    @Test
    void updateWhenExecutionNotExists_thenThrowResourceNotFoundException(){
        Long userId = 10L;
        when(authService.getAuthenticatedUserId()).thenReturn(userId);
        when(executionRepository.findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(executionEntity.getId(),
                workoutSession.getId(),
                userId)).thenReturn(Optional.empty());
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () ->{
            executionService.update(workoutSession.getId(), executionEntity.getId(), executionCreateDTO);
        });

        assertEquals("EXECUTION_NOT_FOUND", thrown.getErrorCode().name());
        assertNotNull(thrown.getMessage());
        verify(authService).getAuthenticatedUserId();
        verify(executionRepository).findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(executionEntity.getId(),
                workoutSession.getId(),
                userId);
        verifyNoMoreInteractions(authService, executionRepository, mapper);
    }

    @Test
    void delete_whenExecutionExists_thendSucceed() {
        Long userId = 10L;
        when(authService.getAuthenticatedUserId()).thenReturn(userId);
        when(executionRepository
                .findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(executionEntity.getId(), workoutSession.getId(), userId))
                .thenReturn(Optional.of(executionEntity));
        doNothing().when(executionRepository).delete(executionEntity);

        executionService.delete(workoutSession.getId(), executionEntity.getId());

        verify(authService).getAuthenticatedUserId();
        verify(executionRepository).findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(executionEntity.getId(), workoutSession.getId(), userId);
        verify(executionRepository).delete(executionEntity);
        verifyNoMoreInteractions(authService, executionRepository, mapper);
    }

    @Test
    void deleteWhenExecutionNotExists_thenThrowResourceNotFoundException(){
        Long userId = 10L;
        when(authService.getAuthenticatedUserId()).thenReturn(userId);
        when(executionRepository
                .findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(executionEntity.getId(), workoutSession.getId(), userId))
                .thenReturn(Optional.empty());
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () ->{
            executionService.delete(workoutSession.getId(), executionEntity.getId());
        });
        assertEquals("EXECUTION_NOT_FOUND", thrown.getErrorCode().name());
        assertNotNull(thrown.getMessage());
        verify(authService).getAuthenticatedUserId();
        verify(executionRepository).findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(executionEntity.getId(), workoutSession.getId(), userId);
        verifyNoMoreInteractions(authService, executionRepository, mapper);
    }



}
