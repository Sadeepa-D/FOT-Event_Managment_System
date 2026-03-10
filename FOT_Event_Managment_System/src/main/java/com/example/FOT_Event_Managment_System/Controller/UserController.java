package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Service.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {
    @Autowired
    private UserServices userServices;
    @GetMapping("/users")
    public String viewUsers(Model model) {
        model.addAttribute("UserList",userServices.getUsers());
        return "Users";
    }
}
