package br.com.wilgner.brotreinos.service;

import br.com.wilgner.brotreinos.model.dto.userdto.UserRegisterDTO;
import br.com.wilgner.brotreinos.model.dto.userdto.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(UserRegisterDTO userDto);
}
