package br.com.wilgner.brotreinos.model.dto.userdto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserRegisterDTO(
        @NotBlank(message = "O campo 'nome' não pode estar em branco")
        @Size(min = 4, max = 50)
        String username,

        @NotBlank(message = "O campo 'senha' não pode estar em branco")
        @Size(min = 4, max = 50)
        String password) {
}
