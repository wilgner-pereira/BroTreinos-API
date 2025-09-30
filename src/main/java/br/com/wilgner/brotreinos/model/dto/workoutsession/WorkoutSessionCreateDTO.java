package br.com.wilgner.brotreinos.model.dto.workoutsession;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.util.List;

public record WorkoutSessionCreateDTO(
        @NotBlank(message = "O nome do treino não pode estar vazio")
        String name,

        @NotNull
        LocalDate workoutDate,

        @NotEmpty
        @Valid
        List<ExecutionCreateDTO> executions,

        Long previousSessionId,
        Long trainingPlanId
) {}
