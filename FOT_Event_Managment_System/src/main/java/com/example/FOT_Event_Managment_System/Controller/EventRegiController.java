package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Model.Event;
import com.example.FOT_Event_Managment_System.Model.EventRegi;
import com.example.FOT_Event_Managment_System.Service.EventRegiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class EventRegiController {
    @Autowired
    private EventRegiServices eventRegiServices;
    @GetMapping("/event/register") // This opens the form page
    public String showRegistrationForm(@RequestParam("id") Long eventId,
                                       @RequestParam("name") String eventName,
                                       @RequestParam("organizer") String organizerName,

                                       Model model) {
        EventRegi newReg = new EventRegi();
        newReg.setEventId(eventId);
        newReg.setEventName(eventName);
        newReg.setOrganizerName(organizerName);
        model.addAttribute("EventRegForm", newReg);
        return "Participant/EventRegister"; // Points to your HTML file
    }
    @PostMapping("/event/register/save")
    public String registerEvent(@ModelAttribute("EventRegForm") EventRegi eventRegi) {
        eventRegiServices.registerforEvent(eventRegi);
        return "redirect:/paticipanthome?success";
    }
    @GetMapping("/event/Showparticipant/{id}")
    public String showParticipant(@PathVariable("id") Long eventId, Model model) {
        // 1. Fetch the list of registrations
        List<EventRegi> participants = eventRegiServices.getParticipantsByEventId(eventId);
        model.addAttribute("participants", participants);

        // 2. Create a "header" object to avoid the null error
        EventRegi headerInfo = new EventRegi();

        if (!participants.isEmpty()) {
            // Pass the FIRST registration object from the list
            headerInfo = participants.get(0);
        } else {
            // Fallback data so the page doesn't crash if no one joined yet
            headerInfo.setEventName("No registrations found for this event");
            headerInfo.setOrganizerName("N/A");
        }

        // IMPORTANT: This object must be named "event" to match your HTML
        model.addAttribute("event", headerInfo);

        return "Organizer/ParticipantList";
    }
}
