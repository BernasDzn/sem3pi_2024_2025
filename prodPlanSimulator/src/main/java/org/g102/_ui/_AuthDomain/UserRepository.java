package org.g102._ui._AuthDomain;

import java.security.NoSuchAlgorithmException;
import java.util.*;

public class UserRepository {

    private static UserRepository instance;
    Set<User> users;

    public UserRepository() { users = new HashSet<User>(); }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public boolean addUser(User user) {
        return users.add(user);
    }

    public boolean removeUser(User user) {
        return users.remove(user);
    }

    public User isValidUser(String email, String password) throws NoSuchAlgorithmException {
        for (User u : users) {
            if (u.getEmail().equals(email) && u.getPassword().checkPassword(password)) {
                return u;
            }
        }
        return null;
    }

}