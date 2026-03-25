package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Service.EventServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPage {
    @Autowired
    private EventServices eventServices;
    @GetMapping("/")

    public String ViewMainPage(Model model) {
        model.addAttribute("message", "Welcome to Fot Event Management System");
        return "Main";
    }
    @GetMapping("/allevents")
    public String ViewAllEvents(Model model) {
        model.addAttribute("View_All_Events",eventServices.getEvents());
        return "AllEvents";
    }
    @GetMapping("/paticipanthome")
    public String ViewPaticipanthome(Model model) {
        // 1. Fetch all events so the student can see what is available
        model.addAttribute("events", eventServices.getEvents());

        // 2. Optional: Add a custom welcome message
        model.addAttribute("welcomeMsg", "Welcome to the Student Event Portal");

        return "Participant/ParticiHome";
    }
}