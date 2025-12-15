package com.ibrahim.mehdi.gymmanager.service;

import com.ibrahim.mehdi.gymmanager.model.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Test class for GymService
 * 
 * @author ibrahim.mehdi
 */
public class GymServiceTest {
    
    private GymService service;
    
    @BeforeEach
    public void setUp() {
        service = new GymService();
    }
    
    @Test
    @DisplayName("Add and Search Member")
    public void testAddAndSearchMember() {
        Member member = service.addMember("Test", "User", "555-1234", 
            "test@test.com", Member.MembershipType.MONTHLY);
        
        assertNotNull(member);
        assertEquals("Test", member.getName());
        
        Member found = service.searchMember(member.getId());
        assertNotNull(found);
        assertEquals(member.getId(), found.getId());
    }
    
    @Test
    @DisplayName("KMP Search Members by Name")
    public void testSearchMemberByName() {
        service.addMember("Ahmet", "Yılmaz", "555-0001", "a@test.com", 
            Member.MembershipType.MONTHLY);
        service.addMember("Mehmet", "Kaya", "555-0002", "m@test.com", 
            Member.MembershipType.YEARLY);
        
        List<Member> results = service.searchMemberByName("met");
        assertTrue(results.size() >= 2);
    }
    
    @Test
    @DisplayName("Create and Process Appointment")
    public void testAppointmentManagement() {
        Member member = service.addMember("Test", "User", "555-1234", 
            "test@test.com", Member.MembershipType.MONTHLY);
        
        Appointment apt = service.createAppointment(member.getId(), "Training", 1);
        assertNotNull(apt);
        
        Appointment next = service.getNextAppointment();
        assertNotNull(next);
        assertEquals(apt.getId(), next.getId());
        
        Appointment processed = service.processNextAppointment();
        assertNotNull(processed);
        assertEquals(Appointment.AppointmentStatus.COMPLETED, processed.getStatus());
    }
    
    @Test
    @DisplayName("Equipment Management with Sparse Matrix")
    public void testEquipmentManagement() {
        Equipment equip = service.addEquipment("Treadmill", "Cardio", 5, 10, 10);
        assertNotNull(equip);
        
        Equipment found = service.getEquipmentAt(10, 10);
        assertNotNull(found);
        assertEquals("Treadmill", found.getName());
        
        Equipment notFound = service.getEquipmentAt(5, 5);
        assertNull(notFound);
    }
    
    @Test
    @DisplayName("Waiting Queue Operations")
    public void testWaitingQueue() {
        Member m1 = service.addMember("User1", "Test", "555-0001", 
            "u1@test.com", Member.MembershipType.MONTHLY);
        Member m2 = service.addMember("User2", "Test", "555-0002", 
            "u2@test.com", Member.MembershipType.MONTHLY);
        
        service.addToWaitingQueue(m1);
        service.addToWaitingQueue(m2);
        
        assertEquals(2, service.getQueueSize());
        
        Member processed = service.processNextInQueue();
        assertEquals(m1.getId(), processed.getId());
        assertEquals(1, service.getQueueSize());
    }
    
    @Test
    @DisplayName("History Navigation")
    public void testHistoryNavigation() {
        service.addMember("User1", "Test", "555-0001", 
            "u1@test.com", Member.MembershipType.MONTHLY);
        service.addMember("User2", "Test", "555-0002", 
            "u2@test.com", Member.MembershipType.MONTHLY);
        
        String current = service.getCurrentHistory();
        assertNotNull(current);
        
        String forward = service.navigateHistoryForward();
        // May be null if at end
        
        String backward = service.navigateHistoryBackward();
        // May be null if at start
    }
    
    @Test
    @DisplayName("Undo Operations")
    public void testUndoOperations() {
        service.addMember("User", "Test", "555-0001", 
            "u@test.com", Member.MembershipType.MONTHLY);
        
        String undone = service.undo();
        assertNotNull(undone);
        assertTrue(undone.contains("member"));
    }
    
    @Test
    @DisplayName("Data Compression with Huffman")
    public void testDataCompression() {
        String data = "This is test data for compression";
        String compressed = service.compressData(data);
        assertNotNull(compressed);
        
        String decompressed = service.decompressData(compressed);
        assertEquals(data, decompressed);
    }
    
    @Test
    @DisplayName("Statistics Generation")
    public void testStatistics() {
        var stats = service.getStatistics();
        assertNotNull(stats);
        assertTrue(stats.containsKey("Total Members"));
        assertTrue(stats.containsKey("Total Equipment"));
    }
}
