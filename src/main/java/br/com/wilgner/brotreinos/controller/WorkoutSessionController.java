package br.com.wilgner.brotreinos.controller;

import br.com.wilgner.brotreinos.exception.ApiResponse;
import br.com.wilgner.brotreinos.model.dto.workoutsession.WorkoutSessionCreateDTO;
import br.com.wilgner.brotreinos.model.dto.workoutsession.WorkoutSessionResponseDTO;
import br.com.wilgner.brotreinos.service.WorkoutSessionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workout")
public class WorkoutSessionController {

    private final WorkoutSessionService workoutSessionService;
    public  WorkoutSessionController(WorkoutSessionService workoutSessionService) {
        this.workoutSessionService = workoutSessionService;

    }

    @PostMapping()
    public ResponseEntity<ApiResponse<WorkoutSessionResponseDTO>> create(@RequestBody @Valid WorkoutSessionCreateDTO wksDto) {
        WorkoutSessionResponseDTO wksRDTO = workoutSessionService.createSession(wksDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(wksRDTO));
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<WorkoutSessionResponseDTO>>> getAllSessions(){
        List<WorkoutSessionResponseDTO> wksRDTO = workoutSessionService.listAllSessions();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(wksRDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkoutSessionResponseDTO>> getSessionById(@PathVariable Long id){
        WorkoutSessionResponseDTO wksRDTO = workoutSessionService.getSessionById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(wksRDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<WorkoutSessionResponseDTO>> updateWorkoutSession(@PathVariable Long id, @RequestBody @Valid WorkoutSessionCreateDTO wksDto){
        WorkoutSessionResponseDTO wksRDTO = workoutSessionService.updateSession(id, wksDto);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(wksRDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSessionById(@PathVariable Long id){
        workoutSessionService.deleteSessionById(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .build();
    }
}
