package com.ibrahim.mehdi.gymmanager.model;

import java.io.Serializable;

/**
 * Equipment class representing gym equipment.
 * Used in Sparse Matrix and Graph algorithms.
 * 
 * @author ibrahim.mehdi
 */
public class Equipment implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private String type;
    private int quantity;
    private boolean isAvailable;
    private int locationX; // For sparse matrix
    private int locationY; // For sparse matrix
    
    /**
     * Default constructor
     */
    public Equipment() {
        this.isAvailable = true;
    }
    
    /**
     * Parameterized constructor
     */
    public Equipment(int id, String name, String type, int quantity) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.quantity = quantity;
        this.isAvailable = true;
        this.locationX = 0;
        this.locationY = 0;
    }
    
    /**
     * Constructor with location
     */
    public Equipment(int id, String name, String type, int quantity, 
                    int locationX, int locationY) {
        this(id, name, type, quantity);
        this.locationX = locationX;
        this.locationY = locationY;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }
    
    public int getLocationX() { return locationX; }
    public void setLocationX(int locationX) { this.locationX = locationX; }
    
    public int getLocationY() { return locationY; }
    public void setLocationY(int locationY) { this.locationY = locationY; }
    
    @Override
    public String toString() {
        return String.format("Equipment[ID=%d, Name=%s, Type=%s, Qty=%d, Loc=(%d,%d)]",
                id, name, type, quantity, locationX, locationY);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Equipment equipment = (Equipment) obj;
        return id == equipment.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
