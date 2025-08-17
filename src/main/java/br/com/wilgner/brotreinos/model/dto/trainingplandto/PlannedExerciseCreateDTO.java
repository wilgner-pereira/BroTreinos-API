package br.com.wilgner.brotreinos.model.dto.trainingplandto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PlannedExerciseCreateDTO(
        @NotBlank(message = "O nome do exercício não pode estar vazio") String exerciseName,

        @NotNull(message = "O número de séries é obrigatório")
        @Min(value = 1, message = "O número de séries deve ser no mínimo 1")
        Integer series,

        @NotNull(message = "O número de repetições é obrigatório")
        @Min(value = 1, message = "O número de repetições deve ser no mínimo 1")
        Integer repetitions
) {}
