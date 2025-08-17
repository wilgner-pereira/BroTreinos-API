package br.com.wilgner.brotreinos.model.dto.userdto;

import java.time.Instant;

public record UserAuthLoginResponseDTO(String acessToken, Instant expiresIn) {
}
