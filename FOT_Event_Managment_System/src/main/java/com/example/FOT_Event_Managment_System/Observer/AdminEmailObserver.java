package com.example.FOT_Event_Managment_System.Observer;

import com.example.FOT_Event_Managment_System.Model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AdminEmailObserver {

    private static final Logger log = LoggerFactory.getLogger(AdminEmailObserver.class);

    private final JavaMailSender mailSender;

    // Pull admin email from application.properties
    @Value("${app.admin.email}")
    private String adminEmail;

    public AdminEmailObserver(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @EventListener                // marks this as an Observer
    @Async                        // runs in background — won't slow down the response
    public void onEventCreated(EventCreatedEvent eventCreatedEvent) {
        Event event = eventCreatedEvent.getEvent();
        String triggerType = eventCreatedEvent.getTriggerType();

        log.info("Observer triggered: Admin email for event '{}' [{}]",
                event.getEventName(), triggerType);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(adminEmail);

            if ("NEW".equals(triggerType)) {
                message.setSubject("New Event Submitted for Review: " + event.getEventName());
                message.setText(buildNewEventEmailBody(event));
            } else {
                message.setSubject("Event Edited - Needs Re-Review: " + event.getEventName());
                message.setText(buildEditedEventEmailBody(event));
            }

            mailSender.send(message);
            log.info("Admin notification email sent successfully for event ID: {}", event.getId());

        } catch (Exception e) {
            log.error("Failed to send admin email for event ID: {}", event.getId(), e);
        }
    }

    private String buildNewEventEmailBody(Event event) {
        return """
                A new event has been submitted and is awaiting your approval.
                
                ─────────────────────────────
                Event Details
                ─────────────────────────────
                Name        : %s
                Organizer   : %s
                Contact     : %s
                Date        : %s at %s
                Venue       : %s
                Slots       : %d
                Ticket Price: Rs. %.2f
                Status      : %s
                
                Description:
                %s
                
                Please log in to the admin panel to review and approve or reject this event.
                """.formatted(
                event.getEventName(),
                event.getOrganizerName(),
                event.getOrganizerPhone(),
                event.getDate(),
                event.getTime(),
                event.getVenue(),
                event.getAvailableSlots(),
                event.getTicketPrice(),
                event.getEventstatus(),
                event.getDescription()
        );
    }

    private String buildEditedEventEmailBody(Event event) {
        return """
                An existing event has been edited and requires re-review.
                
                ─────────────────────────────
                Updated Event Details
                ─────────────────────────────
                Event ID    : %d
                Name        : %s
                Organizer   : %s
                Contact     : %s
                Date        : %s at %s
                Venue       : %s
                Slots       : %d
                Ticket Price: Rs. %.2f
                Status      : %s
                
                Please log in to the admin panel to review the changes.
                """.formatted(
                event.getId(),
                event.getEventName(),
                event.getOrganizerName(),
                event.getOrganizerPhone(),
                event.getDate(),
                event.getTime(),
                event.getVenue(),
                event.getAvailableSlots(),
                event.getTicketPrice(),
                event.getEventstatus()
        );
    }
}