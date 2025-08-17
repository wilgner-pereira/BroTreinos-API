package br.com.wilgner.brotreinos.model.repository;

import br.com.wilgner.brotreinos.model.entities.workout.Execution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExecutionRepository extends JpaRepository<Execution, Long> {
    List<Execution> findByWorkoutSession_IdAndWorkoutSession_User_Id(Long workoutSessionId, Long userId);
    Optional<Execution> findByIdAndWorkoutSession_IdAndWorkoutSession_User_Id(Long id, Long workoutSessionId, Long userId);
}
