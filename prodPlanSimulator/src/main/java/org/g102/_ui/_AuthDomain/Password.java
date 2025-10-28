package org.g102._ui._AuthDomain;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class Password {

    private String password;
    private byte[] salt;

    public Password(String password) throws NoSuchAlgorithmException {
        this.salt = generateSalt();
        this.password = encryptPassword(password, this.salt);
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws NoSuchAlgorithmException {
        this.password = encryptPassword(password, this.salt);
    }

    public static String encryptPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }

    public boolean checkPassword(String inputPassword) throws NoSuchAlgorithmException {
        String encryptedInputPassword = encryptPassword(inputPassword, this.salt);
        return this.password.equals(encryptedInputPassword);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Password && ((Password) obj).getPassword().equals(password);
    }

}