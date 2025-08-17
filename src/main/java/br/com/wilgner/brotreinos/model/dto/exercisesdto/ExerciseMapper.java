package br.com.wilgner.brotreinos.model.dto.exercisesdto;

import br.com.wilgner.brotreinos.model.entities.Exercises;
import org.springframework.stereotype.Component;

@Component
public class ExerciseMapper {
    public Exercises toEntity(ExercisesCreateDTO exerDto) {
        Exercises exercises = new Exercises();
        exercises.setName(exerDto.name());
        exercises.setSeries(exerDto.series());
        exercises.setRepeticoes(exerDto.repeticoes());
        return exercises;
    }
    
    public ExercisesResponseDTO toDTO(Exercises exercises) {
        return new ExercisesResponseDTO(
                exercises.getName(),
                exercises.getSeries(),
                exercises.getRepeticoes()
        );
    }

    public void toEntityUpdate(Exercises ex, ExercisesCreateDTO exerDto) {
        ex.setName(exerDto.name());
        ex.setSeries(exerDto.series());
        ex.setRepeticoes(exerDto.repeticoes());
    }

}
