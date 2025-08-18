package br.com.wilgner.brotreinos.config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PemUtils {

    public static RSAPrivateKey readPrivateKey(String path) throws Exception {
        String keyContent = readKeyContent(path);
        keyContent = keyContent
                .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(keyContent));
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public static RSAPublicKey readPublicKey(String path) throws Exception {
        String keyContent = readKeyContent(path);
        keyContent = keyContent
                .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(keyContent));
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    private static String readKeyContent(String path) throws Exception {
        String keyContent;
        if (path.startsWith("classpath:")) {
            InputStream is = PemUtils.class.getClassLoader().getResourceAsStream(path.substring(10));
            if (is == null) throw new RuntimeException("Arquivo não encontrado: " + path);
            keyContent = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } else if (path.startsWith("file:")) {
            keyContent = new String(Files.readAllBytes(Paths.get(path.substring(5))), StandardCharsets.UTF_8);
        } else {
            throw new IllegalArgumentException("Path inválido: " + path);
        }
        return keyContent;
    }
}
