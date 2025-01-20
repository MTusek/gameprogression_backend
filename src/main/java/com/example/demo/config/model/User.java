package com.example.demo.config.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

    private String roles;

    private boolean enabled;

    public User() {
    }

    public User(String username, String password, String email, String roles, boolean enabled) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public String getRoles() {
        return roles;
    }
}
