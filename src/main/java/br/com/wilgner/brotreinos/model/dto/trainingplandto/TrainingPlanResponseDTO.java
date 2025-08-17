package br.com.wilgner.brotreinos.model.dto.trainingplandto;

import java.util.List;

public record TrainingPlanResponseDTO(Long id, String name, List<TrainingDayResponseDTO> days) {
}
