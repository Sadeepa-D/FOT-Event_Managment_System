package com.example.FOT_Event_Managment_System.Repository;

import com.example.FOT_Event_Managment_System.Model.Event;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventRepo extends JpaRepository<Event,Long> {
    List<Event> findByOrganizerId(Long organizerId);
    List<Event> findByOrganizerIdAndEventstatus(Long organizerId, String eventstatus);
    List<Event> findByEventstatus(String status);

    // Custom Query to update status to DELETED
    @Modifying
    @Transactional
    @Query("UPDATE Event e SET e.eventstatus = 'DELETED' WHERE e.id = :id")
    void softDeleteEvent(@Param("id") Long id);
}
