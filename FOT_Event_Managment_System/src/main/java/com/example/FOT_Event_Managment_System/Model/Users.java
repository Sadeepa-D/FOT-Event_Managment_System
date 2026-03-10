package com.example.FOT_Event_Managment_System.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="user")
public class Users {
    @Id
    private String u_id;
    private String u_name;
    private String u_password;
    private String u_email;
    private String role;

    public Users() {}

    public Users(String u_id, String u_name, String u_password, String u_email, String role) {
        this.u_id = u_id;
        this.u_name = u_name;
        this.u_password = u_password;
        this.u_email = u_email;
        this.role = role;
    }

    public String getU_id() {
        return u_id;
    }

    public void setU_id(String u_id) {
        this.u_id = u_id;
    }

    public String getU_name() {
        return u_name;
    }

    public void setU_name(String u_name) {
        this.u_name = u_name;
    }

    public String getU_password() {
        return u_password;
    }

    public void setU_password(String u_password) {
        this.u_password = u_password;
    }

    public String getU_email() {
        return u_email;
    }

    public void setU_email(String u_email) {
        this.u_email = u_email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
