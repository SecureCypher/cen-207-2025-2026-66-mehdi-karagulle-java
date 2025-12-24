package com.ibrahim.mehdi.gymmanager.model;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Equipment Edge Cases and Branch Coverage Tests
 * Increases coverage by testing all branches
 */
@DisplayName("Equipment - Edge Cases and Branch Coverage")
public class EquipmentEdgeCasesTest {
    
    @Test
    @DisplayName("Should handle equipment with zero quantity")
    public void testZeroQuantity() {
        Equipment equipment = new Equipment(1, "Broken Treadmill", "Cardio", 0);
        
        assertEquals(0, equipment.getQuantity());
        assertNotNull(equipment);
        assertEquals("Broken Treadmill", equipment.getName());
    }
    
    @Test
    @DisplayName("Should handle negative location values")
    public void testNegativeLocations() {
        Equipment equipment = new Equipment();
        equipment.setLocationX(-5);
        equipment.setLocationY(-10);
        
        assertEquals(-5, equipment.getLocationX());
        assertEquals(-10, equipment.getLocationY());
    }
    
    @Test
    @DisplayName("Should handle very large quantities")
    public void testLargeQuantities() {
        Equipment equipment = new Equipment(1, "Dumbbells", "Weights", Integer.MAX_VALUE);
        
        assertEquals(Integer.MAX_VALUE, equipment.getQuantity());
    }
    
    @Test
    @DisplayName("Should handle empty strings in equipment data")
    public void testEmptyStrings() {
        Equipment equipment = new Equipment(1, "", "", 1);
        
        assertEquals("", equipment.getName());
        assertEquals("", equipment.getType());
        assertNotNull(equipment.toString());
    }
    
    @Test
    @DisplayName("Should handle null strings gracefully")
    public void testNullStrings() {
        Equipment equipment = new Equipment();
        equipment.setName(null);
        equipment.setType(null);
        
        assertNull(equipment.getName());
        assertNull(equipment.getType());
    }
    
    @Test
    @DisplayName("Should handle availability toggle")
    public void testAvailabilityToggle() {
        Equipment equipment = new Equipment(1, "Test", "Test", 1);
        
        assertTrue(equipment.isAvailable());
        
        equipment.setAvailable(false);
        assertFalse(equipment.isAvailable());
        
        equipment.setAvailable(true);
        assertTrue(equipment.isAvailable());
    }
    
    @Test
    @DisplayName("Should handle equipment at boundary coordinates")
    public void testBoundaryCoordinates() {
        Equipment e1 = new Equipment(1, "E1", "T1", 1, 0, 0);
        assertEquals(0, e1.getLocationX());
        assertEquals(0, e1.getLocationY());
        
        Equipment e2 = new Equipment(2, "E2", "T2", 1, Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, e2.getLocationX());
        assertEquals(Integer.MAX_VALUE, e2.getLocationY());
    }
    
    @Test
    @DisplayName("Should verify equals with different object types")
    public void testEqualsWithDifferentTypes() {
        Equipment equipment = new Equipment(1, "Test", "Test", 1);
        
        assertNotEquals(equipment, null);
        assertNotEquals(equipment, "String");
        assertNotEquals(equipment, Integer.valueOf(1));
        assertEquals(equipment, equipment);
    }
    
    @Test
    @DisplayName("Should verify hashCode consistency")
    public void testHashCodeConsistency() {
        Equipment e1 = new Equipment(1, "A", "B", 1);
        Equipment e2 = new Equipment(1, "C", "D", 2);
        
        assertEquals(e1.hashCode(), e2.hashCode());
        
        Equipment e3 = new Equipment(2, "A", "B", 1);
        assertNotEquals(e1.hashCode(), e3.hashCode());
    }
    
    @Test
    @DisplayName("Should handle toString with various states")
    public void testToStringVariousStates() {
        Equipment e1 = new Equipment();
        assertNotNull(e1.toString());
        
        Equipment e2 = new Equipment(1, "Test", "Type", 5, 10, 15);
        String str = e2.toString();
        assertTrue(str.contains("Test"));
        assertTrue(str.contains("Type"));
        assertTrue(str.contains("5"));
        assertTrue(str.contains("10"));
        assertTrue(str.contains("15"));
    }
    
    @Test
    @DisplayName("Should handle special characters in equipment name")
    public void testSpecialCharactersInName() {
        Equipment equipment = new Equipment(1, "Test@#$%^&*()", "Type!@#", 1);
        
        assertEquals("Test@#$%^&*()", equipment.getName());
        assertEquals("Type!@#", equipment.getType());
    }
    
    @Test
    @DisplayName("Should handle very long equipment names")
    public void testLongEquipmentName() {
        StringBuilder longName = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            longName.append("A");
        }
        
        Equipment equipment = new Equipment(1, longName.toString(), "Type", 1);
        assertEquals(1000, equipment.getName().length());
    }
    
    @Test
    @DisplayName("Should handle ID changes")
    public void testIdChanges() {
        Equipment equipment = new Equipment();
        equipment.setId(100);
        assertEquals(100, equipment.getId());
        
        equipment.setId(0);
        assertEquals(0, equipment.getId());
        
        equipment.setId(-1);
        assertEquals(-1, equipment.getId());
    }
    
    @Test
    @DisplayName("Should handle quantity updates")
    public void testQuantityUpdates() {
        Equipment equipment = new Equipment(1, "Test", "Test", 10);
        
        equipment.setQuantity(5);
        assertEquals(5, equipment.getQuantity());
        
        equipment.setQuantity(0);
        assertEquals(0, equipment.getQuantity());
        
        equipment.setQuantity(100);
        assertEquals(100, equipment.getQuantity());
    }
}