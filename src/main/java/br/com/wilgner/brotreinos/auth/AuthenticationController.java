package br.com.wilgner.brotreinos.auth;

import br.com.wilgner.brotreinos.model.dto.userdto.UserAuthLoginResponseDTO;
import br.com.wilgner.brotreinos.model.dto.userdto.UserLoginRequestDTO;
import br.com.wilgner.brotreinos.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
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
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final JwtEncoder jwtEncoder;
    public AuthenticationController(AuthenticationManager authenticationManager, JwtEncoder jwtEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtEncoder = jwtEncoder;
    }
    @PostMapping("/login")
    public ResponseEntity<UserAuthLoginResponseDTO> login(@RequestBody @Valid UserLoginRequestDTO userLoginRequestDTO) {

        var authenticationToken = new UsernamePasswordAuthenticationToken(userLoginRequestDTO.username(), userLoginRequestDTO.password());
        var authentication = authenticationManager.authenticate(authenticationToken);

        var userDetails = (CustomUserDetails) authentication.getPrincipal();
        Instant now =  Instant.now();
        Instant expires = now.plusSeconds(3600);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("brotreinos-api")
                .issuedAt(now)
                .expiresAt(expires)
                .subject(userDetails.getId().toString())
                .claim("authorities", userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .build();

        String token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new UserAuthLoginResponseDTO(token, expires));
    }
}
