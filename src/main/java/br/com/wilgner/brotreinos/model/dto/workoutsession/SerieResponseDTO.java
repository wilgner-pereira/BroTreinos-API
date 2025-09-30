package br.com.wilgner.brotreinos.model.dto.workoutsession;

public record SerieResponseDTO(

        Long id,
        Integer numero,
        Integer repeticoes,
        Double carga
) {}
