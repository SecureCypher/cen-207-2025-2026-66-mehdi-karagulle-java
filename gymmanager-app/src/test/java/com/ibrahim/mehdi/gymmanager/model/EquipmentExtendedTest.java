package com.ibrahim.mehdi.gymmanager.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Equipment Extended Tests")
public class EquipmentExtendedTest {
    
    @Test
    public void testLocationCorners() {
        Equipment e1 = new Equipment(1, "E1", "T1", 1, 0, 0);
        assertEquals(0, e1.getLocationX());
        assertEquals(0, e1.getLocationY());
        
        Equipment e2 = new Equipment(2, "E2", "T2", 1, 19, 19);
        assertEquals(19, e2.getLocationX());
        assertEquals(19, e2.getLocationY());
    }
    
    @Test
    public void testNegativeLocations() {
        Equipment equipment = new Equipment();
        
        equipment.setLocationX(-5);
        equipment.setLocationY(-10);
        
        assertEquals(-5, equipment.getLocationX());
        assertEquals(-10, equipment.getLocationY());
    }
    
    @Test
    public void testExtremeLocations() {
        Equipment equipment = new Equipment();
        
        equipment.setLocationX(Integer.MAX_VALUE);
        equipment.setLocationY(Integer.MAX_VALUE);
        
        assertEquals(Integer.MAX_VALUE, equipment.getLocationX());
        assertEquals(Integer.MAX_VALUE, equipment.getLocationY());
    }
    
    @Test
    public void testZeroQuantity() {
        Equipment equipment = new Equipment(1, "Broken", "Maintenance", 0);
        assertEquals(0, equipment.getQuantity());
    }
    
    @Test
    public void testNegativeQuantity() {
        Equipment equipment = new Equipment();
        equipment.setQuantity(-5);
        assertEquals(-5, equipment.getQuantity());
    }
    
    @Test
    public void testLargeQuantity() {
        Equipment equipment = new Equipment(1, "Dumbbells", "Weights", Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, equipment.getQuantity());
    }
    
    @Test
    public void testEmptyStrings() {
        Equipment equipment = new Equipment(1, "", "", 1);
        assertEquals("", equipment.getName());
        assertEquals("", equipment.getType());
    }
    
    @Test
    public void testNullStrings() {
        Equipment equipment = new Equipment();
        equipment.setName(null);
        equipment.setType(null);
        
        assertNull(equipment.getName());
        assertNull(equipment.getType());
    }
    
    @Test
    public void testAvailability() {
        Equipment equipment = new Equipment(1, "Test", "Test", 1);
        
        assertTrue(equipment.isAvailable());
        equipment.setAvailable(false);
        assertFalse(equipment.isAvailable());
        equipment.setAvailable(true);
        assertTrue(equipment.isAvailable());
    }
    
    @Test
    public void testEqualsNull() {
        Equipment equipment = new Equipment(1, "Test", "Test", 1);
        assertNotEquals(equipment, null);
    }
    
    @Test
    public void testEqualsDifferentTypes() {
        Equipment equipment = new Equipment(1, "Test", "Test", 1);
        assertNotEquals(equipment, "String");
    }
    
    @Test
    public void testEqualsSameId() {
        Equipment e1 = new Equipment(100, "Treadmill", "Cardio", 5);
        Equipment e2 = new Equipment(100, "Bike", "Strength", 3);
        
        assertEquals(e1, e2);
        assertEquals(e1.hashCode(), e2.hashCode());
    }
    
    @Test
    public void testEqualsDifferentIds() {
        Equipment e1 = new Equipment(1, "Test", "Test", 1);
        Equipment e2 = new Equipment(2, "Test", "Test", 1);
        assertNotEquals(e1, e2);
    }
    
    @Test
    public void testHashCode() {
        Equipment e1 = new Equipment(50, "A", "B", 1);
        Equipment e2 = new Equipment(50, "C", "D", 2);
        Equipment e3 = new Equipment(100, "A", "B", 1);
        
        assertEquals(e1.hashCode(), e2.hashCode());
        assertNotEquals(e1.hashCode(), e3.hashCode());
    }
    
    @Test
    public void testToString() {
        Equipment equipment = new Equipment(1, "Treadmill", "Cardio", 5, 10, 15);
        String str = equipment.toString();
        
        assertTrue(str.contains("Treadmill"));
        assertTrue(str.contains("Cardio"));
    }
    
    @Test
    public void testIdChanges() {
        Equipment equipment = new Equipment();
        
        equipment.setId(100);
        assertEquals(100, equipment.getId());
        
        equipment.setId(0);
        assertEquals(0, equipment.getId());
    }
}