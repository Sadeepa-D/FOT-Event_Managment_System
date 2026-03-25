package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Model.Users;
import com.example.FOT_Event_Managment_System.Service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    @Autowired
    private UserServices userServices;
    @GetMapping("/users")
    public String viewUsers(Model model) {
        model.addAttribute("UserList",userServices.getUsers());
        return "Users";
    }
    @GetMapping("/users/add")
    public String showaddUserform(Model model) {
       model.addAttribute("UserForm", new Users());
       return "RegisterPage";
    }

    @PostMapping("/users/save")
    public String saveUser(@ModelAttribute("UserForm") Users user) {
        userServices.addUser(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String showLogin(Model model) {
        return "Login";
    }
}
