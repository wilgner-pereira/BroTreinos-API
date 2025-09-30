package br.com.wilgner.brotreinos.model.dto.workoutsession;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


public record SerieCreateDTO(
        @NotNull(message = "O número da série é obrigatório")
        @Min(value = 1, message = "O número da série deve ser maior ou igual a 1")
        Integer numero,

        @NotNull(message = "As repetições são obrigatórias")
        @Min(value = 1, message = "As repetições devem ser maiores que zero")
        Integer repeticoes,

        @NotNull(message = "A carga é obrigatória")
        @DecimalMin(value = "0.0", inclusive = false, message = "A carga deve ser maior que zero")
        Double carga
) {}
