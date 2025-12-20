package com.ibrahim.mehdi.gymmanager.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

/**
 * Complete Member test - 100% coverage
 */
@DisplayName("Member Model - Complete Tests")
public class MemberTest {
    
    @Test
    public void testAllMembershipTypes() {
        Member monthly = new Member(1, "A", "B", "555", "a@a.com", Member.MembershipType.MONTHLY);
        assertEquals(1000, monthly.getMembershipPrice());
        assertEquals(1, Member.MembershipType.MONTHLY.getDurationMonths());
        
        Member quarterly = new Member(2, "C", "D", "555", "c@c.com", Member.MembershipType.QUARTERLY);
        assertEquals(2700, quarterly.getMembershipPrice());
        
        Member yearly = new Member(3, "E", "F", "555", "e@e.com", Member.MembershipType.YEARLY);
        assertEquals(9600, yearly.getMembershipPrice());
        
        Member vip = new Member(4, "G", "H", "555", "g@g.com", Member.MembershipType.VIP);
        assertEquals(15000, vip.getMembershipPrice());
    }
    
    @Test
    public void testMembershipExpiry() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", Member.MembershipType.MONTHLY);
        
        assertFalse(member.isMembershipExpired());
        
        member.setMembershipEndDate(LocalDate.now().minusDays(1));
        assertTrue(member.isMembershipExpired());
    }
    
    @Test
    public void testDaysUntilExpiry() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", Member.MembershipType.MONTHLY);
        
        long days = member.getDaysUntilExpiry();
        assertTrue(days >= 0);
    }
    
    @Test
    public void testFullName() {
        Member member = new Member(1, "İbrahim", "Demirci", "555", "ibrahim@test.com", Member.MembershipType.VIP);
        assertEquals("İbrahim Demirci", member.getFullName());
    }
    
    @Test
    public void testSetMembershipTypeUpdatesEndDate() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", Member.MembershipType.MONTHLY);
        LocalDate oldEnd = member.getMembershipEndDate();
        
        member.setMembershipType(Member.MembershipType.YEARLY);
        
        assertNotEquals(oldEnd, member.getMembershipEndDate());
        assertEquals(9600, member.getMembershipPrice());
    }
    
    @Test
    public void testCompareTo() {
        Member m1 = new Member(1, "A", "A", "555", "a@a.com", Member.MembershipType.MONTHLY);
        Member m2 = new Member(2, "B", "B", "555", "b@b.com", Member.MembershipType.MONTHLY);
        
        assertTrue(m1.compareTo(m2) < 0);
        assertTrue(m2.compareTo(m1) > 0);
        assertEquals(0, m1.compareTo(m1));
    }
    
    @Test
    public void testEqualsAndHashCode() {
        Member m1 = new Member(1, "A", "A", "555", "a@a.com", Member.MembershipType.MONTHLY);
        Member m2 = new Member(1, "B", "B", "555", "b@b.com", Member.MembershipType.VIP);
        Member m3 = new Member(2, "A", "A", "555", "a@a.com", Member.MembershipType.MONTHLY);
        
        assertEquals(m1, m2);
        assertNotEquals(m1, m3);
        assertEquals(m1.hashCode(), m2.hashCode());
    }
    
    @Test
    public void testToString() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", Member.MembershipType.MONTHLY);
        String str = member.toString();
        
        assertTrue(str.contains("Test"));
        assertTrue(str.contains("User"));
        assertTrue(str.contains("MONTHLY"));
    }
    
    @Test
    public void testActiveStatus() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", Member.MembershipType.MONTHLY);
        
        assertTrue(member.isActive());
        
        member.setActive(false);
        assertFalse(member.isActive());
    }
    
    @Test
    public void testMembershipTypeDescription() {
        assertEquals("MONTHLY (1 ay - 1000₺)", Member.MembershipType.MONTHLY.getDescription());
        assertEquals("1000₺", Member.MembershipType.MONTHLY.getPriceText());
    }
    
    @Test
    public void testTurkishCharacters() {
        Member member = new Member(1, "Çağlar", "Şahin", "555", "caglar@test.com", Member.MembershipType.VIP);
        assertEquals("Çağlar Şahin", member.getFullName());
    }
}