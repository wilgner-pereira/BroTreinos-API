package br.com.wilgner.brotreinos.service;

import br.com.wilgner.brotreinos.model.dto.exercisesdto.ExercisesCreateDTO;
import br.com.wilgner.brotreinos.model.dto.exercisesdto.ExercisesResponseDTO;

public interface ExercisesService {
    ExercisesResponseDTO createExercise(ExercisesCreateDTO userDto);
    ExercisesResponseDTO getExerciseByName(String name);
    ExercisesResponseDTO updateExercise(Long id, ExercisesCreateDTO exerDto);
}
