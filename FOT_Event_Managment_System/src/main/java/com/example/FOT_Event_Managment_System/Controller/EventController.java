package com.example.FOT_Event_Managment_System.Controller;

import com.example.FOT_Event_Managment_System.Model.Event;
import com.example.FOT_Event_Managment_System.Service.EventServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EventController {
    @Autowired
    private EventServices eventServices;
    @GetMapping("/events")
    public String events(Model model) {
        model.addAttribute("events", eventServices.getEvents());
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
}
