package br.com.wilgner.brotreinos.model.dto.workoutsession;

import java.time.DayOfWeek;
import java.time.LocalDate;

public record WorkoutSessionResponseDTO (Long id, DayOfWeek dayOfWeek, LocalDate localdate) {
}
