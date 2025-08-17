package br.com.wilgner.brotreinos.service;

import br.com.wilgner.brotreinos.exception.BusinessRuleException;
import br.com.wilgner.brotreinos.exception.ErrorCode;
import br.com.wilgner.brotreinos.exception.ResourceNotFoundException;
import br.com.wilgner.brotreinos.model.dto.exercisesdto.ExerciseMapper;
import br.com.wilgner.brotreinos.model.dto.exercisesdto.ExercisesCreateDTO;
import br.com.wilgner.brotreinos.model.dto.exercisesdto.ExercisesResponseDTO;
import br.com.wilgner.brotreinos.model.entities.Exercises;
import br.com.wilgner.brotreinos.model.entities.User;
import br.com.wilgner.brotreinos.model.repository.ExercisesRepository;
import br.com.wilgner.brotreinos.model.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ExercisesServiceImpl implements ExercisesService {

    private final ExercisesRepository exercisesRepository;
    private final ExerciseMapper exerciseMapper;
    private final UserRepository userRepository;
    private final AuthService authService;

    public ExercisesServiceImpl(AuthService authService, UserRepository userRepository, ExercisesRepository exercisesRepository,  ExerciseMapper exerciseMapper) {
        this.exercisesRepository = exercisesRepository;
        this.exerciseMapper = exerciseMapper;
        this.userRepository = userRepository;
        this.authService = authService;
    }


    @Override
    public ExercisesResponseDTO getExerciseByName(String name){
        Long userId = authService.getAuthenticatedUserId();

        Exercises ex = exercisesRepository.findByUser_IdAndName(userId, name).orElseThrow(() ->
                new ResourceNotFoundException(ErrorCode.EXERCISE_NOT_FOUND));
        return exerciseMapper.toDTO(ex);
    }


    @Override
    @Transactional
    public ExercisesResponseDTO createExercise(ExercisesCreateDTO exerDto) {
        Long userId = authService.getAuthenticatedUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessRuleException(ErrorCode.USER_NOT_FOUND));


        exercisesRepository.findByUser_IdAndName(userId, exerDto.name())
                .ifPresent(e -> {
                    throw new BusinessRuleException(ErrorCode.EXERCISE_ALREADY_EXISTS);
                });

        Exercises exercises = exerciseMapper.toEntity(exerDto);
        exercises.setUser(user);
        Exercises saved = exercisesRepository.save(exercises);
        return exerciseMapper.toDTO(saved);
    }


    @Override
    @Transactional
    public ExercisesResponseDTO updateExercise(Long id, ExercisesCreateDTO exerDto) {
        Long userId = authService.getAuthenticatedUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessRuleException(ErrorCode.USER_NOT_FOUND));
        Exercises ex = exercisesRepository.findByUser_IdAndId(userId, id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorCode.EXERCISE_NOT_FOUND));
        // Verifica se existe outro exercício com o mesmo nome (para o mesmo usuário)
        exercisesRepository.findByUser_IdAndName(userId, exerDto.name()).ifPresent(existing -> {
            if (!existing.getId().equals(id)) { // Certifica que não é o próprio exercício
                throw new BusinessRuleException(ErrorCode.EXERCISE_ALREADY_EXISTS);
            }
        });

        // Atualiza os dados
        exerciseMapper.toEntityUpdate(ex, exerDto);
        exercisesRepository.save(ex);
        return exerciseMapper.toDTO(ex);
    }



}
