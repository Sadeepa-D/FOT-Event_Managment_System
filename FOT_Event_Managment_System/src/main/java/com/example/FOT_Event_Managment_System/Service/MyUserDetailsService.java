package com.example.FOT_Event_Managment_System.Service;

import com.example.FOT_Event_Managment_System.Repository.UserRepo;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.AuthorityUtils;
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
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = userRepo.findByUseremail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // BLOCK SUSPENDED USERS
        // We check if status is NOT "Active"
        if (user.getUserstatus() != null && !user.getUserstatus().equalsIgnoreCase("Active")) {
            throw new LockedException("Your account has been suspended. Please contact the administrator.");
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUseremail(),
                user.getUserpassword(), // CHANGED FROM getPass() TO getUserpassword()
                AuthorityUtils.createAuthorityList("ROLE_" + user.getUserrole())
        );
    }
}
