package br.com.wilgner.brotreinos.service;

import br.com.wilgner.brotreinos.exception.BusinessRuleException;
import br.com.wilgner.brotreinos.exception.ErrorCode;
import br.com.wilgner.brotreinos.exception.ResourceNotFoundException;
import br.com.wilgner.brotreinos.model.dto.trainingplandto.*;
import br.com.wilgner.brotreinos.model.entities.trainingplan.TrainingPlan;
import br.com.wilgner.brotreinos.model.entities.User;
import br.com.wilgner.brotreinos.model.repository.TrainingPlanRepository;
import br.com.wilgner.brotreinos.model.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TrainingPlanServiceImpl implements TrainingPlanService {

    private final TrainingPlanRepository trainingPlanRepository;
    private final UserRepository userRepository;
    private final TrainingMapper mapper;
    private final AuthService authService;

    public TrainingPlanServiceImpl(AuthService authService, TrainingMapper mapper, TrainingPlanRepository trainingPlanRepository, UserRepository userRepository) {
        this.mapper = mapper;
        this.trainingPlanRepository = trainingPlanRepository;
        this.userRepository = userRepository;
        this.authService = authService;
    }



    @Transactional
    public TrainingPlanResponseDTO createCompleteTrainingPlan(TrainingPlanCreateDTO trainingPlanCreateDTO) {
        Long userId = authService.getAuthenticatedUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessRuleException(ErrorCode.USER_NOT_FOUND));

        TrainingPlan plan = mapper.toEntity(trainingPlanCreateDTO, user);
        TrainingPlan savedPlan = trainingPlanRepository.save(plan);
        return mapper.toResponseDto(savedPlan);
    }

    public TrainingPlanResponseDTO getByName(String name) {
        Long userId = authService.getAuthenticatedUserId();
        TrainingPlan plan = trainingPlanRepository.findByUser_IdAndName(userId, name)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.TRAINING_PLAN_NOT_FOUND));

        return mapper.toResponseDto(plan);
    }

    @Transactional
    public TrainingPlanResponseDTO updateTrainingPlan(String name, TrainingPlanCreateDTO trainingPlanCreateDTO) {
        Long userId = authService.getAuthenticatedUserId();
        TrainingPlan plan = trainingPlanRepository.findByUser_IdAndName(userId, name)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.TRAINING_PLAN_NOT_FOUND));


        mapper.updateEntity(plan, trainingPlanCreateDTO);
        TrainingPlan updated = trainingPlanRepository.save(plan);
        return mapper.toResponseDto(updated);
    }
    @Transactional
    public void deleteTrainingPlan(Long id) {
        Long userId = authService.getAuthenticatedUserId();
        TrainingPlan plan = trainingPlanRepository.findByUser_IdAndId(userId, id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.TRAINING_PLAN_NOT_FOUND));
        trainingPlanRepository.delete(plan);
    }


}
