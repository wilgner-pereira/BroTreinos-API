package br.com.wilgner.brotreinos.service;

import br.com.wilgner.brotreinos.exception.BusinessRuleException;
import br.com.wilgner.brotreinos.exception.ErrorCode;
import br.com.wilgner.brotreinos.model.dto.userdto.UserRegisterDTO;
import br.com.wilgner.brotreinos.model.dto.userdto.UserResponseDTO;
import br.com.wilgner.brotreinos.model.entities.Role;
import br.com.wilgner.brotreinos.model.entities.User;
import br.com.wilgner.brotreinos.model.repository.RoleRepository;
import br.com.wilgner.brotreinos.model.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final RoleRepository roleRepository;
    public UserServiceImpl(UserRepository userRepository,  BCryptPasswordEncoder bCryptPasswordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.roleRepository = roleRepository;
    }
    
    public UserResponseDTO createUser(UserRegisterDTO userDto) {
        if (userRepository.findByUsername(userDto.username()).isPresent()) {
            throw new BusinessRuleException(ErrorCode.USER_ALREADY_EXISTS);
        }
        User user = new User();
        user.setUsername(userDto.username());
        user.setPassword(bCryptPasswordEncoder.encode(userDto.password()));
        Role userRole = roleRepository.findByName("ROLE_USER")
                        .orElseThrow(() -> new RuntimeException("Role not found"));
        user.setRoles(Set.of(userRole));
        userRepository.save(user);
        return new UserResponseDTO(user.getUsername());
    }

}
