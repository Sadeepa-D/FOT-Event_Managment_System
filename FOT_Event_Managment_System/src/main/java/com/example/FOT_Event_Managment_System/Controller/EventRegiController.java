package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Model.Event;
import com.example.FOT_Event_Managment_System.Model.Users;
import com.example.FOT_Event_Managment_System.Repository.EventRegiRepo;
import com.example.FOT_Event_Managment_System.Repository.UserRepo;
import com.example.FOT_Event_Managment_System.Service.EventServices;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.example.FOT_Event_Managment_System.Model.EventRegi;
import com.example.FOT_Event_Managment_System.Service.EventRegiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class EventRegiController {
    @Autowired
    private EventRegiServices eventRegiServices;
    @Autowired
    private EventRegiRepo eventRegiRepo;
    @Autowired
    private EventServices eventServices;
    @Autowired
    private UserRepo userRepo;
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
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {

        String email = authentication.getName();
        Users currentUser = userRepo.findByUseremail(email);

        if (currentUser != null) {
            String providedRegNo = eventRegi.getpRegistrationnnum();
            Long eventId = eventRegi.getEventId();
            int requestedSlots = eventRegi.getPslots();

            // Common redirect for errors
            String errorRedirect = "redirect:/event/register?id=" + eventId +
                    "&name=" + eventRegi.getEventName() +
                    "&organizer=" + eventRegi.getOrganizerName();

            // --- STEP 1: DUPLICATE REGISTRATION CHECK ---
            boolean alreadyRegistered = eventRegiRepo.existsByEventIdAndPRegistrationnnum(eventId, providedRegNo);
            if (alreadyRegistered) {
                redirectAttributes.addFlashAttribute("error", "This Registration Number is already registered.");
                return errorRedirect;
            }

            // --- STEP 2: PROFILE SYNC & VALIDATION ---
            boolean isParticipant = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_PARTICIPANT"));
            if (isParticipant) {
                String existingRegNo = currentUser.getRegno();
                if (existingRegNo != null && !existingRegNo.isEmpty()) {
                    if (!existingRegNo.equalsIgnoreCase(providedRegNo)) {
                        redirectAttributes.addFlashAttribute("error", "Registration Number does not match your profile.");
                        return errorRedirect;
                    }
                } else {
                    currentUser.setRegno(providedRegNo);
                    userRepo.save(currentUser);
                }
            }

            // --- STEP 3: SLOT MANAGEMENT & SAVE ---
            try {
                // Check availability and reduce slots ONLY after all other checks pass
                eventServices.decrementSlots(eventId, requestedSlots);

                // Save the registration
                eventRegiServices.registerforEvent(eventRegi);

            } catch (RuntimeException e) {
                redirectAttributes.addFlashAttribute("error", "Booking failed: " + e.getMessage());
                return errorRedirect;
            }
        }

        // --- STEP 4: SUCCESS REDIRECTS ---
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ORGANIZER"))) {
            return "redirect:/event/showparticipant/" + eventRegi.getEventId();
        } else {
            return "redirect:/paticipanthome?success";
        }
    }
    @GetMapping("/event/showparticipant/{id}")
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
        return "redirect:/event/showparticipant/" + eventId;
    }
    @GetMapping("/event/registration/approve/{id}")
    public String approveEventregistration(@PathVariable("id") Long registrationId) {

        // 1. Find the registration record to verify it exists and get the Event ID
        EventRegi registration = eventRegiRepo.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid registration Id:" + registrationId));

        Long eventId = registration.getEventId();

        // 2. Call the specific service method for APPROVAL
        // This maps to: UPDATE EventRegi e SET e.registatus = 'Approved'
        eventRegiServices.updateregistrationstatustoapprove(registrationId);

        // 3. Redirect back to the participant list for that specific event
        return "redirect:/event/showparticipant/" + eventId;
    }

    @GetMapping("/myregistrations")
    public String showMyEvents(Model model, Authentication authentication) {
        Users user = userRepo.findByUseremail(authentication.getName());

        // This will now find the value we saved in Step 2
        String studentRegNo = user.getRegno();

        if (studentRegNo != null) {
            List<EventRegi> myRegistrations = eventRegiRepo.findBypRegistrationnnum(studentRegNo);
            model.addAttribute("registrations", myRegistrations);
        }

        return "Participant/MyEvents";
    }
    @GetMapping("/event/registration/delete/{id}")
    public String unregisterEvent(@PathVariable("id") Long id) {
        // 1. Find the registration record first
        EventRegi registration = eventRegiRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid registration Id:" + id));

        Long eventId = registration.getEventId();
        int slotsToReturn = registration.getPslots();

        // 2. Add the slots back to the main Event table
        eventServices.incrementSlots(eventId, slotsToReturn);

        // 3. Now it is safe to delete the registration
        eventRegiRepo.deleteById(id);

        return "redirect:/myregistrations?unregistered";
    }
    @GetMapping("/event/registration/checkin/{id}")
    public String checkIn(@PathVariable("id") Long registrationId, RedirectAttributes redirectAttributes) {
        EventRegi registration = eventRegiRepo.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid registration Id:" + registrationId));

        // Check if the registration is approved
        if (!"Approved".equalsIgnoreCase(registration.getRegistatus())) {
            redirectAttributes.addFlashAttribute("error", "Cannot check-in. Participant is not Approved.");
            return "redirect:/event/showparticipant/" + registration.getEventId();
        }

        eventRegiServices.checkInParticipant(registrationId);
        return "redirect:/event/showparticipant/" + registration.getEventId();
    }

    @GetMapping("/event/registration/checkout/{id}")
    public String checkOut(@PathVariable("id") Long registrationId, RedirectAttributes redirectAttributes) {
        EventRegi registration = eventRegiRepo.findById(registrationId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid registration Id:" + registrationId));

        // Check if the registration is approved
        if (!"Approved".equalsIgnoreCase(registration.getRegistatus())) {
            redirectAttributes.addFlashAttribute("error", "Cannot check-out. Participant is not Approved.");
            return "redirect:/event/showparticipant/" + registration.getEventId();
        }

        eventRegiServices.checkOutParticipant(registrationId);
        return "redirect:/event/showparticipant/" + registration.getEventId();
    }
}
