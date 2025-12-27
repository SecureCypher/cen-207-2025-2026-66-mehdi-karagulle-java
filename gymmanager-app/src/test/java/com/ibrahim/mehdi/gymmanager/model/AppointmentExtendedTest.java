package com.ibrahim.mehdi.gymmanager.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;

@DisplayName("Appointment Extended Tests")
public class AppointmentExtendedTest {
    
    @Test
    public void testStatusTransitions() {
        LocalDateTime time = LocalDateTime.now();
        Appointment apt = new Appointment(1, 100, "Test User", time, "Service", 5);
        
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
    public void testPriorityComparison() {
        LocalDateTime time = LocalDateTime.now();
        
        Appointment apt1 = new Appointment(1, 100, "User1", time, "S1", 1);
        Appointment apt2 = new Appointment(2, 101, "User2", time, "S2", 5);
        Appointment apt3 = new Appointment(3, 102, "User3", time, "S3", 10);
        
        assertTrue(apt1.compareTo(apt2) < 0);
        assertTrue(apt2.compareTo(apt3) < 0);
        assertTrue(apt3.compareTo(apt1) > 0);
        assertEquals(0, apt1.compareTo(apt1));
    }
    
    @Test
    public void testSamePriority() {
        LocalDateTime time = LocalDateTime.now();
        
        Appointment apt1 = new Appointment(1, 100, "User1", time, "S1", 5);
        Appointment apt2 = new Appointment(2, 101, "User2", time, "S2", 5);
        
        assertEquals(0, apt1.compareTo(apt2));
    }
    
    @Test
    public void testSetters() {
        Appointment apt = new Appointment();
        
        apt.setId(999);
        assertEquals(999, apt.getId());
        
        apt.setMemberId(777);
        assertEquals(777, apt.getMemberId());
        
        apt.setMemberName("John Doe");
        assertEquals("John Doe", apt.getMemberName());
        
        apt.setService("Training");
        assertEquals("Training", apt.getService());
        
        apt.setPriority(3);
        assertEquals(3, apt.getPriority());
        
        LocalDateTime newTime = LocalDateTime.now().plusDays(5);
        apt.setAppointmentTime(newTime);
        assertEquals(newTime, apt.getAppointmentTime());
    }
    
    @Test
    public void testToString() {
        LocalDateTime time = LocalDateTime.now();
        Appointment apt = new Appointment(1, 100, "Alice", time, "Yoga", 1);
        
        String str = apt.toString();
        assertTrue(str.contains("Alice"));
        assertTrue(str.contains("PENDING"));
        
        apt.setStatus(Appointment.AppointmentStatus.CONFIRMED);
        assertTrue(apt.toString().contains("CONFIRMED"));
    }
    
    @Test
    public void testExtremePriorities() {
        LocalDateTime time = LocalDateTime.now();
        
        Appointment aptMin = new Appointment(1, 100, "User", time, "S", Integer.MIN_VALUE);
        Appointment aptMax = new Appointment(2, 101, "User", time, "S", Integer.MAX_VALUE);
        
        assertEquals(Integer.MIN_VALUE, aptMin.getPriority());
        assertEquals(Integer.MAX_VALUE, aptMax.getPriority());
        assertTrue(aptMin.compareTo(aptMax) < 0);
    }
    
    @Test
    public void testPastAndFutureTimes() {
        Appointment apt = new Appointment();
        
        LocalDateTime pastTime = LocalDateTime.now().minusDays(30);
        apt.setAppointmentTime(pastTime);
        assertEquals(pastTime, apt.getAppointmentTime());
        
        LocalDateTime futureTime = LocalDateTime.now().plusMonths(2);
        apt.setAppointmentTime(futureTime);
        assertEquals(futureTime, apt.getAppointmentTime());
    }
    
    @Test
    public void testSpecialCharacters() {
        LocalDateTime time = LocalDateTime.now();
        String special = "Training @#$%";
        
        Appointment apt = new Appointment(1, 100, "User", time, special, 5);
        assertEquals(special, apt.getService());
    }
}