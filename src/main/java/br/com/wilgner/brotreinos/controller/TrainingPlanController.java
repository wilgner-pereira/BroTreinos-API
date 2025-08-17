package br.com.wilgner.brotreinos.controller;

import br.com.wilgner.brotreinos.exception.ApiResponse;
import br.com.wilgner.brotreinos.model.dto.trainingplandto.TrainingPlanCreateDTO;
import br.com.wilgner.brotreinos.model.dto.trainingplandto.TrainingPlanResponseDTO;
import br.com.wilgner.brotreinos.service.TrainingPlanService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("trainings")
public class TrainingPlanController {

    private final TrainingPlanService trainingPlanService;
    public TrainingPlanController(TrainingPlanService trainingPlanService){
        this.trainingPlanService = trainingPlanService;
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<TrainingPlanResponseDTO>> createCompleteTrainingPlan(@RequestBody @Valid TrainingPlanCreateDTO trainingPlanCreateDTO){
        TrainingPlanResponseDTO plan = trainingPlanService.createCompleteTrainingPlan(trainingPlanCreateDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(plan));
    }

    @GetMapping("/{name}")
    public ResponseEntity<ApiResponse<TrainingPlanResponseDTO>> getByName(@PathVariable String name){
        TrainingPlanResponseDTO plan = trainingPlanService.getByName(name);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(plan));
    }

    @PutMapping("/{name}")
    public ResponseEntity<ApiResponse<TrainingPlanResponseDTO>> updateTrainingPlan (@PathVariable String name, @RequestBody @Valid TrainingPlanCreateDTO trainingPlanCreateDTO){
        TrainingPlanResponseDTO plan = trainingPlanService.updateTrainingPlan(name, trainingPlanCreateDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success(plan));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTrainingPlan(@PathVariable Long id){
        trainingPlanService.deleteTrainingPlan(id);
        return  ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
