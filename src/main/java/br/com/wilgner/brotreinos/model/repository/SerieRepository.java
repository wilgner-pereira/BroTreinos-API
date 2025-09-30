package br.com.wilgner.brotreinos.model.repository;

import br.com.wilgner.brotreinos.model.entities.workout.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByIdAndUser_Id(Long id, Long userId);
}
