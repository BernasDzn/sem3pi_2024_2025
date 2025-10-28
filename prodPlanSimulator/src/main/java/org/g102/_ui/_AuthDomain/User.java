package org.g102._ui._AuthDomain;

import java.util.*;

public class User {

    private String username;
    private String email;
    private Password password;
    private List<UserRoles> roles = new ArrayList<>();

    public User(String username, String email, Password password, List<UserRoles> roles){
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(String username, String email, Password password){
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername(){ return username; }
    public String getEmail(){ return email; }
    public Password getPassword(){ return password; }
    public List<UserRoles> getRoles(){ return roles; }

    public void setUsername(String username){ this.username = username; }
    public void setEmail(String email){ this.email = email; }
    public void setPassword(Password password){ this.password = password; }
    public void addRole(UserRoles role){ roles.add(role); }

}
