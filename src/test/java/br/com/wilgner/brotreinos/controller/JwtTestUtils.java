//package br.com.wilgner.brotreinos.controller;
//
//import org.springframework.security.oauth2.jwt.JwtClaimsSet;
//import org.springframework.security.oauth2.jwt.JwtEncoder;
//import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
//
//import java.time.Instant;
//import java.util.Map;
//
//public class JwtTestUtils {
//
//    public static String generateToken(JwtEncoder jwtEncoder, Map<String, Object> claims) {
//
//        Instant now = Instant.now();
//
//        JwtClaimsSet.Builder claimsBuilder = JwtClaimsSet.builder()
//                .issuedAt(now)
//                .expiresAt(now.plusSeconds(3600));
//        claims.forEach(claimsBuilder::claim);
//        return jwtEncoder.encode(JwtEncoderParameters.from(claimsBuilder.build())).getTokenValue();
//
//    }
//
//}