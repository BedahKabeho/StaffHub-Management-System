package com.example.SHMS.Model;


import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate attendanceDate;

    private LocalDateTime clockInTIme;

    private LocalDateTime clockOutTIme;

    private String ipAddress;

    public String notes;

    //Constructor for clock in

    public Attendance(){}

    public Attendance(User user, LocalDateTime clockInTIme, String ipAddress) {

        this.user = user;
        this.attendanceDate = clockInTIme.toLocalDate();
        this.clockInTIme = clockInTIme;
        this.ipAddress = ipAddress;

    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public LocalDateTime getClockInTIme() {
        return clockInTIme;
    }

    public void setClockInTIme(LocalDateTime clockInTIme) {
        this.clockInTIme = clockInTIme;
    }

    public LocalDateTime getClockOutTIme() {
        return clockOutTIme;
    }

    public void setClockOutTIme(LocalDateTime clockOutTIme) {
        this.clockOutTIme = clockOutTIme;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}