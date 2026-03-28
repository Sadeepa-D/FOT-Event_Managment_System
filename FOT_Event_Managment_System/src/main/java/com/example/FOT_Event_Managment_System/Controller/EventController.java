package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Model.Event;
import com.example.FOT_Event_Managment_System.Repository.UserRepo;
import com.example.FOT_Event_Managment_System.Service.EventServices;
import com.example.FOT_Event_Managment_System.Model.Users;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class EventController {
    @Autowired
    private EventServices eventServices;
    @Autowired
    private UserRepo userRepo;
    @GetMapping("/events")
    public String events(Model model, Authentication authentication) {
        // 1. Get current logged-in user email
        String email = authentication.getName();

        // 2. Find user to get their ID
        Users user = userRepo.findByUseremail(email);

        // 3. ONLY fetch events for this specific user
        if (user != null) {
            model.addAttribute("events", eventServices.getEventsByOrganizer(user.getUserid()));
            model.addAttribute("fullName", user.getUsername());
        } else {
            model.addAttribute("events", new ArrayList<>());
            model.addAttribute("fullName", "User");
        }

        model.addAttribute("eventForm", new Event());
        return "Organizer/Events";
    }
    @GetMapping("/event/add")
    public String addEventForm(Model model) {
        model.addAttribute("eventForm", new Event());
        return "Organizer/AddEvent";
    }
    @PostMapping("/event/save")
    public String SaveEvent(@ModelAttribute("eventForm") Event event, Authentication authentication) {
        // 1. Get the username of the person logged in
        String currentUsername = authentication.getName();

        // 2. Find that user in your database
        Users currentUser = userRepo.findByUseremail(currentUsername);

        // 3. Link the event to this user's ID
        // Assuming your Event model has a field like 'organizerId'
        event.setOrganizerId(currentUser.getUserid());

        eventServices.addEvent(event);
        return "redirect:/events";
    }
    @GetMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable("id") Long id) {
        eventServices.deleteEvent(id);
        return "redirect:/events";
    }
    @GetMapping("/events/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Event existingEvent = eventServices.getEventById(id);
        model.addAttribute("eventForm", existingEvent);
        return "Organizer/AddEvent";
    }
}
