package com.example.FOT_Event_Managment_System.Config;

import com.example.FOT_Event_Managment_System.Model.Users;
import com.example.FOT_Event_Managment_System.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepo userepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userepo.findByUseremail("admin@faculty.lk")==null) {
            Users admin = new Users();
            admin.setUseremail("admin@faculty.lk");
            admin.setUserpassword(passwordEncoder.encode("admin123"));
            admin.setUserrole("ADMIN");
            userepo.save(admin);
            System.out.println("Default Admin created successfully.");
        }
    }
}