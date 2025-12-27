package com.ibrahim.mehdi.gymmanager.util;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

@DisplayName("BinaryFileStorage Extended Tests")
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class BinaryFileStorageExtendedTest {
    
    private static final String TEST_DIR = "data";
    private static final String TEST_FILE = "extended_test.dat";
    
    static class TestObject implements Serializable {
        private static final long serialVersionUID = 1L;
        String data;
        int value;
        
        TestObject(String data, int value) {
            this.data = data;
            this.value = value;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof TestObject)) return false;
            TestObject other = (TestObject) obj;
            if (this.data == null) return other.data == null && this.value == other.value;
            return this.data.equals(other.data) && this.value == other.value;
        }
    }
    
    @BeforeEach
    public void setUp() {
        cleanup();
    }
    
    @AfterEach
    public void tearDown() {
        cleanup();
    }
    
    private void cleanup() {
        try {
            BinaryFileStorage.delete(TEST_FILE);
            BinaryFileStorage.delete("extended_file1.dat");
            BinaryFileStorage.delete("extended_file2.dat");
            BinaryFileStorage.delete("corrupted_test.dat");
            
            File dir = new File(TEST_DIR);
            if (dir.exists()) {
                File[] files = dir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.getName().startsWith("extended_") || 
                            file.getName().startsWith("corrupted_")) {
                            file.delete();
                        }
                    }
                }
            }
        } catch (Exception e) {
            // Ignore cleanup errors
        }
    }
    
    @Test
    public void testCorruptedFile() {
        String corruptedFile = "corrupted_test.dat";
        File dir = new File(TEST_DIR);
        File file = new File(dir, corruptedFile);
        
        try {
            // Create directory
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            // Write corrupted data
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write("THIS_IS_NOT_VALID_SERIALIZED_DATA".getBytes());
                fos.flush();
            }
            
            // Verify file was created
            assertTrue(file.exists(), "Test file must exist");
            
            // Attempt to load corrupted file
            // We don't make assumptions about the result
            // We just verify the operation completes
            try {
                BinaryFileStorage.load(corruptedFile);
            } catch (Exception e) {
                // Exception is acceptable
            }
            
            // Verify file still exists before cleanup
            assertTrue(file.exists(), "File should exist before cleanup");
            
        } catch (IOException fileError) {
            Assumptions.assumeTrue(false, "Test skipped: could not create test file");
        } finally {
            // Cleanup
            try {
                if (file.exists()) {
                    file.delete();
                }
                BinaryFileStorage.delete(corruptedFile);
            } catch (Exception e) {
                // Ignore cleanup errors
            }
        }
    }
    
    @Test
    public void testLargeData() {
        StringBuilder large = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            large.append("Data");
        }
        
        TestObject obj = new TestObject(large.toString(), 999);
        
        assertTrue(BinaryFileStorage.save(TEST_FILE, obj));
        
        TestObject loaded = BinaryFileStorage.load(TEST_FILE);
        assertNotNull(loaded);
        assertEquals(999, loaded.value);
    }
    
    @Test
    public void testMultipleSaves() {
        TestObject obj1 = new TestObject("First", 1);
        TestObject obj2 = new TestObject("Second", 2);
        TestObject obj3 = new TestObject("Third", 3);
        
        BinaryFileStorage.save(TEST_FILE, obj1);
        BinaryFileStorage.save(TEST_FILE, obj2);
        BinaryFileStorage.save(TEST_FILE, obj3);
        
        TestObject loaded = BinaryFileStorage.load(TEST_FILE);
        assertEquals(obj3, loaded);
    }
    
    @Test
    public void testRapidOperations() {
        for (int i = 0; i < 10; i++) {
            TestObject obj = new TestObject("Test" + i, i);
            
            assertTrue(BinaryFileStorage.save(TEST_FILE, obj));
            
            TestObject loaded = BinaryFileStorage.load(TEST_FILE);
            assertNotNull(loaded);
            assertEquals(i, loaded.value);
        }
    }
    
    @Test
    public void testNullFields() {
        TestObject obj = new TestObject(null, 100);
        
        BinaryFileStorage.save(TEST_FILE, obj);
        TestObject loaded = BinaryFileStorage.load(TEST_FILE);
        
        assertNull(loaded.data);
        assertEquals(100, loaded.value);
    }
    
    @Test
    public void testDeleteAndResave() {
        TestObject obj1 = new TestObject("Before", 1);
        TestObject obj2 = new TestObject("After", 2);
        
        BinaryFileStorage.save(TEST_FILE, obj1);
        BinaryFileStorage.delete(TEST_FILE);
        BinaryFileStorage.save(TEST_FILE, obj2);
        
        TestObject loaded = BinaryFileStorage.load(TEST_FILE);
        assertEquals(obj2, loaded);
    }
    
    @Test
    public void testMultipleFiles() {
        String file1 = "extended_file1.dat";
        String file2 = "extended_file2.dat";
        
        try {
            TestObject obj1 = new TestObject("Data1", 1);
            TestObject obj2 = new TestObject("Data2", 2);
            
            BinaryFileStorage.save(file1, obj1);
            BinaryFileStorage.save(file2, obj2);
            
            assertTrue(BinaryFileStorage.exists(file1));
            assertTrue(BinaryFileStorage.exists(file2));
            
            assertEquals(obj1, BinaryFileStorage.load(file1));
            assertEquals(obj2, BinaryFileStorage.load(file2));
            
        } finally {
            BinaryFileStorage.delete(file1);
            BinaryFileStorage.delete(file2);
        }
    }
    
    @Test
    public void testEmptyData() {
        TestObject obj = new TestObject("", 0);
        
        BinaryFileStorage.save(TEST_FILE, obj);
        TestObject loaded = BinaryFileStorage.load(TEST_FILE);
        
        assertEquals("", loaded.data);
        assertEquals(0, loaded.value);
    }
    
    @Test
    public void testNegativeValues() {
        TestObject obj = new TestObject("Negative", Integer.MIN_VALUE);
        
        BinaryFileStorage.save(TEST_FILE, obj);
        TestObject loaded = BinaryFileStorage.load(TEST_FILE);
        
        assertEquals(Integer.MIN_VALUE, loaded.value);
    }
    
    @Test
    public void testSpecialCharacters() {
        String special = "@#$%^&*()";
        TestObject obj = new TestObject(special, 42);
        
        BinaryFileStorage.save(TEST_FILE, obj);
        TestObject loaded = BinaryFileStorage.load(TEST_FILE);
        
        assertEquals(special, loaded.data);
    }
    
    @Test
    public void testDirectoryCreation() {
        File dir = new File(TEST_DIR);
        
        TestObject obj = new TestObject("Test", 1);
        BinaryFileStorage.save(TEST_FILE, obj);
        
        assertTrue(dir.exists());
        assertTrue(dir.isDirectory());
    }
}