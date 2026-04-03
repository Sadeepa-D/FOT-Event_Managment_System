package com.example.FOT_Event_Managment_System.Model;

import jakarta.persistence.*;

@Entity
@Table(name="eventrejister")
public class EventRegi {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String eventName;
    private String organizerName;
    private String Pname;
    private String pRegistrationnnum;
    private String pnumber;
    private int pslots;
    private Long eventId;
    private String registatus = "Pending";
    private String checkinstatus = "Pending";

    public EventRegi() {
        this.registatus = "Pending";
        this.checkinstatus = "Pending";
    }

    public EventRegi(Long id, String eventName, String organizerName, String pname, String pRegistrationnnum, String pnumber, int pslots, Long eventId, String registatus) {
        this.id = id;
        this.eventName = eventName;
        this.organizerName = organizerName;
        this.Pname = pname;
        this.pRegistrationnnum = pRegistrationnnum;
        this.pnumber = pnumber;
        this.pslots = pslots;
        this.eventId = eventId;
        this.registatus = (registatus != null) ? registatus : "Pending";
        this.checkinstatus = (checkinstatus != null) ? checkinstatus : "Pending";
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

    public String getPname() {
        return Pname;
    }

    public String getpRegistrationnnum() {
        return pRegistrationnnum;
    }

    public void setpRegistrationnnum(String pRegistrationnnum) {
        this.pRegistrationnnum = pRegistrationnnum;
    }

    public void setPname(String pname) {
        Pname = pname;
    }

    public String getPnumber() {
        return pnumber;
    }

    public void setPnumber(String pnumber) {
        this.pnumber = pnumber;
    }

    public int getPslots() {
        return pslots;
    }

    public void setPslots(int pslots) {
        this.pslots = pslots;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getRegistatus() {
        return registatus;
    }

    public void setRegistatus(String registatus) {
        this.registatus = registatus;
    }

    public String getCheckinstatus() {
        return checkinstatus;
    }

    public void setCheckinstatus(String checkinstatus) {
        this.checkinstatus = checkinstatus;
    }
}
