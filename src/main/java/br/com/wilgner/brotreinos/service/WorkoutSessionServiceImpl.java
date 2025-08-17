package br.com.wilgner.brotreinos.service;


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
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class WorkoutSessionServiceImpl implements WorkoutSessionService {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final UserRepository userRepository;
    private final WorkoutSessionMapper workoutSessionMapper;
    private final AuthService authService;

    public WorkoutSessionServiceImpl(AuthService authService, WorkoutSessionMapper workoutSessionMapper, WorkoutSessionRepository workoutSessionRepository, UserRepository userRepository) {
        this.workoutSessionRepository = workoutSessionRepository;
        this.userRepository = userRepository;
        this.workoutSessionMapper = workoutSessionMapper;
        this.authService = authService;
    }


    
    @Override
    @Transactional
    public WorkoutSessionResponseDTO createSession(WorkoutSessionCreateDTO  wksDto) {
        Long userId = authService.getAuthenticatedUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessRuleException(ErrorCode.USER_NOT_FOUND));


        WorkoutSession wk = workoutSessionMapper.toWorkout(wksDto);
        wk.setUser(user);
        WorkoutSession wksSaved = workoutSessionRepository.save(wk);
        return workoutSessionMapper.toWorkoutResponseDTO(wksSaved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WorkoutSessionResponseDTO> listAllSessions(){
        Long userId = authService.getAuthenticatedUserId();

        List<WorkoutSession> wk = workoutSessionRepository.findAllByUser_Id(userId);

        return wk.stream()
                .map(workoutSessionMapper::toWorkoutResponseDTO)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public WorkoutSessionResponseDTO getSessionById(Long id){
        Long userId = authService.getAuthenticatedUserId();

        WorkoutSession wk = workoutSessionRepository.findByIdAndUser_Id(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WORKOUT_SESSION_NOT_FOUND));

        return workoutSessionMapper.toWorkoutResponseDTO(wk);
    }

    @Override
    @Transactional
    public WorkoutSessionResponseDTO updateSession(Long id, WorkoutSessionCreateDTO wksDto) {
        Long userId = authService.getAuthenticatedUserId();
        WorkoutSession wks = workoutSessionRepository.findByIdAndUser_Id(id, userId).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WORKOUT_SESSION_NOT_FOUND));

        workoutSessionMapper.toEntityUpdate(wks, wksDto);
        workoutSessionRepository.save(wks);
        return workoutSessionMapper.toWorkoutResponseDTO(wks);

    }

    @Override
    @Transactional
    public void deleteSessionById(Long workId){
        Long userId = authService.getAuthenticatedUserId();
        WorkoutSession wks = workoutSessionRepository.findByIdAndUser_Id(workId, userId).orElseThrow(() -> new ResourceNotFoundException(ErrorCode.WORKOUT_SESSION_NOT_FOUND));


        workoutSessionRepository.delete(wks);

    }



}
