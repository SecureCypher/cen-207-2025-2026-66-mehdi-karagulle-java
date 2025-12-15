package com.ibrahim.mehdi.gymmanager.datastructures;

import java.io.Serializable;

/**
 * File Operations using Linear Probing hash technique.
 * Fast member ID lookup with collision handling.
 * 
 * @param <K> Key type
 * @param <V> Value type
 * @author ibrahim.mehdi
 */
public class LinearProbingHash<K, V> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_CAPACITY = 101; // Prime number
    
    private static class Entry<K, V> implements Serializable {
        private static final long serialVersionUID = 1L;
        K key;
        V value;
        boolean isDeleted;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.isDeleted = false;
        }
    }
    
    @SuppressWarnings("unchecked")
    private Entry<K, V>[] table;
    private int size;
    private int capacity;
    private int collisionCount;
    
    @SuppressWarnings("unchecked")
    public LinearProbingHash() {
        this.capacity = DEFAULT_CAPACITY;
        this.table = (Entry<K, V>[]) new Entry[capacity];
        this.size = 0;
        this.collisionCount = 0;
    }
    
    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }
    
    /**
     * Insert key-value pair
     */
    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        
        int index = hash(key);
        int originalIndex = index;
        int probeCount = 0;
        
        while (table[index] != null && !table[index].isDeleted) {
            if (table[index].key.equals(key)) {
                table[index].value = value;
                return;
            }
            
            index = (index + 1) % capacity;
            probeCount++;
            
            if (index == originalIndex) {
                resize();
                put(key, value);
                return;
            }
        }
        
        if (probeCount > 0) {
            collisionCount++;
        }
        
        table[index] = new Entry<>(key, value);
        size++;
        
        if ((double) size / capacity > 0.7) {
            resize();
        }
    }
    
    /**
     * Get value by key
     */
    public V get(K key) {
        if (key == null) return null;
        
        int index = hash(key);
        int originalIndex = index;
        
        while (table[index] != null) {
            if (!table[index].isDeleted && table[index].key.equals(key)) {
                return table[index].value;
            }
            
            index = (index + 1) % capacity;
            
            if (index == originalIndex) {
                break;
            }
        }
        
        return null;
    }
    
    /**
     * Remove key
     */
    public V remove(K key) {
        if (key == null) return null;
        
        int index = hash(key);
        int originalIndex = index;
        
        while (table[index] != null) {
            if (!table[index].isDeleted && table[index].key.equals(key)) {
                V value = table[index].value;
                table[index].isDeleted = true;
                size--;
                return value;
            }
            
            index = (index + 1) % capacity;
            
            if (index == originalIndex) {
                break;
            }
        }
        
        return null;
    }
    
    /**
     * Check if key exists
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        Entry<K, V>[] oldTable = table;
        
        table = (Entry<K, V>[]) new Entry[newCapacity];
        capacity = newCapacity;
        size = 0;
        collisionCount = 0;
        
        for (Entry<K, V> entry : oldTable) {
            if (entry != null && !entry.isDeleted) {
                put(entry.key, entry.value);
            }
        }
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
    
    public int getCollisionCount() {
        return collisionCount;
    }
    
    public double getLoadFactor() {
        return (double) size / capacity;
    }
    
    @Override
    public String toString() {
        return String.format("LinearProbingHash[size=%d, capacity=%d, load=%.2f, collisions=%d]",
                size, capacity, getLoadFactor(), collisionCount);
    }
}
