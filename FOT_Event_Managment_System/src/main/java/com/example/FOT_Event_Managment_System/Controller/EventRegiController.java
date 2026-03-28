package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Model.Event;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    public String registerEvent(@ModelAttribute("EventRegForm") EventRegi eventRegi,
                                Authentication authentication) {

        // 1. Save the registration as usual
        eventRegiServices.registerforEvent(eventRegi);

        // 2. Check the user's role
        if (authentication != null && authentication.getAuthorities()
                .contains(new SimpleGrantedAuthority("ROLE_ORGANIZER"))) {

            // If Organizer: Redirect back to the participant list for that specific event
            return "redirect:/event/Showparticipant/" + eventRegi.getEventId();
        } else {

            // If Participant: Redirect to their home dashboard with a success message
            return "redirect:/paticipanthome?success";
        }
    }
    @GetMapping("/event/Showparticipant/{id}")
    public String showParticipant(@PathVariable("id") Long eventId, Model model) {
        List<EventRegi> participants = eventRegiServices.getParticipantsByEventId(eventId);
        model.addAttribute("participants", participants);

        // Explicitly send the ID to the page for the "Register" button
        model.addAttribute("selectedEventId", eventId);

        EventRegi headerInfo = new EventRegi();
        if (!participants.isEmpty()) {
            headerInfo = participants.get(0);
        } else {
            // Fallback info
            headerInfo.setEventName("New Event");
            headerInfo.setOrganizerName("Faculty Organizer");
        }
        model.addAttribute("event", headerInfo);

        return "Organizer/ParticipantList";
    }
}
