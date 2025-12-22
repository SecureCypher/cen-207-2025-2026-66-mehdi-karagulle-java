package com.ibrahim.mehdi.gymmanager.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

@DisplayName("Appointment Model - Complete Tests")
public class AppointmentTest {
    
    @Test
    public void testConstructorAndGetters() {
        LocalDateTime time = LocalDateTime.now();
        Appointment apt = new Appointment(1, 100, "Test User", time, "Training", 5);
        
        assertEquals(1, apt.getId());
        assertEquals(100, apt.getMemberId());
        assertEquals("Test User", apt.getMemberName());
        assertEquals(time, apt.getAppointmentTime());
        assertEquals("Training", apt.getService());
        assertEquals(5, apt.getPriority());
    }
    
    @Test
    public void testDefaultConstructor() {
        Appointment apt = new Appointment();
        assertEquals(Appointment.AppointmentStatus.PENDING, apt.getStatus());
    }
    
    @Test
    public void testSetters() {
        Appointment apt = new Appointment();
        
        apt.setId(10);
        apt.setMemberId(200);
        apt.setMemberName("New Name");
        apt.setService("New Service");
        apt.setPriority(3);
        apt.setStatus(Appointment.AppointmentStatus.COMPLETED);
        
        LocalDateTime newTime = LocalDateTime.now().plusDays(5);
        apt.setAppointmentTime(newTime);
        
        assertEquals(10, apt.getId());
        assertEquals(200, apt.getMemberId());
        assertEquals("New Name", apt.getMemberName());
        assertEquals("New Service", apt.getService());
        assertEquals(3, apt.getPriority());
        assertEquals(newTime, apt.getAppointmentTime());
        assertEquals(Appointment.AppointmentStatus.COMPLETED, apt.getStatus());
    }
    
    @Test
    public void testAllStatuses() {
        Appointment apt = new Appointment();
        
        apt.setStatus(Appointment.AppointmentStatus.PENDING);
        assertEquals(Appointment.AppointmentStatus.PENDING, apt.getStatus());
        
        apt.setStatus(Appointment.AppointmentStatus.CONFIRMED);
        assertEquals(Appointment.AppointmentStatus.CONFIRMED, apt.getStatus());
        
        apt.setStatus(Appointment.AppointmentStatus.COMPLETED);
        assertEquals(Appointment.AppointmentStatus.COMPLETED, apt.getStatus());
        
        apt.setStatus(Appointment.AppointmentStatus.CANCELLED);
        assertEquals(Appointment.AppointmentStatus.CANCELLED, apt.getStatus());
    }
    
    @Test
    public void testCompareToPriority() {
        Appointment apt1 = new Appointment(1, 100, "User", LocalDateTime.now(), "S1", 1);
        Appointment apt2 = new Appointment(2, 101, "User", LocalDateTime.now(), "S2", 5);
        Appointment apt3 = new Appointment(3, 102, "User", LocalDateTime.now(), "S3", 10);
        
        assertTrue(apt1.compareTo(apt2) < 0);
        assertTrue(apt2.compareTo(apt1) > 0);
        assertTrue(apt2.compareTo(apt3) < 0);
        assertEquals(0, apt1.compareTo(apt1));
    }
    
    @Test
    public void testToString() {
        LocalDateTime time = LocalDateTime.now();
        Appointment apt = new Appointment(1, 100, "Test User", time, "Training", 5);
        String str = apt.toString();
        
        // Just verify toString returns something meaningful
        assertNotNull(str);
        assertTrue(str.length() > 0);
        assertTrue(str.contains("Appointment"));
    }
}