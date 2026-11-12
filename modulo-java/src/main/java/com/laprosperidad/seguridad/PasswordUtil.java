package com.laprosperidad.seguridad;

import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

// Protege las contraseñas mediante PBKDF2.
public class PasswordUtil {

    private static final int ITERACIONES = 210000;
    private static final int TAMANO_CLAVE = 256;

    // Genera un hash con una sal aleatoria.
    public static String generarHash(String password) throws Exception {

        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        KeySpec spec = new PBEKeySpec(
            password.toCharArray(), salt, ITERACIONES, TAMANO_CLAVE
        );

        SecretKeyFactory factory =
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hash = factory.generateSecret(spec).getEncoded();

        return ITERACIONES + ":"
            + Base64.getEncoder().encodeToString(salt) + ":"
            + Base64.getEncoder().encodeToString(hash);
    }

    // Verifica una contraseña contra el hash almacenado.
    public static boolean verificarPassword(
            String password, String hashGuardado) throws Exception {

        String[] partes = hashGuardado.split(":");

        if (partes.length != 3) {
            return false;
        }

        int iteraciones = Integer.parseInt(partes[0]);
        byte[] salt = Base64.getDecoder().decode(partes[1]);
        byte[] hashOriginal = Base64.getDecoder().decode(partes[2]);

        KeySpec spec = new PBEKeySpec(
            password.toCharArray(),
            salt,
            iteraciones,
            hashOriginal.length * 8
        );

        SecretKeyFactory factory =
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

        byte[] hashNuevo = factory.generateSecret(spec).getEncoded();

        if (hashNuevo.length != hashOriginal.length) {
            return false;
        }

        int diferencia = 0;

        for (int i = 0; i < hashNuevo.length; i++) {
            diferencia |= hashNuevo[i] ^ hashOriginal[i];
        }

        return diferencia == 0;
    }
}
