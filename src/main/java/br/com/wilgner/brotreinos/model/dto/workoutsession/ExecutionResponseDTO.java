package br.com.wilgner.brotreinos.model.dto.workoutsession;

import java.util.List;

// Record aninhado para cada execução
public record ExecutionResponseDTO(
        Long id,
        String exerciseName,
        String comment,
        List<SerieResponseDTO> series
) {}
