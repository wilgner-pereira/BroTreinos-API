package br.com.wilgner.brotreinos.service;

import br.com.wilgner.brotreinos.model.dto.executiondto.ExecutionCreateDTO;
import br.com.wilgner.brotreinos.model.dto.executiondto.ExecutionResponseDTO;

import java.util.List;

public interface ExecutionService {
    ExecutionResponseDTO executionCreate(Long id, ExecutionCreateDTO dto);
    List<ExecutionResponseDTO> listBySession(Long workoutSessionId);
    ExecutionResponseDTO getById(Long id, Long workoutSessionId);
    ExecutionResponseDTO update(Long workoutSessionId, Long exerciseId, ExecutionCreateDTO dto);
    void delete(Long workoutSessionId, Long exerciseId);
}
