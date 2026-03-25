package com.example.FOT_Event_Managment_System.Repository;

import com.example.FOT_Event_Managment_System.Model.EventRegi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRegiRepo extends JpaRepository<EventRegi,Long> {
    List<EventRegi> findByEventId(Long eventId);
}
