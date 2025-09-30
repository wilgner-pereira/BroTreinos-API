package br.com.wilgner.brotreinos.service;

import br.com.wilgner.brotreinos.model.entities.workout.WorkoutSession;
import br.com.wilgner.brotreinos.model.repository.ExecutionRepository;
import br.com.wilgner.brotreinos.model.repository.SerieRepository;
import br.com.wilgner.brotreinos.model.repository.TrainingPlanRepository;
import br.com.wilgner.brotreinos.model.repository.WorkoutSessionRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkoutService {

    private final WorkoutSessionRepository workoutSessionRepository;
    private final ExecutionRepository executionRepository;
    private final SerieRepository serieRepository;
    private final TrainingPlanRepository trainingPlanRepository;

    public WorkoutService(WorkoutSessionRepository workoutSessionRepository, ExecutionRepository executionRepository, SerieRepository serieRepository, TrainingPlanRepository trainingPlanRepository) {
        this.workoutSessionRepository = workoutSessionRepository;
        this.executionRepository = executionRepository;
        this.serieRepository = serieRepository;
        this.trainingPlanRepository = trainingPlanRepository;
    }


    public WorkoutSession createWorkoutSession() {}

}
