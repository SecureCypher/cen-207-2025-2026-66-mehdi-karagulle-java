package com.ibrahim.mehdi.gymmanager;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GymManagerApp Tests")
public class GymmanagerAppTest {
    
    private GymManagerApp app;
    
    @BeforeEach
    public void setUp() {
        // Simplified - just create app without showing it
        app = new GymManagerApp();
    }
    
    @AfterEach
    public void tearDown() {
        if (app != null) {
            app.dispose();
        }
    }
    
    @Test
    @DisplayName("Should create application")
    public void testApplicationCreation() {
        assertNotNull(app);
    }
    
    @Test
    @DisplayName("Should have title")
    public void testTitle() {
        String title = app.getTitle();
        
        assertNotNull(title);
        assertTrue(title.contains("Gym Manager") || title.contains("Data Structures"));
    }
    
    @Test
    @DisplayName("Should have correct size")
    public void testSize() {
        int width = app.getWidth();
        int height = app.getHeight();
        
        assertTrue(width >= 1200);
        assertTrue(height >= 700);
    }
}