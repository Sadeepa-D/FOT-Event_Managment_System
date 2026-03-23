package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Model.Event;
import com.example.FOT_Event_Managment_System.Service.EventServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EventController {
    @Autowired
    private EventServices eventServices;
    @GetMapping("/events")
    public String events(Model model) {
        model.addAttribute("events", eventServices.getEvents());
        model.addAttribute("eventForm", new Event());
        return "Events";
    }
    @GetMapping("/event/add")
    public String addEventForm(Model model) {
        model.addAttribute("eventForm", new Event());
        return "AddEvent";
    }
    @PostMapping("/event/save")
    public String SaveEvent(@ModelAttribute("eventForm") Event event) {
eventServices.addEvent(event);
return "redirect:/events";
    }
    @GetMapping("/events/delete/{id}")
    public String deleteEvent(@PathVariable("id") Long id) {
        eventServices.deleteEvent(id);
        return "redirect:/events";
    }
    @GetMapping("/events/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Event existingEvent = eventServices.getEventById(id);
        model.addAttribute("eventForm", existingEvent);
        return "AddEvent";}
}
