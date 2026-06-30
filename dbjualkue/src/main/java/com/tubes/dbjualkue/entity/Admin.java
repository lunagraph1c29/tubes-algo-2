package com.tubes.dbjualkue.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @Column(name = "id_admin", length = 4)
    private String idAdmin;

    @Column(name = "username", length = 30)
    private String username;

    @Column(name = "password", length = 255)
    private String password;

    public Admin() {
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}