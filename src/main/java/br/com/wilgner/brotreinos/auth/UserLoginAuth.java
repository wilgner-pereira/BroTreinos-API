package br.com.wilgner.brotreinos.auth;

import br.com.wilgner.brotreinos.exception.BusinessRuleException;
import br.com.wilgner.brotreinos.exception.ErrorCode;
import br.com.wilgner.brotreinos.model.dto.userdto.UserAuthLoginResponseDTO;
import br.com.wilgner.brotreinos.model.dto.userdto.UserLoginRequestDTO;
import br.com.wilgner.brotreinos.model.entities.User;
import br.com.wilgner.brotreinos.model.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
public class UserLoginAuth {

    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserLoginAuth(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder,  JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtEncoder = jwtEncoder;
    }

    @PostMapping("/token")
    public ResponseEntity<UserAuthLoginResponseDTO> login (@RequestBody UserLoginRequestDTO userLoginRequestDto) {
        // 1. Buscar o usuário no banco
        User user = userRepository.findByusername(userLoginRequestDto.username())
                .orElseThrow(() -> new BusinessRuleException(ErrorCode.UNAUTHORIZED_ACTION));

        // 2. Verificar se a senha é válida
        boolean isPasswordCorrect = bCryptPasswordEncoder.matches(
                userLoginRequestDto.password(),
                user.getPassword()
        );
        if(!isPasswordCorrect) {
            throw new BusinessRuleException(ErrorCode.UNAUTHORIZED_ACTION);
        }

        // 3. Gerar JWT
        Instant now = Instant.now();
        Instant expires = now.plusSeconds(3600);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("acad-api")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .subject(user.getId().toString())
                .claim("scope", "USER")
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new UserAuthLoginResponseDTO(token, expires));


    }


}
