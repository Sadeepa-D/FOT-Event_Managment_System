package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Model.Event;
import com.example.FOT_Event_Managment_System.Repository.EventRegiRepo;
import com.example.FOT_Event_Managment_System.Service.EventServices;
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
    @Autowired
    private EventRegiRepo eventRegiRepo;
    @Autowired
    private EventServices eventServices;
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
        // 1. Get ONLY active participants
        List<EventRegi> participants = eventRegiServices.getParticipantsByEventId(eventId);
        model.addAttribute("participants", participants);
        model.addAttribute("selectedEventId", eventId);

        // 2. Fetch official Event details so the header is always correct
        // Replace 'eventService' with your actual Event Service name
        Event actualEvent = eventServices.getEventById(eventId);

        if (actualEvent != null) {
            model.addAttribute("event", actualEvent);
        } else {
            // Fallback if event is totally missing
            EventRegi fallback = new EventRegi();
            fallback.setEventName("Event Not Found");
            model.addAttribute("event", fallback);
        }

        return "Organizer/ParticipantList";
    }
    @GetMapping("/event/registration/remove/{id}")
    public String removeEventregistration(@PathVariable("id") Long registrationId) {

        // 1. Find the registration record to know which event it belongs to
        EventRegi registration = eventRegiRepo.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid registration Id:" + registrationId));

        Long eventId = registration.getEventId();

        // 2. Perform the update to set status as 'true'
        eventRegiServices.updateregistrationstatus(registrationId);

        // 3. Redirect back to the participant list for that specific event
        return "redirect:/event/Showparticipant/" + eventId;
    }
}
