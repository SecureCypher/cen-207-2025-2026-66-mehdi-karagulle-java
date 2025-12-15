package com.ibrahim.mehdi.gymmanager.datastructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * XOR Linked List implementation for memory-efficient workout history.
 * Uses XOR operation to store prev and next in single pointer.
 * 
 * Note: Java doesn't support true pointer arithmetic, so this is a simulated version
 * using HashMap-like structure to demonstrate the concept.
 * 
 * @param <T> Type of elements
 * @author ibrahim.mehdi
 */
public class XORLinkedList<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * Node class with XOR link
     */
    private static class XORNode<T> implements Serializable {
        private static final long serialVersionUID = 1L;
        T data;
        int xorLink; // XOR of prev and next indices
        
        XORNode(T data) {
            this.data = data;
            this.xorLink = 0;
        }
    }
    
    private List<XORNode<T>> nodes;
    private int headIndex;
    private int tailIndex;
    private int size;
    
    /**
     * Default constructor
     */
    public XORLinkedList() {
        this.nodes = new ArrayList<>();
        this.headIndex = -1;
        this.tailIndex = -1;
        this.size = 0;
    }
    
    /**
     * Add element to the end
     * 
     * @param data Element to add
     */
    public void add(T data) {
        XORNode<T> newNode = new XORNode<>(data);
        int newIndex = nodes.size();
        
        if (headIndex == -1) {
            // First node
            headIndex = tailIndex = newIndex;
            newNode.xorLink = -1;
        } else {
            // Update tail's XOR link
            XORNode<T> tail = nodes.get(tailIndex);
            int tailPrev = tail.xorLink; // Previous node index
            tail.xorLink = tailPrev ^ newIndex; // XOR with new node
            
            // New node's XOR link
            newNode.xorLink = tailIndex ^ (-1); // XOR with tail and null
            
            tailIndex = newIndex;
        }
        
        nodes.add(newNode);
        size++;
    }
    
    /**
     * Traverse forward and collect all elements
     * 
     * @return List of elements
     */
    public List<T> traverseForward() {
        List<T> result = new ArrayList<>();
        
        if (headIndex == -1) {
            return result;
        }
        
        int current = headIndex;
        int prev = -1;
        
        while (current != -1) {
            XORNode<T> node = nodes.get(current);
            result.add(node.data);
            
            // Calculate next using XOR
            int next = prev ^ node.xorLink;
            prev = current;
            current = next;
        }
        
        return result;
    }
    
    /**
     * Traverse backward and collect all elements
     * 
     * @return List of elements in reverse order
     */
    public List<T> traverseBackward() {
        List<T> result = new ArrayList<>();
        
        if (tailIndex == -1) {
            return result;
        }
        
        int current = tailIndex;
        int next = -1;
        
        while (current != -1) {
            XORNode<T> node = nodes.get(current);
            result.add(node.data);
            
            // Calculate prev using XOR
            int prev = next ^ node.xorLink;
            next = current;
            current = prev;
        }
        
        return result;
    }
    
    /**
     * Get element at index (0-based)
     * 
     * @param index Index of element
     * @return Element at index
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        
        int current = headIndex;
        int prev = -1;
        int count = 0;
        
        while (current != -1 && count < index) {
            XORNode<T> node = nodes.get(current);
            int next = prev ^ node.xorLink;
            prev = current;
            current = next;
            count++;
        }
        
        return current != -1 ? nodes.get(current).data : null;
    }
    
    /**
     * Get size of the list
     * 
     * @return Size
     */
    public int size() {
        return size;
    }
    
    /**
     * Check if list is empty
     * 
     * @return True if empty
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Clear the list
     */
    public void clear() {
        nodes.clear();
        headIndex = -1;
        tailIndex = -1;
        size = 0;
    }
    
    @Override
    public String toString() {
        List<T> elements = traverseForward();
        return elements.toString();
    }
}
