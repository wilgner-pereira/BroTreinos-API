package br.com.wilgner.brotreinos.model.dto.exercisesdto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ExercisesCreateDTO(
        @NotBlank(message = "O nome não pode estar vazio")
        @Size(min = 1, max = 50)
        String name,
        @NotNull
        @Min(1)
        Integer series,
        @NotNull
        @Min(1)
        Integer repeticoes) {
}
