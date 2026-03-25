package com.example.FOT_Event_Managment_System.Model;

import jakarta.persistence.*;

@Entity
@Table(name="user")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userid;
    private String username;
    private String userpassword;
    private String useremail;
    private String userrole;

    public Users() {}

    public Users(Long userid, String username, String userpassword, String useremail, String userrole) {
        this.userid = userid;
        this.username = username;
        this.userpassword = userpassword;
        this.useremail = useremail;
        this.userrole = userrole;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserrole() {
        return userrole;
    }

    public void setUserrole(String userrole) {
        this.userrole = userrole;
    }
}
