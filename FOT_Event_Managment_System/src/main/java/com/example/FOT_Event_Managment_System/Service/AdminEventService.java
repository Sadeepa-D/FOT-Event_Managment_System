package com.example.FOT_Event_Managment_System.Service;

import com.example.FOT_Event_Managment_System.Model.Event;
import com.example.FOT_Event_Managment_System.Model.EventRegi;
import com.example.FOT_Event_Managment_System.Repository.EventRegiRepo;
import com.example.FOT_Event_Managment_System.Repository.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminEventService {

    @Autowired
    private EventRepo eventRepo;

    @Autowired
    private EventRegiRepo eventRegiRepo;

    // View all events
    public List<Event> getAllEvents() {
        return eventRepo.findAll();
    }

    // Approve event
    public Event approveEvent(Long id) {
        Event event = eventRepo.findById(id).orElseThrow();
        event.setEventstatus("APPROVED");
        return eventRepo.save(event);
    }

    // Reject event
    public Event rejectEvent(Long id) {
        Event event = eventRepo.findById(id).orElseThrow();
        event.setEventstatus("REJECTED");
        return eventRepo.save(event);
    }

    // Delete event
    public void deleteEvent(Long id) {
        eventRepo.deleteById(id);
    }

    // Get participants of an event
    public List<EventRegi> getParticipants(Long eventId) {
        return eventRegiRepo.findByEventId(eventId);
    }
}