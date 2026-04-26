package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Model.Event;
import com.example.FOT_Event_Managment_System.Repository.EventRepo;
import com.example.FOT_Event_Managment_System.Repository.UserRepo;
import com.example.FOT_Event_Managment_System.Repository.locationRepo;
import com.example.FOT_Event_Managment_System.Service.EventServices;
import com.example.FOT_Event_Managment_System.Model.Users;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class EventController {
    @Autowired
    private EventServices eventServices;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private locationRepo locationRepo;
    @Autowired
    private EventRepo eventRepo;

    @GetMapping("/events")
    public String events(Model model, Authentication authentication) {
        String email = authentication.getName();
        Users user = userRepo.findByUseremail(email);

        if (user != null) {
            // Fetch ALL events for this user ID, ignoring status filters
            List<Event> allUserEvents = eventServices.getEventsByOrganizer(user.getUserid());
            model.addAttribute("events", allUserEvents);
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
        model.addAttribute("locations", locationRepo.findAll());
        return "Organizer/AddEvent";
    }
    @PostMapping("/event/save")
    public String SaveEvent(@ModelAttribute("eventForm") Event event, Authentication authentication, Model model) {
        String currentUsername = authentication.getName();
        Users currentUser = userRepo.findByUseremail(currentUsername);

        // 1. Check if Venue is null or empty
        if (event.getVenue() == null || event.getVenue().isEmpty()) {
            model.addAttribute("errorMessage", "⚠️ Error: Please select a valid venue before submitting.");
            model.addAttribute("locations", locationRepo.findAll());
            return "Organizer/AddEvent"; // Same page reload
        }

        // 2. Double-check booking conflict on the backend
        List<String> bookedVenues = eventRepo.findBookedVenuesByDate(event.getDate());
        if (bookedVenues.contains(event.getVenue())) {
            // If it's an edit, check if it's the SAME event's booking (allowed) or a different one (blocked)
            Event existing = (event.getId() != null) ? eventServices.getEventById(event.getId()) : null;
            if (existing == null || !event.getVenue().equals(existing.getVenue())) {
                model.addAttribute("errorMessage", "⚠️ Conflict: This venue is already booked for the selected date.");
                model.addAttribute("locations", locationRepo.findAll());
                return "Organizer/AddEvent";
            }
        }

        // 3. Normal Saving Logic
        event.setOrganizerId(currentUser.getUserid());
        if (event.getId() == null) {
            event.setEventstatus("PENDING");
        } else {
            event.setEventstatus("Edited PENDING To Review");
        }

        eventServices.addEvent(event);
        return "redirect:/events"; // Only redirect on success
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
        model.addAttribute("locations", locationRepo.findAll());
        return "Organizer/AddEvent";
    }
    @GetMapping("/api/booked-venues")
    @ResponseBody
    public List<String> getBookedVenues(@RequestParam String date,
                                        @RequestParam(required = false) Long excludeId) {
        if (excludeId != null) {
            return eventRepo.findBookedVenuesByDateExcluding(date, excludeId);
        }
        return eventRepo.findBookedVenuesByDate(date);
    }
}
