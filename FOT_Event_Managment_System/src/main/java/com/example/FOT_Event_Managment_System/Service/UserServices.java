package com.example.FOT_Event_Managment_System.Service;


import com.example.FOT_Event_Managment_System.Model.Users;
import com.example.FOT_Event_Managment_System.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {
    @Autowired
   private UserRepo userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Users> getUsers() {
        return userRepo.findByuserroleNot("ADMIN");
    }
    public Users getUserbyid(Long id) {
        return userRepo.findById(id).orElse(null);
    }
    public void deleteUser(Long id) {
         userRepo.deleteById(id);
    }
    public void addUser(Users user) {
        // 1. Check if we are UPDATING an existing user
        if (user.getUserid() != null) {
            Users existingUser = userRepo.findById(user.getUserid()).orElse(null);

            if (existingUser != null) {
                // 2. If the password field in the form is BLANK, keep the OLD encoded password
                if (user.getUserpassword() == null || user.getUserpassword().isEmpty()) {
                    user.setUserpassword(existingUser.getUserpassword());
                } else {
                    // 3. If a NEW password was typed, encode it before saving
                    user.setUserpassword(passwordEncoder.encode(user.getUserpassword()));
                }
            }
        } else {
            // 4. This is a NEW registration - Always encode the password
            user.setUserpassword(passwordEncoder.encode(user.getUserpassword()));
        }

        userRepo.save(user);
    }
    public void updateUser(Users user) {
        userRepo.save(user);
    }
}
