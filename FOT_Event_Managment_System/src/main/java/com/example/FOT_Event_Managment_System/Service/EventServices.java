package com.example.FOT_Event_Managment_System.Service;

import com.example.FOT_Event_Managment_System.Model.Event;
import com.example.FOT_Event_Managment_System.Repository.EventRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServices {
    @Autowired
    private EventRepo eventRepo;

    public void addEvent(Event event) {
        eventRepo.save(event);
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
}
