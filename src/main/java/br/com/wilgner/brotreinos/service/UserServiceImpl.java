package br.com.wilgner.brotreinos.service;

import br.com.wilgner.brotreinos.exception.BusinessRuleException;
import br.com.wilgner.brotreinos.exception.ErrorCode;
import br.com.wilgner.brotreinos.model.dto.userdto.UserRegisterDTO;
import br.com.wilgner.brotreinos.model.dto.userdto.UserResponseDTO;
import br.com.wilgner.brotreinos.model.entities.User;
import br.com.wilgner.brotreinos.model.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserServiceImpl(UserRepository userRepository,  BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    public UserResponseDTO createUser(UserRegisterDTO userDto) {
        if (userRepository.findByusername(userDto.username()).isPresent()) {
            throw new BusinessRuleException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User user = new User();
        user.setUsername(userDto.username());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.password()));
        userRepository.save(user);
        return new UserResponseDTO(user.getUsername());
    }

}
