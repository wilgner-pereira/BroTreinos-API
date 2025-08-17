package br.com.wilgner.brotreinos.model.dto.workoutsession;

import br.com.wilgner.brotreinos.model.entities.workout.WorkoutSession;
import org.springframework.stereotype.Component;

@Component
public class WorkoutSessionMapper {

    public WorkoutSession toWorkout(WorkoutSessionCreateDTO workDto){
        WorkoutSession workoutSession = new WorkoutSession();
        workoutSession.setDayOfWeek(workDto.dayOfWeek());
        workoutSession.setWorkoutDate(workDto.localdate());
        return workoutSession;
    }

    public WorkoutSessionResponseDTO toWorkoutResponseDTO(WorkoutSession workoutSession){
        return new WorkoutSessionResponseDTO(
                workoutSession.getId(),
                workoutSession.getDayOfWeek(),
                workoutSession.getWorkoutDate()

        );
    }

    public void toEntityUpdate(WorkoutSession ex, WorkoutSessionCreateDTO workDto){
        ex.setDayOfWeek(workDto.dayOfWeek());
        ex.setWorkoutDate(workDto.localdate());
    }

}
