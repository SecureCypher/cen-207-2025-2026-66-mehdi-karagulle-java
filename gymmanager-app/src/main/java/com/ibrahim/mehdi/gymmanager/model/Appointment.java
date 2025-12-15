package com.ibrahim.mehdi.gymmanager.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Appointment class for gym appointments.
 * Used in Priority Queue (Heap) implementation.
 * 
 * @author ibrahim.mehdi
 */
public class Appointment implements Serializable, Comparable<Appointment> {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private int memberId;
    private String memberName;
    private LocalDateTime appointmentTime;
    private String service;
    private int priority; // Lower number = higher priority
    private AppointmentStatus status;
    
    /**
     * Appointment status enumeration
     */
    public enum AppointmentStatus {
        PENDING, CONFIRMED, COMPLETED, CANCELLED
    }
    
    /**
     * Default constructor
     */
    public Appointment() {
        this.status = AppointmentStatus.PENDING;
    }
    
    /**
     * Parameterized constructor
     */
    public Appointment(int id, int memberId, String memberName, 
                      LocalDateTime appointmentTime, String service, int priority) {
        this.id = id;
        this.memberId = memberId;
        this.memberName = memberName;
        this.appointmentTime = appointmentTime;
        this.service = service;
        this.priority = priority;
        this.status = AppointmentStatus.PENDING;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }
    
    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }
    
    public LocalDateTime getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(LocalDateTime appointmentTime) { 
        this.appointmentTime = appointmentTime; 
    }
    
    public String getService() { return service; }
    public void setService(String service) { this.service = service; }
    
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    
    @Override
    public int compareTo(Appointment other) {
        // Lower priority number means higher priority
        return Integer.compare(this.priority, other.priority);
    }
    
    @Override
    public String toString() {
        return String.format("Appointment[ID=%d, Member=%s, Time=%s, Priority=%d, Status=%s]",
                id, memberName, appointmentTime, priority, status);
    }
}
