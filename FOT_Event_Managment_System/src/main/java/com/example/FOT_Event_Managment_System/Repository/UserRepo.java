package com.example.FOT_Event_Managment_System.Repository;

import com.example.FOT_Event_Managment_System.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<Users, String> {
    Users findByUseremail(String email);
}
