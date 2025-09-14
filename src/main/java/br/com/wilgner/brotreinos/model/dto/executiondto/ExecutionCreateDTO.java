package br.com.wilgner.brotreinos.model.dto.executiondto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ExecutionCreateDTO(
        @NotBlank(message = "O nome do exercicio não pode estar vazio")
        String exerciseName,
        @NotEmpty
        @Valid
        List<SerieCreateDTO> series){
}
