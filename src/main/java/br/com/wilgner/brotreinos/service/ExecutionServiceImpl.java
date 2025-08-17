package br.com.wilgner.brotreinos.service;


import br.com.wilgner.brotreinos.exception.ErrorCode;
import br.com.wilgner.brotreinos.exception.ResourceNotFoundException;
import br.com.wilgner.brotreinos.model.dto.executiondto.ExecutionCreateDTO;
import br.com.wilgner.brotreinos.model.dto.executiondto.ExecutionMapper;

import br.com.wilgner.brotreinos.model.dto.executiondto.ExecutionResponseDTO;
import br.com.wilgner.brotreinos.model.entities.workout.Execution;
import br.com.wilgner.brotreinos.model.entities.workout.Serie;
import br.com.wilgner.brotreinos.model.entities.workout.WorkoutSession;
import br.com.wilgner.brotreinos.model.repository.ExecutionRepository;
import br.com.wilgner.brotreinos.model.repository.WorkoutSessionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ExecutionServiceImpl implements ExecutionService {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final ExecutionMapper executionMapper;
    private final ExecutionRepository executionRepository;
    private final AuthService authService;


    public ExecutionServiceImpl(AuthService authService, WorkoutSessionRepository workoutSessionRepository, ExecutionMapper executionMapper, ExecutionRepository executionRepository){
        this.workoutSessionRepository = workoutSessionRepository;
        this.executionMapper = executionMapper;
        this.executionRepository = executionRepository;
        this.authService = authService;

    }


    @Override
    @Transactional
    public ExecutionResponseDTO executionCreate(Long workoutSessionId, ExecutionCreateDTO dto){
        Long userId = authService.getAuthenticatedUserId();


        WorkoutSession workoutSession = workoutSessionRepository.findByIdAndUser_Id(workoutSessionId, userId).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WORKOUT_SESSION_NOT_FOUND));

        Execution execution = executionMapper.toEntity(dto);
        execution.setWorkoutSession(workoutSession);

        Execution saved = executionRepository.save(execution);
        return executionMapper.toDto(saved);
    }



    @Override
    @Transactional(readOnly = true)
    public List<ExecutionResponseDTO> listBySession(Long workoutSessionId){
        Long userId = authService.getAuthenticatedUserId();

        return executionRepository.findByWorkoutSession_IdAndWorkoutSession_User_Id(workoutSessionId, userId).stream()
                .map(executionMapper::toDto)
                .toList();
    }



    @Override
    @Transactional(readOnly = true)
    public ExecutionResponseDTO getById(Long id, Long workoutSessionId) {
        Long userId = authService.getAuthenticatedUserId();

        Execution execution = executionRepository
                .findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(id, workoutSessionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EXECUTION_NOT_FOUND));

        return executionMapper.toDto(execution);
    }

    @Transactional
    public ExecutionResponseDTO update(Long workoutSessionId, Long executionId, ExecutionCreateDTO dto) {
        Long userId = authService.getAuthenticatedUserId();
        Execution execution = executionRepository
                .findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(executionId, workoutSessionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EXECUTION_NOT_FOUND));


        executionMapper.toEntityUpdate(execution, dto);
        Execution updated = executionRepository.save(execution);
        return executionMapper.toDto(updated);
    }

    @Override
    @Transactional
    public void delete(Long workoutSessionId, Long executionId) {
        Long userId = authService.getAuthenticatedUserId();
        Execution execution = executionRepository
                .findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(executionId, workoutSessionId, userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EXECUTION_NOT_FOUND));
        executionRepository.delete(execution);
    }
}



