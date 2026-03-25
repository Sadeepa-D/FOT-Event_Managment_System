package com.example.FOT_Event_Managment_System.Repository;

import com.example.FOT_Event_Managment_System.Model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<Event,Long> {
}
