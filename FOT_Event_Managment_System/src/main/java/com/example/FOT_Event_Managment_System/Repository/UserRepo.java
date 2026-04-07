package com.example.FOT_Event_Managment_System.Repository;

import com.example.FOT_Event_Managment_System.Model.Users;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepo extends JpaRepository<Users, String> {
    Users findByUseremail(String email);
    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.userstatus = 'Suspended' WHERE u.userid = ?1")
    void suspendUser(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE Users u SET u.userstatus = 'Active' WHERE u.userid = ?1")
    void activateUser(Long id);
}
