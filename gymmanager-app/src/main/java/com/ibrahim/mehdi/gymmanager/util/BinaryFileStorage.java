package com.ibrahim.mehdi.gymmanager.util;

import java.io.*;

/**
 * Binary file storage utility for saving/loading data.
 * 
 * @author ibrahim.mehdi
 */
public class BinaryFileStorage {
    
    private static final String DATA_DIR = "data";
    
    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
    
    /**
     * Save object to binary file
     */
    public static <T> boolean save(String filename, T object) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(DATA_DIR + File.separator + filename))) {
            oos.writeObject(object);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving to file: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load object from binary file
     */
    @SuppressWarnings("unchecked")
    public static <T> T load(String filename) {
        File file = new File(DATA_DIR + File.separator + filename);
        if (!file.exists()) {
            return null;
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(file))) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading from file: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Check if file exists
     */
    public static boolean exists(String filename) {
        return new File(DATA_DIR + File.separator + filename).exists();
    }
    
    /**
     * Delete file
     */
    public static boolean delete(String filename) {
        return new File(DATA_DIR + File.separator + filename).delete();
    }
}
