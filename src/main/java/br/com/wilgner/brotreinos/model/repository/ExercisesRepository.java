package br.com.wilgner.brotreinos.model.repository;

import br.com.wilgner.brotreinos.model.entities.Exercises;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExercisesRepository extends JpaRepository<Exercises, Long> {
    Optional<Exercises> findByName(String name);
    Optional<Exercises> findByUser_IdAndName(Long userId, String name);
    Optional<Exercises> findByUser_IdAndId(Long userId, Long exId);
    List<Exercises> findByUser_Id(Long userId);


}
