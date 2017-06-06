package com.timsedam.models;

import com.timsedam.dto.UserDTO;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.*;

@Entity
public class User {

    @Id
    private String email;

    private String password;

    @ManyToOne
    private Role role;

    public User() {
    }

    public User(String email, String password, Role role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
