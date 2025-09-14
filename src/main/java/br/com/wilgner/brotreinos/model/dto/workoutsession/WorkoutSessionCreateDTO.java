package br.com.wilgner.brotreinos.model.dto.workoutsession;


import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;

public record WorkoutSessionCreateDTO (@NotNull DayOfWeek dayOfWeek, @NotNull LocalDate localdate){
}
