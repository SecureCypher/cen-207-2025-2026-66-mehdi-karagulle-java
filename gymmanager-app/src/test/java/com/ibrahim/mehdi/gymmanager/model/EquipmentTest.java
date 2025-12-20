package com.ibrahim.mehdi.gymmanager.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Complete Equipment test - 100% coverage
 */
@DisplayName("Equipment Model - Complete Tests")
public class EquipmentTest {
    
    @Test
    public void testDefaultConstructor() {
        Equipment equip = new Equipment();
        assertTrue(equip.isAvailable());
    }
    
    @Test
    public void testParameterizedConstructor() {
        Equipment equip = new Equipment(1, "Treadmill", "Cardio", 5);
        
        assertEquals(1, equip.getId());
        assertEquals("Treadmill", equip.getName());
        assertEquals("Cardio", equip.getType());
        assertEquals(5, equip.getQuantity());
        assertTrue(equip.isAvailable());
        assertEquals(0, equip.getLocationX());
        assertEquals(0, equip.getLocationY());
    }
    
    @Test
    public void testConstructorWithLocation() {
        Equipment equip = new Equipment(1, "Bench", "Strength", 3, 10, 15);
        
        assertEquals(10, equip.getLocationX());
        assertEquals(15, equip.getLocationY());
    }
    
    @Test
    public void testSetters() {
        Equipment equip = new Equipment();
        
        equip.setId(10);
        equip.setName("Dumbbell");
        equip.setType("Weights");
        equip.setQuantity(20);
        equip.setAvailable(false);
        equip.setLocationX(5);
        equip.setLocationY(8);
        
        assertEquals(10, equip.getId());
        assertEquals("Dumbbell", equip.getName());
        assertEquals("Weights", equip.getType());
        assertEquals(20, equip.getQuantity());
        assertFalse(equip.isAvailable());
        assertEquals(5, equip.getLocationX());
        assertEquals(8, equip.getLocationY());
    }
    
    @Test
    public void testToString() {
        Equipment equip = new Equipment(1, "Treadmill", "Cardio", 5, 10, 10);
        String str = equip.toString();
        
        assertTrue(str.contains("Treadmill"));
        assertTrue(str.contains("Cardio"));
        assertTrue(str.contains("5"));
        assertTrue(str.contains("10"));
    }
    
    @Test
    public void testEqualsAndHashCode() {
        Equipment e1 = new Equipment(1, "A", "T1", 1);
        Equipment e2 = new Equipment(1, "B", "T2", 2);
        Equipment e3 = new Equipment(2, "A", "T1", 1);
        
        assertEquals(e1, e2);
        assertNotEquals(e1, e3);
        assertEquals(e1.hashCode(), e2.hashCode());
        assertEquals(e1, e1);
        assertNotEquals(null, e1);
        assertNotEquals(e1, "String");
    }
    
    @Test
    public void testTurkishCharacters() {
        Equipment equip = new Equipment(1, "Koşu Bandı", "Kardiyo", 3, 5, 5);
        assertEquals("Koşu Bandı", equip.getName());
    }
}