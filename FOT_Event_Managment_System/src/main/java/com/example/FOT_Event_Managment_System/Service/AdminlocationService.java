package com.example.FOT_Event_Managment_System.Service;

import com.example.FOT_Event_Managment_System.Model.location;
import com.example.FOT_Event_Managment_System.Repository.locationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminlocationService {

    @Autowired
    private locationRepo locationRepo;

    // Get all locations
    public List<location> getAllLocations() {
        return locationRepo.findAll();
    }

    // Save location
    public location saveLocation(location loc) {
        return locationRepo.save(loc);
    }

    // Get by ID
    public location getLocationById(Long id) {
        return locationRepo.findById(id).orElse(null);
    }

    // Delete location
    public void deleteLocation(Long id) {
        locationRepo.deleteById(id);
    }
}