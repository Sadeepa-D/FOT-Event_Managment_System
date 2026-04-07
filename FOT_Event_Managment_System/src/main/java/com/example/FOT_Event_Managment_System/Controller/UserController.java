package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Model.Users;
import com.example.FOT_Event_Managment_System.Repository.UserRepo;
import com.example.FOT_Event_Managment_System.Service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    @Autowired
    private UserServices userServices;
    @Autowired
    private UserRepo userRepo;
    @GetMapping("/users")
    public String viewUsers(Model model) {
        model.addAttribute("UserList",userServices.getUsers());
        return "Admin/Users";
    }
    @GetMapping("/users/add")
    public String showaddUserform(Model model) {
       model.addAttribute("UserForm", new Users());
       return "auth/RegisterPage";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("UserForm") Users user) {
        userServices.addUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        return "auth/Login";
    }
    // Logic for Suspending a User
    @RequestMapping("/users/suspend/{id}")
    public String suspendUser(@PathVariable Long id) {
        userRepo.suspendUser(id);
        return "redirect:/users"; // Redirect back to the user management page
    }

    // Logic for Activating a User
    @RequestMapping("/users/activate/{id}")
    public String activateUser(@PathVariable Long id) {
        userRepo.activateUser(id);
        return "redirect:/users";
    }
}
