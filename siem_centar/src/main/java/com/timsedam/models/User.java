package com.timsedam.models;

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class User {

    @Id
    private String email;

    private String password;

    @ManyToMany
    private Collection<Role> roles;

    public User() {
    }

    public User(String email, String password, Collection<Role> roles, String salt) {
        this.email = email;
        this.password = password;
        this.roles = roles;
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRole(Collection<Role> roles) {
        this.roles = roles;
    }

}
