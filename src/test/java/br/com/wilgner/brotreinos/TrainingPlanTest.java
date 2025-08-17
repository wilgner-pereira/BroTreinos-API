package br.com.wilgner.brotreinos;

import br.com.wilgner.brotreinos.exception.BusinessRuleException;
import br.com.wilgner.brotreinos.exception.ResourceNotFoundException;
import br.com.wilgner.brotreinos.model.dto.trainingplandto.PlannedExerciseCreateDTO;
import br.com.wilgner.brotreinos.model.dto.trainingplandto.PlannedExerciseResponseDTO;
import br.com.wilgner.brotreinos.model.dto.trainingplandto.TrainingDayCreateDTO;
import br.com.wilgner.brotreinos.model.dto.trainingplandto.TrainingDayResponseDTO;
import br.com.wilgner.brotreinos.model.dto.trainingplandto.TrainingMapper;
import br.com.wilgner.brotreinos.model.dto.trainingplandto.TrainingPlanCreateDTO;
import br.com.wilgner.brotreinos.model.dto.trainingplandto.TrainingPlanResponseDTO;
import br.com.wilgner.brotreinos.model.entities.User;
import br.com.wilgner.brotreinos.model.entities.trainingplan.TrainingDay;
import br.com.wilgner.brotreinos.model.entities.trainingplan.TrainingPlan;
import br.com.wilgner.brotreinos.model.repository.TrainingPlanRepository;
import br.com.wilgner.brotreinos.model.repository.UserRepository;
import br.com.wilgner.brotreinos.service.AuthService;
import br.com.wilgner.brotreinos.service.TrainingPlanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrainingPlanTest {
    @Mock
    private TrainingPlanRepository trainingPlanRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TrainingMapper mapper;
    @Mock
    private AuthService authService;

    @InjectMocks
    private TrainingPlanServiceImpl trainingPlanService;

    private User user;
    private TrainingPlanCreateDTO setUpTrainingPlanCreateDTO;
    private TrainingPlan setUpTrainingPlanEntity;
    private TrainingPlanResponseDTO setUpTrainingPlanResponseDTO;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        // DTO de entrada para o serviço
        setUpTrainingPlanCreateDTO = new TrainingPlanCreateDTO("Treino ABC", List.of(new TrainingDayCreateDTO("Segunda", DayOfWeek.MONDAY, List.of(new PlannedExerciseCreateDTO("Supino", 4, 6)))));

        // Entidade que seria criada pelo mapper
        setUpTrainingPlanEntity = new TrainingPlan();
        setUpTrainingPlanEntity.setId(10L);
        setUpTrainingPlanEntity.setName("Treino ABC");
        setUpTrainingPlanEntity.setUser(user);
        TrainingDay trainingDay = new TrainingDay();
        trainingDay.setDayOfWeek(DayOfWeek.MONDAY);
        setUpTrainingPlanEntity.setDays(List.of(trainingDay));

        // DTO de resposta que o serviço deve retornar
        setUpTrainingPlanResponseDTO = new TrainingPlanResponseDTO(10L, "Treino ABC", List.of(new TrainingDayResponseDTO(1L, "Segunda", DayOfWeek.MONDAY, List.of(new PlannedExerciseResponseDTO(1L, "Supino", 4, 6)))));
    }

    @Test
    void testCreateCompleteTrainingPlan_whenSuccess() {
        // Configuração dos mocks (Arrange)
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(mapper.toEntity(setUpTrainingPlanCreateDTO, user)).thenReturn(setUpTrainingPlanEntity);
        when(trainingPlanRepository.save(setUpTrainingPlanEntity)).thenReturn(setUpTrainingPlanEntity);
        when(mapper.toResponseDto(setUpTrainingPlanEntity)).thenReturn(setUpTrainingPlanResponseDTO);

        // Execução do método a ser testado (Act)
        TrainingPlanResponseDTO result = trainingPlanService.createCompleteTrainingPlan(setUpTrainingPlanCreateDTO);

        // Verificação do resultado (Assert)
        assertEquals(setUpTrainingPlanResponseDTO.id(), result.id());
        assertEquals(setUpTrainingPlanResponseDTO.name(), result.name());
        assertEquals(setUpTrainingPlanResponseDTO.days().get(0).dayOfWeek(), result.days().get(0).dayOfWeek());
        assertEquals(setUpTrainingPlanResponseDTO.days().get(0).exercises().get(0).exerciseName(), result.days().get(0).exercises().get(0).exerciseName());

        // Verificação das interações (Verify)
        verify(authService).getAuthenticatedUserId();
        verify(userRepository).findById(user.getId());
        verify(mapper).toEntity(setUpTrainingPlanCreateDTO, user);
        verify(trainingPlanRepository).save(setUpTrainingPlanEntity);
        verify(mapper).toResponseDto(setUpTrainingPlanEntity);
    }

    @Test
    void testCreateCompleteTrainingPlan_whenFailure_thenReturnBusinessRuleException() {
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        BusinessRuleException thrown = assertThrows(BusinessRuleException.class, () ->
                trainingPlanService.createCompleteTrainingPlan(setUpTrainingPlanCreateDTO));

        assertEquals("USER_NOT_FOUND", thrown.getErrorCode().name());

        verify(authService).getAuthenticatedUserId();
        verify(userRepository).findById(user.getId());
        verifyNoMoreInteractions(trainingPlanRepository, mapper);
    }

    @Test
    void testGetByName_whenSuccess() {
        String name = "Treino ABC";
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(trainingPlanRepository.findByUser_IdAndName(user.getId(), name)).thenReturn(Optional.of(setUpTrainingPlanEntity));
        when(mapper.toResponseDto(setUpTrainingPlanEntity)).thenReturn(setUpTrainingPlanResponseDTO);

        TrainingPlanResponseDTO result = trainingPlanService.getByName(name);

        assertEquals(setUpTrainingPlanResponseDTO.id(), result.id());

        verify(authService).getAuthenticatedUserId();
        verify(trainingPlanRepository).findByUser_IdAndName(user.getId(), name);
        verify(mapper).toResponseDto(setUpTrainingPlanEntity);
        verifyNoMoreInteractions(trainingPlanRepository, mapper);
    }
    @Test
    void testGetByName_whenFailure_thenReturnResourceNotFoundException() {
        String name = "Treino Não existe";
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(trainingPlanRepository.findByUser_IdAndName(user.getId(), name)).thenReturn(Optional.empty());
        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () ->
                trainingPlanService.getByName(name));

        assertEquals("TRAINING_PLAN_NOT_FOUND",  thrown.getErrorCode().name());
        verify(authService).getAuthenticatedUserId();
        verify(trainingPlanRepository).findByUser_IdAndName(user.getId(), name);
        verifyNoMoreInteractions(trainingPlanRepository, mapper);
    }

    @Test
    void testUpdateTrainingPlan_whenSuccess() {
        String name = "Treino ABC";
        TrainingPlanCreateDTO toUpdateDTO = new TrainingPlanCreateDTO(
                "Treino Upper/Lower",
                List.of(new TrainingDayCreateDTO(
                        "Terça-Feira",
                        DayOfWeek.TUESDAY,
                        List.of(new PlannedExerciseCreateDTO("Agachamento", 5, 7)))
                )
        );
        TrainingPlanResponseDTO expectedResponseDTO = new TrainingPlanResponseDTO(
                10L,
                "Treino Upper/Lower",
                List.of(new TrainingDayResponseDTO(
                        1L,
                        "Terça-Feira",
                        DayOfWeek.TUESDAY,
                        List.of(new PlannedExerciseResponseDTO(1L, "Agachamento", 5, 7)))
                )
        );

        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(trainingPlanRepository.findByUser_IdAndName(user.getId(), name))
                .thenReturn(Optional.of(setUpTrainingPlanEntity));

        doAnswer(invocation -> {
            TrainingPlan entity = invocation.getArgument(0);
            TrainingPlanCreateDTO dto = invocation.getArgument(1);
            entity.setName(dto.name()); // só alteramos o nome, suficiente pro teste
            return null;
        }).when(mapper).updateEntity(any(TrainingPlan.class), any(TrainingPlanCreateDTO.class));
        when(trainingPlanRepository.save(setUpTrainingPlanEntity)).thenReturn(setUpTrainingPlanEntity);
        when(mapper.toResponseDto(setUpTrainingPlanEntity)).thenReturn(expectedResponseDTO);

        TrainingPlanResponseDTO result = trainingPlanService.updateTrainingPlan(name, toUpdateDTO);

        assertEquals(expectedResponseDTO, result);

        verify(authService).getAuthenticatedUserId();
        verify(trainingPlanRepository).findByUser_IdAndName(user.getId(), name);
        verify(mapper).updateEntity(setUpTrainingPlanEntity, toUpdateDTO);
        verify(trainingPlanRepository).save(setUpTrainingPlanEntity);
        verify(mapper).toResponseDto(setUpTrainingPlanEntity);
        verifyNoMoreInteractions(authService, trainingPlanRepository, mapper);
    }

    @Test
    void testUpdateTrainingPlan_whenFailure_thenReturnResourceNotFoundException() {
        TrainingPlanCreateDTO toUpdateDTO = new TrainingPlanCreateDTO(
                "Treino Upper/Lower",
                List.of(new TrainingDayCreateDTO(
                        "Terça-Feira",
                        DayOfWeek.TUESDAY,
                        List.of(new PlannedExerciseCreateDTO("Agachamento", 5, 7)))
                )
        );
        String name = "Não existe";
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(trainingPlanRepository.findByUser_IdAndName(user.getId(), name)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () ->
                trainingPlanService.updateTrainingPlan(name, toUpdateDTO));

        assertEquals("TRAINING_PLAN_NOT_FOUND",  thrown.getErrorCode().name());
        verify(authService).getAuthenticatedUserId();
        verify(trainingPlanRepository).findByUser_IdAndName(user.getId(), name);
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(trainingPlanRepository, authService);
    }

    @Test
    void testDeleteTrainingPlan_whenSuccess() {
        Long trainingPlanId = 10L;
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(trainingPlanRepository.findByUser_IdAndId(user.getId(), trainingPlanId)).thenReturn(Optional.of(setUpTrainingPlanEntity));
        doNothing().when(trainingPlanRepository).delete(setUpTrainingPlanEntity);

        trainingPlanService.deleteTrainingPlan(trainingPlanId);

        verify(authService).getAuthenticatedUserId();
        verify(trainingPlanRepository).findByUser_IdAndId(user.getId(), trainingPlanId);
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(authService, trainingPlanRepository);
    }

    public void deleteTrainingPlan_whenFailure_thenThrowResourceNotFoundException() {
        Long trainingPlanId = 10L;
        when(authService.getAuthenticatedUserId()).thenReturn(user.getId());
        when(trainingPlanRepository.findByUser_IdAndId(user.getId(), trainingPlanId)).thenReturn(Optional.empty());

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () ->
                trainingPlanService.deleteTrainingPlan(trainingPlanId));

        assertEquals("TRAINING_PLAN_NOT_FOUND",  thrown.getErrorCode().name());
        verify(authService).getAuthenticatedUserId();
        verify(trainingPlanRepository).findByUser_IdAndId(user.getId(), trainingPlanId);
        verifyNoInteractions(mapper);
        verifyNoMoreInteractions(authService, trainingPlanRepository);
    }

}