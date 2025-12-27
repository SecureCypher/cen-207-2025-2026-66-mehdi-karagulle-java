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
 // =====================================================================
 // ADIM 1: MemberTest.java dosyasını aç
 // ADIM 2: Dosyanın SON } işaretinden ÖNCE bu kodu yapıştır
 // ADIM 3: Kaydet ve test et
 // =====================================================================

     // ==================== BRANCH COVERAGE BOOST ====================
     
     @Test
     @DisplayName("Should update date when type changes")
     public void testTypeChangeUpdatesDate() {
         Member m = new Member(1, "T", "U", "555", "t@t.com", Member.MembershipType.MONTHLY);
         LocalDate original = m.getMembershipEndDate();
         m.setMembershipType(Member.MembershipType.QUARTERLY);
         assertNotEquals(original, m.getMembershipEndDate());
         assertEquals(2700, m.getMembershipPrice());
     }
     
     @Test
     @DisplayName("Should check expired with past date")
     public void testExpiredPastDate() {
         Member m = new Member(1, "T", "U", "555", "t@t.com", Member.MembershipType.MONTHLY);
         m.setMembershipEndDate(LocalDate.now().minusDays(5));
         assertTrue(m.isMembershipExpired());
     }
     
     @Test
     @DisplayName("Should check not expired with future")
     public void testNotExpiredFuture() {
         Member m = new Member(1, "T", "U", "555", "t@t.com", Member.MembershipType.MONTHLY);
         m.setMembershipEndDate(LocalDate.now().plusDays(10));
         assertFalse(m.isMembershipExpired());
     }
     
     @Test
     @DisplayName("Should check expiry today")
     public void testExpiryToday() {
         Member m = new Member(1, "T", "U", "555", "t@t.com", Member.MembershipType.MONTHLY);
         m.setMembershipEndDate(LocalDate.now());
         assertFalse(m.isMembershipExpired());
     }
     
     @Test
     @DisplayName("Should calculate days positive")
     public void testDaysPositive() {
         Member m = new Member(1, "T", "U", "555", "t@t.com", Member.MembershipType.MONTHLY);
         m.setMembershipEndDate(LocalDate.now().plusDays(10));
         assertEquals(10, m.getDaysUntilExpiry());
     }
     
     @Test
     @DisplayName("Should calculate days negative")
     public void testDaysNegative() {
         Member m = new Member(1, "T", "U", "555", "t@t.com", Member.MembershipType.MONTHLY);
         m.setMembershipEndDate(LocalDate.now().minusDays(5));
         assertEquals(-5, m.getDaysUntilExpiry());
     }
     
     @Test
     @DisplayName("Should calculate days zero")
     public void testDaysZero() {
         Member m = new Member(1, "T", "U", "555", "t@t.com", Member.MembershipType.MONTHLY);
         m.setMembershipEndDate(LocalDate.now());
         assertEquals(0, m.getDaysUntilExpiry());
     }
     
     @Test
     @DisplayName("Should compare different IDs")
     public void testCompareDifferentIds() {
         Member m1 = new Member(5, "A", "A", "555", "a@a.com", Member.MembershipType.MONTHLY);
         Member m2 = new Member(10, "B", "B", "555", "b@b.com", Member.MembershipType.MONTHLY);
         assertTrue(m1.compareTo(m2) < 0);
         assertTrue(m2.compareTo(m1) > 0);
     }
     
     @Test
     @DisplayName("Should compare with self")
     public void testCompareWithSelf() {
         Member m = new Member(1, "T", "U", "555", "t@t.com", Member.MembershipType.MONTHLY);
         assertEquals(0, m.compareTo(m));
     }
     
     @Test
     @DisplayName("Should not equal null")
     public void testNotEqualsNull() {
         Member m = new Member(1, "T", "U", "555", "t@t.com", Member.MembershipType.MONTHLY);
         assertNotEquals(m, null);
     }
     
     @Test
     @DisplayName("Should not equal different class")
     public void testNotEqualsDifferentClass() {
         Member m = new Member(1, "T", "U", "555", "t@t.com", Member.MembershipType.MONTHLY);
         assertNotEquals(m, "String");
         assertNotEquals(m, Integer.valueOf(1));
     }
     
     @Test
     @DisplayName("Should equal same ID different data")
     public void testEqualsSameIdDifferentData() {
         Member m1 = new Member(50, "A", "S", "111", "a@t.com", Member.MembershipType.MONTHLY);
         Member m2 = new Member(50, "B", "J", "222", "b@t.com", Member.MembershipType.VIP);
         assertEquals(m1, m2);
         assertEquals(m1.hashCode(), m2.hashCode());
     }
     
     @Test
     @DisplayName("Should handle all type transitions")
     public void testAllTypeTransitions() {
         Member m = new Member(1, "T", "U", "555", "t@t.com", Member.MembershipType.MONTHLY);
         m.setMembershipType(Member.MembershipType.QUARTERLY);
         assertEquals(Member.MembershipType.QUARTERLY, m.getMembershipType());
         m.setMembershipType(Member.MembershipType.YEARLY);
         assertEquals(Member.MembershipType.YEARLY, m.getMembershipType());
         m.setMembershipType(Member.MembershipType.VIP);
         assertEquals(Member.MembershipType.VIP, m.getMembershipType());
         m.setMembershipType(Member.MembershipType.MONTHLY);
         assertEquals(Member.MembershipType.MONTHLY, m.getMembershipType());
     }
     
     @Test
     @DisplayName("Should verify price with type")
     public void testPriceWithType() {
         Member m = new Member(1, "T", "U", "555", "t@t.com", Member.MembershipType.MONTHLY);
         assertEquals(1000, m.getMembershipPrice());
         m.setMembershipType(Member.MembershipType.QUARTERLY);
         assertEquals(2700, m.getMembershipPrice());
         m.setMembershipType(Member.MembershipType.YEARLY);
         assertEquals(9600, m.getMembershipPrice());
         m.setMembershipType(Member.MembershipType.VIP);
         assertEquals(15000, m.getMembershipPrice());
     }
     
     @Test
     @DisplayName("Should handle extreme IDs")
     public void testExtremeIds() {
         Member m1 = new Member(Integer.MAX_VALUE, "Max", "ID", "555", "m@t.com", Member.MembershipType.MONTHLY);
         Member m2 = new Member(Integer.MIN_VALUE, "Min", "ID", "555", "m@t.com", Member.MembershipType.MONTHLY);
         Member m3 = new Member(0, "Zero", "ID", "555", "z@t.com", Member.MembershipType.MONTHLY);
         assertEquals(Integer.MAX_VALUE, m1.getId());
         assertEquals(Integer.MIN_VALUE, m2.getId());
         assertEquals(0, m3.getId());
         assertTrue(m2.compareTo(m1) < 0);
         assertTrue(m3.compareTo(m1) < 0);
     }

}