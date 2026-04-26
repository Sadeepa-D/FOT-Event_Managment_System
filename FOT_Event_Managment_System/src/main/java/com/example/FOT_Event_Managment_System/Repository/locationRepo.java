package com.example.FOT_Event_Managment_System.Repository;

import com.example.FOT_Event_Managment_System.Model.location;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface locationRepo extends JpaRepository<location, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE location l SET l.availability = 'Disable' WHERE l.id = ?1")
    void disableLocation(Long id);
}