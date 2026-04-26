package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Model.Event;
import com.example.FOT_Event_Managment_System.Model.Users;
import com.example.FOT_Event_Managment_System.Repository.EventRegiRepo;
import com.example.FOT_Event_Managment_System.Repository.EventRepo;
import com.example.FOT_Event_Managment_System.Repository.UserRepo;
import com.example.FOT_Event_Managment_System.Service.EventServices;
import org.springframework.security.core.Authentication;
import com.example.FOT_Event_Managment_System.Model.EventRegi;
import com.example.FOT_Event_Managment_System.Service.EventRegiServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
    @Autowired
    private EventRepo eventRepo;

    @GetMapping("/event/register")
    public String showRegistrationForm(@RequestParam("id") Long eventId,
                                       @RequestParam("name") String eventName,
                                       @RequestParam("organizer") String organizerName,
                                       @RequestParam(value = "source", defaultValue = "participant") String source,
                                       Authentication authentication,
                                       Model model) {

        // Check if the form object already exists (this happens after a redirect with errors)
        // If it doesn't exist, only then do we create a fresh one.
        if (!model.containsAttribute("EventRegForm")) {
            EventRegi newReg = new EventRegi();
            newReg.setEventId(eventId);
            newReg.setEventName(eventName);
            newReg.setOrganizerName(organizerName);

            if (authentication != null) {
                Users currentUser = userRepo.findByUseremail(authentication.getName());
                if (currentUser != null && currentUser.getRegno() != null) {
                    newReg.setpRegistrationnnum(currentUser.getRegno());
                }
            }
            model.addAttribute("source", source);
            model.addAttribute("EventRegForm", newReg);
        }

        return "Participant/EventRegister";
    }
    @PostMapping("/event/register/save")
    public String registerEvent(@ModelAttribute("EventRegForm") EventRegi eventRegi,
                                @RequestParam(value = "source", defaultValue = "participant") String source,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {

        String email = authentication.getName();
        Users currentUser = userRepo.findByUseremail(email);

        if (currentUser != null) {
            Long eventId = eventRegi.getEventId();
            String inputRegNo = eventRegi.getpRegistrationnnum();
            String errorRedirect = "redirect:/event/register?id=" + eventId +
                    "&name=" + URLEncoder.encode(eventRegi.getEventName(), StandardCharsets.UTF_8) +
                    "&organizer=" + URLEncoder.encode(eventRegi.getOrganizerName(), StandardCharsets.UTF_8) +
                    "&source=" + source; // ✅ don't lose source on error
Users existingUserwithregno = userRepo.findByRegno(inputRegNo);
            if (existingUserwithregno != null && !existingUserwithregno.getUseremail().equals(email)) {
                redirectAttributes.addFlashAttribute("error", "This Registration Number is already associated with another account.");
                return errorRedirect;
            }
            // Save regno to profile if it's the first time
            if ("participant".equals(source)) { if (currentUser.getRegno() == null || currentUser.getRegno().isEmpty()) {
                currentUser.setRegno(inputRegNo);
                userRepo.save(currentUser);
            }}


            String regNo = "organizer".equals(source)
                    ? inputRegNo
                    : (currentUser.getRegno() != null ? currentUser.getRegno() : inputRegNo);

            // --- DUPLICATE CHECK ---
            if (eventRegiRepo.existsByEventIdAndPRegistrationnnum(eventId, regNo)) {
                redirectAttributes.addFlashAttribute("error", "You have already registered for this event.");
                return errorRedirect;
            }

            // --- SAVE ---
            try {
                eventRegi.setpRegistrationnnum(regNo); // Ensure form uses trusted value
                eventServices.decrementSlots(eventId, eventRegi.getPslots());
                eventRegiServices.registerforEvent(eventRegi);
            } catch (RuntimeException e) {
                redirectAttributes.addFlashAttribute("error", "Booking failed: " + e.getMessage());
                return errorRedirect;
            }
        }

        if ("organizer".equals(source)) {
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
        if (user == null || user.getRegno() == null) return "Participant/MyEvents";

        String studentRegNo = user.getRegno();
        List<EventRegi> myRegistrations = eventRegiRepo.findBypRegistrationnnum(studentRegNo);

        for (EventRegi reg : myRegistrations) {
            // Fetch the actual event object to check its status
            Event actualEvent = eventRepo.findById(reg.getEventId()).orElse(null);

            // Check if event is missing OR if its status is "DELETED"
            if (actualEvent == null || "DELETED".equalsIgnoreCase(actualEvent.getEventstatus())) {
                reg.setRegistatus("Event Deleted");
            }
        }

        model.addAttribute("registrations", myRegistrations);
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
