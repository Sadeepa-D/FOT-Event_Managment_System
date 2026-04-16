package com.example.FOT_Event_Managment_System.Observer;

import com.example.FOT_Event_Managment_System.Model.Event;
import org.springframework.context.ApplicationEvent;

public class EventCreatedEvent extends ApplicationEvent {
    private final Event event;
    private final String triggerType;
    public EventCreatedEvent(Object source, Event event, String triggerType) {
        super(source);
        this.event = event;
        this.triggerType = triggerType;
    }

    public Event getEvent() {
        return event;
    }

    public String getTriggerType() {
        return triggerType;
    }
}
