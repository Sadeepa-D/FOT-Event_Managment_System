package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Model.Users;
import com.example.FOT_Event_Managment_System.Repository.UserRepo;
import com.example.FOT_Event_Managment_System.Service.AdminEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    
    @Autowired
    private AdminEventService service;
    @Autowired
    private UserRepo userRepo;

    // Redirect root /admin to /admin/events
    @GetMapping
    public String adminRoot() {
        return "redirect:/admin/events";
    }

    // Optional: handle /admin/dashboard directly
    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/admin/events";
    }

    // Load admin dashboard (list of events)
    @GetMapping("/events")
    public String viewEvents(Model model, Authentication authentication) {
        String email = authentication.getName();
        Users user= userRepo.findByUseremail(email);
        if (user != null) {
            model.addAttribute("fullName", user.getUsername());
        }else {
            model.addAttribute("fullName", "Admin");
        }

        model.addAttribute("events", service.getAllEvents());
        return "Admin/admin-dashboard";
    }

    // Approve event
    @GetMapping("/approve/{id}")
    public String approveEvent(@PathVariable Long id) {
        service.approveEvent(id);
        return "redirect:/admin/events";
    }

    // Reject event
    @GetMapping("/reject/{id}")
    public String rejectEvent(@PathVariable Long id) {
        service.rejectEvent(id);
        return "redirect:/admin/events";
    }

    // Delete event
    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        service.deleteEvent(id);
        return "redirect:/admin/events";
    }

    // View participants
    @GetMapping("/participants/{eventId}")
    public String viewParticipants(@PathVariable Long eventId, Model model) {
        model.addAttribute("participants", service.getParticipants(eventId));
        return "participants";
    }
}