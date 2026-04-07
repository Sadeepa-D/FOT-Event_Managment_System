package com.example.FOT_Event_Managment_System.Model;

import jakarta.persistence.*;

@Entity
@Table(name="location")
public class location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
            private long id;

            private String locationName;
            private Integer capacity;
            private String availability;

    public location() {
        this.availability = "available";
    }

    public location(String locationName, Integer capacity, String availability, long id) {
        this.id = id;
        this.locationName = locationName;
        this.capacity = capacity;
        this.availability = availability;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }
}
