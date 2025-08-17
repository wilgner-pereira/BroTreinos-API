package br.com.wilgner.brotreinos.model.dto.executiondto;

import java.util.List;

public record ExecutionResponseDTO(Long Id, String exerciseName, List<SerieResponseDTO> series){
}
