package br.com.wilgner.brotreinos.model.dto.executiondto;

import br.com.wilgner.brotreinos.model.entities.workout.Execution;
import br.com.wilgner.brotreinos.model.entities.workout.Serie;
import org.springframework.stereotype.Component;

@Component
public class ExecutionMapper {

    public Execution toEntity(ExecutionCreateDTO dto) {
        Execution execution = new Execution();
        execution.setExerciseName(dto.exerciseName());


        if (dto.series() != null) {
            dto.series().forEach(serieDto -> {
                Serie serie = new Serie();
                serie.setNumero(serieDto.numero());
                serie.setRepeticoes(serieDto.repeticoes());
                serie.setCarga(serieDto.carga());
                serie.setExerciseExecution(execution); // vínculo bidirecional
                execution.getSeries().add(serie);
            });
        }

        return execution;
    }

    public ExecutionResponseDTO toDto(Execution entity) {
        return new ExecutionResponseDTO(
                entity.getId(),
                entity.getExerciseName(),
                entity.getSeries().stream()
                        .map(serie -> new SerieResponseDTO(
                                serie.getId(),
                                serie.getNumero(),
                                serie.getRepeticoes(),
                                serie.getCarga()
                        ))
                        .toList()
        );
    }

    public void toEntityUpdate(Execution ex, ExecutionCreateDTO dto) {
        ex.setExerciseName(dto.exerciseName());
        ex.getSeries().clear();
        if (dto.series() != null) {
            dto.series().forEach(serieDto -> {
                Serie serie = new Serie();
                serie.setNumero(serieDto.numero());
                serie.setRepeticoes(serieDto.repeticoes());
                serie.setCarga(serieDto.carga());
                serie.setExerciseExecution(ex);
                ex.getSeries().add(serie);
            });
        }
    }
}
