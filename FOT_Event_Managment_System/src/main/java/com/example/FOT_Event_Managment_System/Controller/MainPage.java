package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Model.Event;
import com.example.FOT_Event_Managment_System.Service.EventServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

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
        // 1. Fetch only the events with 'accept' status
        List<Event> approvedEvents = eventServices.getAcceptedEvents();

        // 2. Add to model using your existing attribute name
        model.addAttribute("View_All_Events", approvedEvents);

        return "AllEvents";
    }
    @GetMapping("/paticipanthome")
    public String ViewPaticipanthome(Model model) {
        // 1. Fetch ONLY approved events
        List<Event> approvedEvents = eventServices.getAcceptedEvents();
        model.addAttribute("events", approvedEvents);

        // 2. Add custom welcome message
        model.addAttribute("welcomeMsg", "Welcome to the Student Event Portal");

        return "Participant/ParticiHome";
    }
}