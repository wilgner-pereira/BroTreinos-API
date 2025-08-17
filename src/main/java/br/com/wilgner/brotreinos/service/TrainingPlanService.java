package br.com.wilgner.brotreinos.service;

import br.com.wilgner.brotreinos.model.dto.trainingplandto.TrainingPlanCreateDTO;
import br.com.wilgner.brotreinos.model.dto.trainingplandto.TrainingPlanResponseDTO;

public interface TrainingPlanService {
    TrainingPlanResponseDTO createCompleteTrainingPlan (TrainingPlanCreateDTO trainingPlanCreateDTO);
    TrainingPlanResponseDTO getByName (String name);
    TrainingPlanResponseDTO updateTrainingPlan(String name, TrainingPlanCreateDTO trainingPlanCreateDTO);
    void deleteTrainingPlan (Long id);
}
