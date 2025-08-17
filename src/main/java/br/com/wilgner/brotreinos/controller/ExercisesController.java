package br.com.wilgner.brotreinos.controller;

import br.com.wilgner.brotreinos.exception.ApiResponse;
import br.com.wilgner.brotreinos.model.dto.exercisesdto.ExercisesCreateDTO;
import br.com.wilgner.brotreinos.model.dto.exercisesdto.ExercisesResponseDTO;
import br.com.wilgner.brotreinos.service.ExercisesService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/exercicios")
public class ExercisesController {
    private final ExercisesService exercisesService;

    public ExercisesController(ExercisesService exercisesService) {
        this.exercisesService = exercisesService;
    }

    @PostMapping ()
    public ResponseEntity<ApiResponse<ExercisesResponseDTO>> createExercise(@Valid @RequestBody ExercisesCreateDTO exercisesCreateDto) {
        ExercisesResponseDTO exercisesResponseDto = exercisesService.createExercise(exercisesCreateDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(exercisesResponseDto));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<ExercisesResponseDTO>> getExerciseByName(@RequestParam String name) {
        ExercisesResponseDTO exercisesResponseDto = exercisesService.getExerciseByName(name);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(exercisesResponseDto));
    }

    @PutMapping ("/{id}")
    public ResponseEntity<ApiResponse<ExercisesResponseDTO>> update(@PathVariable Long id, @Valid @RequestBody ExercisesCreateDTO exercisesCreateDto) {
        ExercisesResponseDTO exercisesResponseDto =  exercisesService.updateExercise(id, exercisesCreateDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(exercisesResponseDto));
    }




}
