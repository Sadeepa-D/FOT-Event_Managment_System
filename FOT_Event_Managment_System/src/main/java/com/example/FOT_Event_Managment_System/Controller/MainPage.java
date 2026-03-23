package com.example.FOT_Event_Managment_System.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPage {
    @GetMapping("/")

    public String ViewMainPage(Model model) {
        model.addAttribute("message", "Welcome to Fot Event Management System");
        return "Main";
    }
}