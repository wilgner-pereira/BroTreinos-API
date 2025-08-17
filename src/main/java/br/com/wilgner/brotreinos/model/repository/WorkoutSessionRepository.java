package br.com.wilgner.brotreinos.model.repository;

import br.com.wilgner.brotreinos.model.entities.User;
import br.com.wilgner.brotreinos.model.entities.workout.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {
    List<WorkoutSession> findAllByUser_Id(Long userId);
    Optional<WorkoutSession> findByIdAndUser_Id(Long id, Long userId);


}
