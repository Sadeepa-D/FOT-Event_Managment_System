package com.example.FOT_Event_Managment_System.Service;


import com.example.FOT_Event_Managment_System.Model.Users;
import com.example.FOT_Event_Managment_System.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {
    @Autowired
   private UserRepo userRepo;

    public List<Users> getUsers() {
        return userRepo.findAll();
    }
    public Users getUserbyid(String id) {
        return userRepo.findById(id).orElse(null);
    }
    public void deleteUser(String id) {
         userRepo.deleteById(id);
    }
    public void addUser(Users user) {
        userRepo.save(user);
    }
    public void updateUser(Users user) {
        userRepo.save(user);
    }
}
