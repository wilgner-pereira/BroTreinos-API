package br.com.wilgner.brotreinos.model.repository;

import br.com.wilgner.brotreinos.model.entities.trainingplan.TrainingPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlan,Long> {

    Optional<TrainingPlan> findByUser_IdAndName(Long userId, String name);
    Optional<TrainingPlan> findByUser_IdAndId(Long userId, Long planId);
}
