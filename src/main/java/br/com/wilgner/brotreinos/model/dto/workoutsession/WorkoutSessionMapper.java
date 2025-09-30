package br.com.wilgner.brotreinos.model.dto.workoutsession;

import br.com.wilgner.brotreinos.model.entities.workout.WorkoutSession;
import org.springframework.stereotype.Component;

@Component
public class WorkoutSessionMapper {

    public WorkoutSession toWorkout(WorkoutSessionCreateDTO workDto){
        WorkoutSession workoutSession = new WorkoutSession();
        workoutSession.setName(workDto.name());
        workoutSession.setWorkoutDate(workDto.workoutDate());
        workoutSession.setExecutions(workDto.executions().stream().map(wk -> wk));
        return workoutSession;
    }

    public WorkoutSessionResponseDTO toWorkoutResponseDTO(WorkoutSession workoutSession){
        return new WorkoutSessionResponseDTO(
                workoutSession.getId(),
                workoutSession.getName(),
                workoutSession.getWorkoutDate()
        );
    }

    public void toEntityUpdate(WorkoutSession ex, WorkoutSessionCreateDTO workDto){
        ex.setName(workDto.name());
        ex.setWorkoutDate(workDto.localdate());
    }

}
