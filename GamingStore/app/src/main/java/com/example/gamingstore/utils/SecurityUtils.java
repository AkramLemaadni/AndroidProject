package com.example.gamingstore.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityUtils {

    public static String hashPassword(String password) {
        try {
            // Créer l'instance de hachage SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Hacher le mot de passe
            byte[] hashedBytes = digest.digest(password.getBytes());

            // Convertir en format hexadécimal lisible
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Erreur de hachage", e);
        }
    }

}