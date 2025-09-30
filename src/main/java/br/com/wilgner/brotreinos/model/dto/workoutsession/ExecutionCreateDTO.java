package br.com.wilgner.brotreinos.model.dto.workoutsession;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record ExecutionCreateDTO(
        @NotBlank(message = "O nome da execução não pode estar vazio")
        String exerciseName,

        String comment,

        @NotEmpty
        @Valid
        List<SerieCreateDTO> series
) {}
