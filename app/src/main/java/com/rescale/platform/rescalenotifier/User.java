package com.rescale.platform.rescalenotifier;

/**
 * Created by johnpark on 12/19/14.
 */
public class User {
    private String username;
    private String password;

    public User(String email, String password) {
        this.username = email;
        this.password = password;
    }

    public User() {
    }

    public String getEmail() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
