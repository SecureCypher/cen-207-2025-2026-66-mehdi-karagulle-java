package com.ibrahim.mehdi.gymmanager.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 
 */
@DisplayName("MembershipType Enum - Complete Tests")
public class MembershipTypeTest {
    
    @Test
    @DisplayName("Should have correct prices for all types")
    public void testPrices() {
        assertEquals(1000, Member.MembershipType.MONTHLY.getPrice());
        assertEquals(2700, Member.MembershipType.QUARTERLY.getPrice());
        assertEquals(9600, Member.MembershipType.YEARLY.getPrice());
        assertEquals(15000, Member.MembershipType.VIP.getPrice());
    }
    
    @Test
    @DisplayName("Should have correct durations")
    public void testDurations() {
        assertEquals(1, Member.MembershipType.MONTHLY.getDurationMonths());
        assertEquals(3, Member.MembershipType.QUARTERLY.getDurationMonths());
        assertEquals(12, Member.MembershipType.YEARLY.getDurationMonths());
        assertEquals(12, Member.MembershipType.VIP.getDurationMonths());
    }
    
    @Test
    @DisplayName("Should format price text correctly")
    public void testPriceText() {
        assertEquals("1000₺", Member.MembershipType.MONTHLY.getPriceText());
        assertEquals("2700₺", Member.MembershipType.QUARTERLY.getPriceText());
        assertEquals("9600₺", Member.MembershipType.YEARLY.getPriceText());
        assertEquals("15000₺", Member.MembershipType.VIP.getPriceText());
    }
    
    @Test
    @DisplayName("Should provide full description")
    public void testDescription() {
        String monthly = Member.MembershipType.MONTHLY.getDescription();
        assertTrue(monthly.contains("MONTHLY"));
        assertTrue(monthly.contains("1 ay"));
        assertTrue(monthly.contains("1000₺"));
        
        String quarterly = Member.MembershipType.QUARTERLY.getDescription();
        assertTrue(quarterly.contains("QUARTERLY"));
        assertTrue(quarterly.contains("3 ay"));
        assertTrue(quarterly.contains("2700₺"));
        
        String yearly = Member.MembershipType.YEARLY.getDescription();
        assertTrue(yearly.contains("YEARLY"));
        assertTrue(yearly.contains("12 ay"));
        assertTrue(yearly.contains("9600₺"));
        
        String vip = Member.MembershipType.VIP.getDescription();
        assertTrue(vip.contains("VIP"));
        assertTrue(vip.contains("12 ay"));
        assertTrue(vip.contains("15000₺"));
    }
    
    @Test
    @DisplayName("Should have all enum values")
    public void testEnumValues() {
        Member.MembershipType[] types = Member.MembershipType.values();
        assertEquals(4, types.length);
        
        assertEquals(Member.MembershipType.MONTHLY, types[0]);
        assertEquals(Member.MembershipType.QUARTERLY, types[1]);
        assertEquals(Member.MembershipType.YEARLY, types[2]);
        assertEquals(Member.MembershipType.VIP, types[3]);
    }
    
    @Test
    @DisplayName("Should support valueOf")
    public void testValueOf() {
        assertEquals(Member.MembershipType.MONTHLY, 
            Member.MembershipType.valueOf("MONTHLY"));
        assertEquals(Member.MembershipType.QUARTERLY, 
            Member.MembershipType.valueOf("QUARTERLY"));
        assertEquals(Member.MembershipType.YEARLY, 
            Member.MembershipType.valueOf("YEARLY"));
        assertEquals(Member.MembershipType.VIP, 
            Member.MembershipType.valueOf("VIP"));
    }
    
    @Test
    @DisplayName("Should throw exception for invalid valueOf")
    public void testInvalidValueOf() {
        assertThrows(IllegalArgumentException.class, () -> {
            Member.MembershipType.valueOf("INVALID");
        });
    }
    
    @Test
    @DisplayName("Should have correct discount calculations")
    public void testDiscounts() {
        // MONTHLY: 1000 TL/month → 1000 TL total
        int monthlyPerMonth = 1000;
        assertEquals(monthlyPerMonth, Member.MembershipType.MONTHLY.getPrice());
        
        // QUARTERLY: 2700 TL for 3 months → 900 TL/month (10% discount)
        int quarterlyPerMonth = Member.MembershipType.QUARTERLY.getPrice() / 3;
        assertEquals(900, quarterlyPerMonth);
        assertTrue(quarterlyPerMonth < monthlyPerMonth);
        
        // YEARLY: 9600 TL for 12 months → 800 TL/month (20% discount)
        int yearlyPerMonth = Member.MembershipType.YEARLY.getPrice() / 12;
        assertEquals(800, yearlyPerMonth);
        assertTrue(yearlyPerMonth < quarterlyPerMonth);
        
        // VIP: Premium pricing
        int vipPerMonth = Member.MembershipType.VIP.getPrice() / 12;
        assertEquals(1250, vipPerMonth);
        assertTrue(vipPerMonth > yearlyPerMonth);
    }
    
    @Test
    @DisplayName("Should verify enum name methods")
    public void testEnumNames() {
        assertEquals("MONTHLY", Member.MembershipType.MONTHLY.name());
        assertEquals("QUARTERLY", Member.MembershipType.QUARTERLY.name());
        assertEquals("YEARLY", Member.MembershipType.YEARLY.name());
        assertEquals("VIP", Member.MembershipType.VIP.name());
    }
    
    @Test
    @DisplayName("Should verify enum toString")
    public void testEnumToString() {
        assertNotNull(Member.MembershipType.MONTHLY.toString());
        assertNotNull(Member.MembershipType.QUARTERLY.toString());
        assertNotNull(Member.MembershipType.YEARLY.toString());
        assertNotNull(Member.MembershipType.VIP.toString());
    }
}