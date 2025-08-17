package br.com.wilgner.brotreinos.model.dto.userdto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDTO(
        @NotBlank String username,
        @NotBlank String password) {
}
