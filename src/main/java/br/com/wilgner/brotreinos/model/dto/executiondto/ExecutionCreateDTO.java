package br.com.wilgner.brotreinos.model.dto.executiondto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ExecutionCreateDTO(
        @NotBlank(message = "O nome do exercicio não pode estar vazio")
        String exerciseName,
        @NotBlank(message = "As series não podem estar vazias")
        @Size(min = 1)
        List<SerieCreateDTO> series){
}
