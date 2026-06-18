package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Model.Event;
import com.example.FOT_Event_Managment_System.Model.Users;
import com.example.FOT_Event_Managment_System.Service.EventServices;
import org.springframework.security.core.Authentication;
import com.example.FOT_Event_Managment_System.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainPage {
    @Autowired
    private EventServices eventServices;
    @Autowired
    private UserRepo userRepo;

    @GetMapping("/")
    public String ViewMainPage(Model model) {
        model.addAttribute("message", "Welcome to Fot Event Management System");
        
        // Dynamic stats
        long totalEvents = eventServices.getEvents().size();
        long totalStudents = userRepo.countByUserrole("Participant");
        long totalClubs = userRepo.countByUserrole("Organizer");
        
        model.addAttribute("totalEvents", totalEvents);
        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalClubs", totalClubs);
        
        List<Event> approvedEvents = eventServices.getAcceptedEvents();
        
        // Sort by date ascending (nearest events first)
        approvedEvents.sort(java.util.Comparator.comparing(Event::getDate, java.util.Comparator.nullsLast(String::compareTo)));

        if (approvedEvents.size() > 3) {
            approvedEvents = approvedEvents.subList(0, 3);
        }
        model.addAttribute("upcomingEvents", approvedEvents);
        
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
    public String ViewPaticipanthome(Model model, Authentication authentication) {
        String email = authentication.getName();
        Users user = userRepo.findByUseremail(email);
        // 3. Add the Name to the model for the greeting
        if (user != null) {
            model.addAttribute("fullName", user.getUsername());
        } else {
            model.addAttribute("fullName", "Student");
        }
        // 1. Fetch ONLY approved events
        List<Event> approvedEvents = eventServices.getAcceptedEvents();
        model.addAttribute("events", approvedEvents);

        // 2. Add custom welcome message
        model.addAttribute("welcomeMsg", "Welcome to the Student Event Portal");
        model.addAttribute("today", java.time.LocalDate.now());

        return "Participant/ParticiHome";
    }
}