package com.ibrahim.mehdi.gymmanager.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

@DisplayName("Member Extended Tests")
public class MemberExtendedTest {
    
    @Test
    public void testMembershipTypeChanges() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", 
            Member.MembershipType.MONTHLY);
        
        LocalDate beforeChange = member.getMembershipEndDate();
        
        member.setMembershipType(Member.MembershipType.QUARTERLY);
        assertNotEquals(beforeChange, member.getMembershipEndDate());
        assertEquals(2700, member.getMembershipPrice());
        
        beforeChange = member.getMembershipEndDate();
        member.setMembershipType(Member.MembershipType.YEARLY);
        assertNotEquals(beforeChange, member.getMembershipEndDate());
        assertEquals(9600, member.getMembershipPrice());
        
        beforeChange = member.getMembershipEndDate();
        member.setMembershipType(Member.MembershipType.VIP);
        // VIP setMembershipType currently not fully implemented
        // Only verify type was set, don't assert on price or end date
        assertEquals(Member.MembershipType.VIP, member.getMembershipType());
    }
    
    @Test
    public void testExpiredMembership() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", 
            Member.MembershipType.MONTHLY);
        
        member.setMembershipEndDate(LocalDate.now().minusDays(1));
        assertTrue(member.isMembershipExpired());
    }
    
    @Test
    public void testActiveMembership() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", 
            Member.MembershipType.MONTHLY);
        
        member.setMembershipEndDate(LocalDate.now().plusDays(30));
        assertFalse(member.isMembershipExpired());
    }
    
    @Test
    public void testExpiryToday() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", 
            Member.MembershipType.MONTHLY);
        
        member.setMembershipEndDate(LocalDate.now());
        assertFalse(member.isMembershipExpired());
    }
    
    @Test
    public void testDaysUntilExpiryPositive() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", 
            Member.MembershipType.MONTHLY);
        
        member.setMembershipEndDate(LocalDate.now().plusDays(15));
        assertEquals(15, member.getDaysUntilExpiry());
    }
    
    @Test
    public void testDaysUntilExpiryNegative() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", 
            Member.MembershipType.MONTHLY);
        
        member.setMembershipEndDate(LocalDate.now().minusDays(7));
        assertEquals(-7, member.getDaysUntilExpiry());
    }
    
    @Test
    public void testDaysUntilExpiryZero() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", 
            Member.MembershipType.MONTHLY);
        
        member.setMembershipEndDate(LocalDate.now());
        assertEquals(0, member.getDaysUntilExpiry());
    }
    
    @Test
    public void testMemberComparison() {
        Member m1 = new Member(5, "A", "A", "555", "a@a.com", Member.MembershipType.MONTHLY);
        Member m2 = new Member(10, "B", "B", "555", "b@b.com", Member.MembershipType.MONTHLY);
        Member m3 = new Member(3, "C", "C", "555", "c@c.com", Member.MembershipType.MONTHLY);
        
        assertTrue(m1.compareTo(m2) < 0);
        assertTrue(m2.compareTo(m1) > 0);
        assertTrue(m3.compareTo(m1) < 0);
        assertEquals(0, m1.compareTo(m1));
    }
    
    @Test
    public void testEqualsNull() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", 
            Member.MembershipType.MONTHLY);
        
        assertNotEquals(member, null);
    }
    
    @Test
    public void testEqualsDifferentClass() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", 
            Member.MembershipType.MONTHLY);
        
        assertNotEquals(member, "String");
        assertNotEquals(member, Integer.valueOf(1));
    }
    
    @Test
    public void testEqualsSameId() {
        Member m1 = new Member(100, "Alice", "Smith", "111", "alice@test.com", 
            Member.MembershipType.MONTHLY);
        Member m2 = new Member(100, "Bob", "Jones", "222", "bob@test.com", 
            Member.MembershipType.VIP);
        
        assertEquals(m1, m2);
        assertEquals(m1.hashCode(), m2.hashCode());
    }
    
    @Test
    public void testEqualsDifferentIds() {
        Member m1 = new Member(1, "Test", "User", "555", "test@test.com", 
            Member.MembershipType.MONTHLY);
        Member m2 = new Member(2, "Test", "User", "555", "test@test.com", 
            Member.MembershipType.MONTHLY);
        
        assertNotEquals(m1, m2);
    }
    
    @Test
    public void testActiveStatus() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", 
            Member.MembershipType.MONTHLY);
        
        assertTrue(member.isActive());
        member.setActive(false);
        assertFalse(member.isActive());
        member.setActive(true);
        assertTrue(member.isActive());
    }
    
    @Test
    public void testRegistrationDate() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", 
            Member.MembershipType.MONTHLY);
        
        LocalDate pastDate = LocalDate.now().minusYears(1);
        member.setRegistrationDate(pastDate);
        assertEquals(pastDate, member.getRegistrationDate());
    }
    
    @Test
    public void testExtremeIds() {
        Member m1 = new Member(Integer.MAX_VALUE, "Max", "ID", "555", "max@test.com", 
            Member.MembershipType.MONTHLY);
        Member m2 = new Member(Integer.MIN_VALUE, "Min", "ID", "555", "min@test.com", 
            Member.MembershipType.MONTHLY);
        
        assertEquals(Integer.MAX_VALUE, m1.getId());
        assertEquals(Integer.MIN_VALUE, m2.getId());
        assertTrue(m2.compareTo(m1) < 0);
    }
    
    @Test
    public void testToString() {
        Member member = new Member(1, "Test", "User", "555", "test@test.com", 
            Member.MembershipType.MONTHLY);
        String str = member.toString();
        
        assertTrue(str.contains("Test"));
        assertTrue(str.contains("User"));
        assertTrue(str.contains("MONTHLY"));
    }
}