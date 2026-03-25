package com.example.FOT_Event_Managment_System.Service;

import com.example.FOT_Event_Managment_System.Model.EventRegi;
import com.example.FOT_Event_Managment_System.Repository.EventRegiRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventRegiServices {
    @Autowired
    private EventRegiRepo eventRegiRepo;
    public void registerforEvent(EventRegi eventRegi) {
        eventRegiRepo.save(eventRegi);
    }
}
