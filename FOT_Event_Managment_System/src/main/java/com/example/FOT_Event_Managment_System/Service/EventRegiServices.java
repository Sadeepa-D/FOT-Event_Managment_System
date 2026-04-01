package com.example.FOT_Event_Managment_System.Service;

import com.example.FOT_Event_Managment_System.Model.EventRegi;
import com.example.FOT_Event_Managment_System.Repository.EventRegiRepo;
import com.example.FOT_Event_Managment_System.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventRegiServices {
    @Autowired
    private EventRegiRepo eventRegiRepo;
    @Autowired
    private UserRepo userRepo;

    public void registerforEvent(EventRegi eventRegi) {
        eventRegiRepo.save(eventRegi);
    }
    public List<EventRegi> getParticipantsByEventId(Long eventId) {
        return eventRegiRepo.findActiveParticipants(eventId);
    }
    public void updateregistrationstatus(Long eventrejiId) {
        eventRegiRepo.updateStatusToTrue(eventrejiId);
    }
    public void updateregistrationstatustoapprove(Long eventrejiId) {
        eventRegiRepo.updateStatusToApproved(eventrejiId);
    }
}
