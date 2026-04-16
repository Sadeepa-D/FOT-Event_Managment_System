package com.example.FOT_Event_Managment_System.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventName;
    private String organizerName;
    private String venue;
    private String date;
    private String time;
    private Double ticketPrice = 0.00;
    private Integer availableSlots;
    private String description;
    private Long organizerId;
    private String eventstatus="PENDING";
    private String organizerPhone;

    public Event() {
        this.eventstatus="PENDING";
        this.ticketPrice = 0.00;
    }

    public Event(String description, Integer availableSlots, Double ticketPrice, String time, String date, String venue, String organizerName, String eventName, Long id, String eventstatus, String organizerPhone, Long organizerId) {
        this.description = description;
        this.availableSlots = availableSlots;
        this.ticketPrice = ticketPrice;
        this.time = time;
        this.date = date;
        this.venue = venue;
        this.organizerName = organizerName;
        this.eventName = eventName;
        this.id = id;
        this.eventstatus = eventstatus;
        this.organizerPhone = organizerPhone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(Double ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public Integer getAvailableSlots() {
        return availableSlots;
    }

    public void setAvailableSlots(Integer availableSlots) {
        this.availableSlots = availableSlots;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOrganizerId(Long uId) {
        this.organizerId = uId;
    }

    public String getEventstatus() {
        return eventstatus;
    }

    public void setEventstatus(String eventstatus) {
        this.eventstatus = eventstatus;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public String getOrganizerPhone() {
        return organizerPhone;
    }

    public void setOrganizerPhone(String organizerPhone) {
        this.organizerPhone = organizerPhone;
    }
}
