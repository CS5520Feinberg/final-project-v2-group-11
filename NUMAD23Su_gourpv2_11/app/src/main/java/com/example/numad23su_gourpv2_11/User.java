package com.example.numad23su_gourpv2_11;

public class User {

    private String name;
    private String username;
    private String email;

    public User() {}

    public User(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public String getName() { return name; }

    public String getEmail() { return email; }

    public String getUsername() { return username; }

}
