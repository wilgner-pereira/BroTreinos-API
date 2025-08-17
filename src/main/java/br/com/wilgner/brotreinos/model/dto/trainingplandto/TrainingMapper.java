package br.com.wilgner.brotreinos.model.dto.trainingplandto;

import br.com.wilgner.brotreinos.model.entities.trainingplan.PlannedExercise;
import br.com.wilgner.brotreinos.model.entities.trainingplan.TrainingDay;
import br.com.wilgner.brotreinos.model.entities.trainingplan.TrainingPlan;
import br.com.wilgner.brotreinos.model.entities.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TrainingMapper {

    public TrainingPlan toEntity(TrainingPlanCreateDTO dto, User user) {
        TrainingPlan plan = new TrainingPlan();
        plan.setName(dto.name());
        plan.setUser(user);
        plan.setDays(new ArrayList<>());

        if(dto.days() != null) {
            for(TrainingDayCreateDTO dayDTO : dto.days()) {
                TrainingDay day = new TrainingDay();
                day.setName(dayDTO.name());
                day.setDayOfWeek(dayDTO.dayOfWeek());
                day.setTrainingPlan(plan);
                day.setExercises(new ArrayList<>());

                if(dayDTO.exercises() != null) {
                    for(PlannedExerciseCreateDTO exDTO : dayDTO.exercises()) {
                        PlannedExercise ex = new PlannedExercise();
                        ex.setExerciseName(exDTO.exerciseName());
                        ex.setSeries(exDTO.series());
                        ex.setRepetitions(exDTO.repetitions());
                        ex.setTrainingDay(day);
                        day.getExercises().add(ex);
                    }
                }
                plan.getDays().add(day);
            }
        }
        return plan;

    }

    public void updateEntity(TrainingPlan existingPlan, TrainingPlanCreateDTO dto) {
        existingPlan.setName(dto.name());
        existingPlan.getDays().clear();

        if(dto.days() != null) {
            for(TrainingDayCreateDTO dayDTO : dto.days()) {
                TrainingDay day = new TrainingDay();
                day.setName(dayDTO.name());
                day.setDayOfWeek(dayDTO.dayOfWeek());
                day.setTrainingPlan(existingPlan);
                day.setExercises(new ArrayList<>());

                if(dayDTO.exercises() != null) {
                    for(PlannedExerciseCreateDTO exDTO : dayDTO.exercises()) {
                        PlannedExercise ex = new PlannedExercise();
                        ex.setExerciseName(exDTO.exerciseName());
                        ex.setSeries(exDTO.series());
                        ex.setRepetitions(exDTO.repetitions());
                        ex.setTrainingDay(day);
                        day.getExercises().add(ex);
                    }
                }

                existingPlan.getDays().add(day);
            }
        }
    }


    public TrainingPlanCreateDTO toDto(TrainingPlan entity) {
        List<TrainingDayCreateDTO> dayDTOs = new ArrayList<>();

        for(TrainingDay day : entity.getDays()) {
            List<PlannedExerciseCreateDTO> exDTOs = new ArrayList<>();

            for (PlannedExercise ex : day.getExercises()) {
                exDTOs.add(new PlannedExerciseCreateDTO(
                        ex.getExerciseName(),
                        ex.getSeries(),
                        ex.getRepetitions()));
            }
            dayDTOs.add(new TrainingDayCreateDTO(
                    day.getName(),
                    day.getDayOfWeek(),
                    exDTOs));
        }
        return new TrainingPlanCreateDTO(entity.getName(), dayDTOs);
    }

    public TrainingPlanResponseDTO toResponseDto(TrainingPlan entity) {
        List<TrainingDayResponseDTO> dayDTOs = new ArrayList<>();

        for(TrainingDay day : entity.getDays()) {
            List<PlannedExerciseResponseDTO> exDTOs = new ArrayList<>();

            for (PlannedExercise ex : day.getExercises()) {
                exDTOs.add(new PlannedExerciseResponseDTO(
                        ex.getId(),
                        ex.getExerciseName(),
                        ex.getSeries(),
                        ex.getRepetitions()));
            }
            dayDTOs.add(new TrainingDayResponseDTO(
                    day.getId(),
                    day.getName(),
                    day.getDayOfWeek(),
                    exDTOs));
        }
        return new TrainingPlanResponseDTO(entity.getId(), entity.getName(), dayDTOs);
    }
}
