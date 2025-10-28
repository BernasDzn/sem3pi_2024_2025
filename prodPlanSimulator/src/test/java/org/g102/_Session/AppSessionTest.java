package org.g102._Session;

import org.g102._ui.Bootstrap;
import org.g102._ui._AuthDomain.Password;
import org.g102._ui._AuthDomain.User;
import org.g102._ui._AuthDomain.UserRoles;
import org.g102._ui._Session.AppSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.*;

import static org.junit.jupiter.api.Assertions.*;

class AppSessionTest {

    @BeforeEach
    void init() {
        new Bootstrap().run();
    }

    User user;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        user = new User("user", "user@email.com", new Password("password"));
        AppSession.getInstance();
    }

    @Test
    void testAppSessionLoginAndLogout() throws NoSuchAlgorithmException {
        AppSession.getInstance().login("admin@this", "admin");
        assertTrue(AppSession.getInstance().isLogged());
        AppSession.getInstance().logout();
        assertFalse(AppSession.getInstance().isLogged());
    }

    @Test
    void testAppSessionIsAdmin() throws NoSuchAlgorithmException {
        AppSession.getInstance().login("pm@this", "manager");
        assertFalse(AppSession.getInstance().isAdmin());
        AppSession.getInstance().getUser().addRole(UserRoles.ADMINISTRATOR);
        assertTrue(AppSession.getInstance().isAdmin());
    }
}