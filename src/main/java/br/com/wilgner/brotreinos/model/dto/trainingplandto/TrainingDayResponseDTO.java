package br.com.wilgner.brotreinos.model.dto.trainingplandto;

import java.time.DayOfWeek;
import java.util.List;

public record TrainingDayResponseDTO (Long id, String name, DayOfWeek dayOfWeek, List<PlannedExerciseResponseDTO> exercises){
}
