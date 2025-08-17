package br.com.wilgner.brotreinos.controller;

import br.com.wilgner.brotreinos.exception.ApiResponse;
import br.com.wilgner.brotreinos.model.dto.executiondto.ExecutionCreateDTO;
import br.com.wilgner.brotreinos.model.dto.executiondto.ExecutionResponseDTO;
import br.com.wilgner.brotreinos.service.ExecutionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workouts/{workoutSessionId}/executions")
public class ExerciseExecutionController {

    private final ExecutionService service;

    public ExerciseExecutionController(ExecutionService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExecutionResponseDTO>> createExecution(
            @PathVariable Long workoutSessionId,
            @RequestBody @Valid ExecutionCreateDTO dto) {

        ExecutionResponseDTO response = service.executionCreate(workoutSessionId, dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ExecutionResponseDTO>>> listByWorkoutSession(
            @PathVariable Long workoutSessionId) {

        List<ExecutionResponseDTO> response = service.listBySession(workoutSessionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    @GetMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<ExecutionResponseDTO>> getExecution(
            @PathVariable Long workoutSessionId,
            @PathVariable Long exerciseId) {

        ExecutionResponseDTO response = service.getById(exerciseId, workoutSessionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    @PutMapping("/{exerciseId}")
    public ResponseEntity<ApiResponse<ExecutionResponseDTO>> updateExecution(
            @PathVariable Long workoutSessionId,
            @PathVariable Long exerciseId,
            @RequestBody @Valid ExecutionCreateDTO dto) {

        ExecutionResponseDTO response = service.update(workoutSessionId, exerciseId, dto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(response));
    }

    @DeleteMapping("/{exerciseId}")
    public ResponseEntity<Void> deleteExecution(
            @PathVariable Long workoutSessionId,
            @PathVariable Long exerciseId) {

        service.delete(workoutSessionId, exerciseId);
        return ResponseEntity.noContent().build();
    }
}
