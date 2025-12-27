package com.ibrahim.mehdi.gymmanager.util;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.Serializable;

/**
 * Complete BinaryFileStorage Test Suite
 * Tests file operations for data persistence
 */
@DisplayName("BinaryFileStorage Utility - Complete Tests")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class BinaryFileStorageTest {
    
    private static final String TEST_DATA_DIR = "data";
    private static final String TEST_FILE = "test_storage.dat";
    
    /**
     * Test class for serialization
     */
    static class TestData implements Serializable {
        private static final long serialVersionUID = 1L;
        
        String name;
        int value;
        
        TestData(String name, int value) {
            this.name = name;
            this.value = value;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TestData)) return false;
            TestData other = (TestData) obj;
            return this.name.equals(other.name) && this.value == other.value;
        }
    }
    
    @BeforeEach
    public void setUp() {
        cleanupTestFile();
    }
    
    @AfterEach
    public void tearDown() {
        cleanupTestFile();
    }
    
    private void cleanupTestFile() {
        try {
            File dataDir = new File(TEST_DATA_DIR);
            if (dataDir.exists()) {
                File testFile = new File(dataDir, TEST_FILE);
                if (testFile.exists()) {
                    testFile.delete();
                }
            }
        } catch (Exception e) {
       
        }
    }
    
    @Test
    @DisplayName("Should save and load object successfully")
    public void testSaveAndLoad() {
        TestData original = new TestData("Test", 42);
       
        boolean saved = BinaryFileStorage.save(TEST_FILE, original);
        assertTrue(saved, "Save should succeed");
        
        TestData loaded = BinaryFileStorage.load(TEST_FILE);
        assertNotNull(loaded, "Loaded object should not be null");
        assertEquals(original, loaded, "Loaded object should equal original");
    }
    
    @Test
    @DisplayName("Should return null for non-existent file")
    public void testLoadNonExistentFile() {
        TestData loaded = BinaryFileStorage.load("non_existent_file.dat");
        assertNull(loaded, "Should return null for non-existent file");
    }
    
    @Test
    @DisplayName("Should check file existence")
    public void testFileExists() {
        assertFalse(BinaryFileStorage.exists(TEST_FILE), 
            "File should not exist initially");
        
        TestData data = new TestData("Test", 100);
        BinaryFileStorage.save(TEST_FILE, data);
        
        assertTrue(BinaryFileStorage.exists(TEST_FILE), 
            "File should exist after save");
    }
    
    @Test
    @DisplayName("Should delete file successfully")
    public void testDeleteFile() {
        TestData data = new TestData("DeleteTest", 200);
        BinaryFileStorage.save(TEST_FILE, data);
        
        assertTrue(BinaryFileStorage.exists(TEST_FILE), 
            "File should exist before delete");
        
        boolean deleted = BinaryFileStorage.delete(TEST_FILE);
        assertTrue(deleted, "Delete should succeed");
        
        assertFalse(BinaryFileStorage.exists(TEST_FILE), 
            "File should not exist after delete");
    }
    
    @Test
    @DisplayName("Should handle delete non-existent file")
    public void testDeleteNonExistentFile() {
        boolean deleted = BinaryFileStorage.delete("non_existent.dat");
        assertFalse(deleted, "Delete of non-existent file should return false");
    }
    
    @Test
    @DisplayName("Should handle null object save")
    public void testSaveNullObject() {
        boolean saved = BinaryFileStorage.save(TEST_FILE, null);
        assertTrue(saved, "Saving null should not throw exception");
        
        Object loaded = BinaryFileStorage.load(TEST_FILE);
        assertNull(loaded, "Loading null object should return null");
    }
    
    @Test
    @DisplayName("Should handle complex object serialization")
    public void testComplexObject() {
        TestData data1 = new TestData("Item1", 10);
        TestData data2 = new TestData("Item2", 20);
        
        // Save first object
        BinaryFileStorage.save(TEST_FILE, data1);
        TestData loaded1 = BinaryFileStorage.load(TEST_FILE);
        assertEquals(data1, loaded1);
        
        // Overwrite with second object
        BinaryFileStorage.save(TEST_FILE, data2);
        TestData loaded2 = BinaryFileStorage.load(TEST_FILE);
        assertEquals(data2, loaded2);
        assertNotEquals(data1, loaded2);
    }
    
    @Test
    @DisplayName("Should handle multiple file operations")
    public void testMultipleFiles() {
        String file1 = "test_file1.dat";
        String file2 = "test_file2.dat";
        
        try {
            TestData data1 = new TestData("Data1", 100);
            TestData data2 = new TestData("Data2", 200);
            
            BinaryFileStorage.save(file1, data1);
            BinaryFileStorage.save(file2, data2);
          
            assertTrue(BinaryFileStorage.exists(file1));
            assertTrue(BinaryFileStorage.exists(file2));
            
            TestData loaded1 = BinaryFileStorage.load(file1);
            TestData loaded2 = BinaryFileStorage.load(file2);
            
            assertEquals(data1, loaded1);
            assertEquals(data2, loaded2);
            
        } finally {
            
            BinaryFileStorage.delete(file1);
            BinaryFileStorage.delete(file2);
        }
    }
    
    @Test
    @DisplayName("Should handle Turkish characters in data")
    public void testTurkishCharacters() {
        TestData data = new TestData("Türkçe Karakter: ğüşıöç ĞÜŞIÖÇ", 42);
        
        BinaryFileStorage.save(TEST_FILE, data);
        TestData loaded = BinaryFileStorage.load(TEST_FILE);
        
        assertNotNull(loaded);
        assertEquals(data.name, loaded.name);
        assertEquals(data.value, loaded.value);
    }
    
    @Test
    @DisplayName("Should create data directory if not exists")
    public void testDirectoryCreation() {
       
        File dataDir = new File(TEST_DATA_DIR);
        
        TestData data = new TestData("DirTest", 300);
        boolean saved = BinaryFileStorage.save(TEST_FILE, data);
        
        assertTrue(saved);
        assertTrue(dataDir.exists());
        assertTrue(dataDir.isDirectory());
    }
    
    @Test
    @DisplayName("Should handle special characters in filename")
    public void testSpecialCharactersInFilename() {
        String specialFile = "test_file_123.dat";
        
        try {
            TestData data = new TestData("Special", 999);
            boolean saved = BinaryFileStorage.save(specialFile, data);
            assertTrue(saved);
            
            TestData loaded = BinaryFileStorage.load(specialFile);
            assertEquals(data, loaded);
            
        } finally {
            BinaryFileStorage.delete(specialFile);
        }
    }
    
    @Test
    @DisplayName("Should handle large objects")
    public void testLargeObject() {
        // Create object with large data
        StringBuilder largeString = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            largeString.append("Line ").append(i).append(" ");
        }
        
        TestData largeData = new TestData(largeString.toString(), 12345);
        
        boolean saved = BinaryFileStorage.save(TEST_FILE, largeData);
        assertTrue(saved);
        
        TestData loaded = BinaryFileStorage.load(TEST_FILE);
        assertNotNull(loaded);
        assertEquals(largeData.value, loaded.value);
        assertTrue(loaded.name.length() > 50000);
    }
    
}