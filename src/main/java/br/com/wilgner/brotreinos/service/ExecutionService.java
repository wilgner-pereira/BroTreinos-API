package br.com.wilgner.brotreinos.service;



import br.com.wilgner.brotreinos.model.dto.workoutsession.ExecutionCreateDTO;
import br.com.wilgner.brotreinos.model.dto.workoutsession.ExecutionResponseDTO;

import java.util.List;

public interface ExecutionService {
    ExecutionResponseDTO executionCreate(Long id, ExecutionCreateDTO dto);
    List<ExecutionResponseDTO> listBySession(Long workoutSessionId);
    ExecutionResponseDTO getById(Long id, Long workoutSessionId);
    ExecutionResponseDTO update(Long workoutSessionId, Long exerciseId, ExecutionCreateDTO dto);
    void delete(Long workoutSessionId, Long exerciseId);
}
