package br.com.wilgner.brotreinos.model.dto.workoutsession;

import java.time.LocalDate;
import java.util.List;

public record WorkoutSessionResponseDTO(

        Long id,
        String name,
        LocalDate workoutDate,
        List<ExecutionResponseDTO> executions

) {}
