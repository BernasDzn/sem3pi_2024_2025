package org.g102._AuthDomain;

import org.g102._ui._AuthDomain.Password;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

class PasswordTest {

    @Test
    void testEncryptedPasswordWorks() throws NoSuchAlgorithmException {
        String unencryptedPassword = "password";
        Password password = new Password(unencryptedPassword);
        assertTrue(password.checkPassword(unencryptedPassword));
    }

    @Test
    void testPasswordEncryption() throws NoSuchAlgorithmException {
        String unencryptedPassword = "password";
        Password password = new Password(unencryptedPassword);
        assertNotEquals(unencryptedPassword, password.getPassword());
    }

    @Test
    void testIncorrectPassword() throws NoSuchAlgorithmException {
        String unencryptedPassword = "password";
        Password password = new Password(unencryptedPassword);
        assertFalse(password.checkPassword("wrongpassword"));
    }
}