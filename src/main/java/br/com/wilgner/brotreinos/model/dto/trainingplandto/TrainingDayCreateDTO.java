package br.com.wilgner.brotreinos.model.dto.trainingplandto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.DayOfWeek;
import java.util.List;

public record TrainingDayCreateDTO(
        @NotBlank(message = "O nome não pode estar vazio")
        String name,

        DayOfWeek dayOfWeek,

        @NotEmpty(message = "O plano deve ter pelo menos 1 dia de treino")
        @Size(min = 1)
        @Valid
        List<PlannedExerciseCreateDTO> exercises) {
}
