package com.example.FOT_Event_Managment_System.Service;

import com.example.FOT_Event_Managment_System.Model.Event;
import com.example.FOT_Event_Managment_System.Observer.EventCreatedEvent;
import com.example.FOT_Event_Managment_System.Repository.EventRepo;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServices {
    @Autowired
    private EventRepo eventRepo;

    private final ApplicationEventPublisher publisher;

    public EventServices(EventRepo eventrepo, ApplicationEventPublisher publisher) {
        this.eventRepo = eventrepo;
        this.publisher = publisher;
    }

    public void addEvent(Event event) {
        String triggerType = (event.getId() == null) ? "NEW" : "EDITED";
        eventRepo.save(event);
        publisher.publishEvent(new EventCreatedEvent(this, event, triggerType));
    }
    public List<Event> getEvents(){
        return eventRepo.findAll();
    }
    public Event getEventById(long id) {
        return eventRepo.findById(id).orElse(null);
    }
    public List<Event> getAcceptedEventsByOrganizer(Long organizerId) {
        // We pass the ID and the hardcoded string "accept"
        return eventRepo.findByOrganizerIdAndEventstatus(organizerId, "APPROVED");
    }
    public List<Event> getAcceptedEvents() {
        // This fetches all events where eventstatus = "accept"
        return eventRepo.findByEventstatus("APPROVED");
    }
    public List<Event> getEventsByOrganizer(Long organizerId) {
        return eventRepo.findByOrganizerId(organizerId);
    }
    public void deleteEvent(long id) {
        eventRepo.softDeleteEvent(id);
    }
    public void updateEvent(long id, Event event) {
        eventRepo.save(event);
    }
    @Transactional
    public void decrementSlots(Long eventId, int count) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        int currentSlots = event.getAvailableSlots();
        if (currentSlots >= count) {
            event.setAvailableSlots(currentSlots - count);
            eventRepo.save(event);
        } else {
            throw new RuntimeException("No slots available!");
        }
    }

    @Transactional
    public void incrementSlots(Long eventId, int count) {
        Event event = eventRepo.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setAvailableSlots(event.getAvailableSlots() + count);
        eventRepo.save(event);
    }
}
