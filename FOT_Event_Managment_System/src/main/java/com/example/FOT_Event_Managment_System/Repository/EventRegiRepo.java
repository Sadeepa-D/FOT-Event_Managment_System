package com.example.FOT_Event_Managment_System.Repository;

import com.example.FOT_Event_Managment_System.Model.EventRegi;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRegiRepo extends JpaRepository<EventRegi,Long> {
    @Query("SELECT e FROM EventRegi e WHERE e.eventId = :eventId AND (e.registatus IS NULL OR e.registatus != 'false')")
    List<EventRegi> findActiveParticipants(@Param("eventId") Long eventId);
    @Modifying
    @Transactional
    @Query("UPDATE EventRegi e SET e.registatus = 'false' WHERE e.id = :id")
    void updateStatusToTrue(@Param("id") Long id);
    List<EventRegi> findBypRegistrationnnum(String pRegistrationnnum);
}
