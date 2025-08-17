package br.com.wilgner.brotreinos.model.dto.trainingplandto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TrainingPlanCreateDTO(
        @NotBlank(message = "O nome do plano não pode estar vazio")String name,
        @Size(min = 1, message = "O plano deve ter pelo menos 1 dia de treino")List<TrainingDayCreateDTO> days) {
}
