package br.com.wilgner.brotreinos.service;

import br.com.wilgner.brotreinos.model.dto.workoutsession.WorkoutSessionCreateDTO;
import br.com.wilgner.brotreinos.model.dto.workoutsession.WorkoutSessionResponseDTO;

import java.util.List;

public interface WorkoutSessionService {

    WorkoutSessionResponseDTO createSession (WorkoutSessionCreateDTO workoutSessionCreateDTO);
    List<WorkoutSessionResponseDTO> listAllSessions();
    WorkoutSessionResponseDTO getSessionById(Long id);
    void deleteSessionById(Long id);
    WorkoutSessionResponseDTO updateSession(Long id, WorkoutSessionCreateDTO workoutSessionCreateDTO);
}
