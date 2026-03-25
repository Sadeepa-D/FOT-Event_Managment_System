package com.example.FOT_Event_Managment_System.Service;

import com.example.FOT_Event_Managment_System.Repository.UserRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.FOT_Event_Managment_System.Model.Users;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String emailOrUsername) throws UsernameNotFoundException {
        // Search by email since that's what the user provided
        Users user = userRepo.findByUseremail(emailOrUsername);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUseremail())
                .password(user.getUserpassword()) // This MUST be the BCrypt hash from DB
                .roles(user.getUserrole())
                .build();
    }
}
