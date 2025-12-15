package com.ibrahim.mehdi.gymmanager.datastructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Hash Table implementation for fast member lookup.
 * Uses chaining for collision resolution.
 * 
 * @param <K> Key type
 * @param <V> Value type
 * @author ibrahim.mehdi
 */
public class HashTable<K, V> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;
    
    /**
     * Entry class for key-value pairs
     */
    private static class Entry<K, V> implements Serializable {
        private static final long serialVersionUID = 1L;
        K key;
        V value;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
    
    private List<LinkedList<Entry<K, V>>> table;
    private int size;
    private int capacity;
    
    /**
     * Default constructor
     */
    public HashTable() {
        this(DEFAULT_CAPACITY);
    }
    
    /**
     * Constructor with capacity
     * 
     * @param capacity Initial capacity
     */
    @SuppressWarnings("unchecked")
    public HashTable(int capacity) {
        this.capacity = capacity;
        this.table = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            table.add(new LinkedList<>());
        }
        this.size = 0;
    }
    
    /**
     * Hash function
     * 
     * @param key Key to hash
     * @return Hash index
     */
    private int hash(K key) {
        return Math.abs(key.hashCode()) % capacity;
    }
    
    /**
     * Put key-value pair
     * 
     * @param key Key
     * @param value Value
     */
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table.get(index);
        
        // Update if key exists
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                entry.value = value;
                return;
            }
        }
        
        // Add new entry
        bucket.add(new Entry<>(key, value));
        size++;
        
        // Resize if load factor exceeded
        if ((double) size / capacity > LOAD_FACTOR) {
            resize();
        }
    }
    
    /**
     * Get value by key
     * 
     * @param key Key
     * @return Value or null
     */
    public V get(K key) {
        if (key == null) {
            return null;
        }
        
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table.get(index);
        
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        
        return null;
    }
    
    /**
     * Remove key-value pair
     * 
     * @param key Key to remove
     * @return Removed value or null
     */
    public V remove(K key) {
        if (key == null) {
            return null;
        }
        
        int index = hash(key);
        LinkedList<Entry<K, V>> bucket = table.get(index);
        
        for (Entry<K, V> entry : bucket) {
            if (entry.key.equals(key)) {
                bucket.remove(entry);
                size--;
                return entry.value;
            }
        }
        
        return null;
    }
    
    /**
     * Check if key exists
     * 
     * @param key Key
     * @return True if exists
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    /**
     * Resize table when load factor exceeded
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        List<LinkedList<Entry<K, V>>> oldTable = table;
        
        table = new ArrayList<>(newCapacity);
        for (int i = 0; i < newCapacity; i++) {
            table.add(new LinkedList<>());
        }
        
        capacity = newCapacity;
        size = 0;
        
        // Rehash all entries
        for (LinkedList<Entry<K, V>> bucket : oldTable) {
            for (Entry<K, V> entry : bucket) {
                put(entry.key, entry.value);
            }
        }
    }
    
    /**
     * Get all keys
     * 
     * @return List of keys
     */
    public List<K> keys() {
        List<K> keys = new ArrayList<>();
        for (LinkedList<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                keys.add(entry.key);
            }
        }
        return keys;
    }
    
    /**
     * Get all values
     * 
     * @return List of values
     */
    public List<V> values() {
        List<V> values = new ArrayList<>();
        for (LinkedList<Entry<K, V>> bucket : table) {
            for (Entry<K, V> entry : bucket) {
                values.add(entry.value);
            }
        }
        return values;
    }
    
    /**
     * Get size
     * 
     * @return Number of entries
     */
    public int size() {
        return size;
    }
    
    /**
     * Check if empty
     * 
     * @return True if empty
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Clear all entries
     */
    public void clear() {
        for (LinkedList<Entry<K, V>> bucket : table) {
            bucket.clear();
        }
        size = 0;
    }
    
    /**
     * Get collision statistics
     * 
     * @return Average chain length
     */
    public double getAverageChainLength() {
        int nonEmptyBuckets = 0;
        int totalChainLength = 0;
        
        for (LinkedList<Entry<K, V>> bucket : table) {
            if (!bucket.isEmpty()) {
                nonEmptyBuckets++;
                totalChainLength += bucket.size();
            }
        }
        
        return nonEmptyBuckets > 0 ? (double) totalChainLength / nonEmptyBuckets : 0;
    }
    
    @Override
    public String toString() {
        return String.format("HashTable[size=%d, capacity=%d, avgChain=%.2f]",
                size, capacity, getAverageChainLength());
    }
}
