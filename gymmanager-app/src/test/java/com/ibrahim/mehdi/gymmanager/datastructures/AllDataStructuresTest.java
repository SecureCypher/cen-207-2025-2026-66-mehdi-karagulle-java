package com.ibrahim.mehdi.gymmanager.datastructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Queue implementation - Enhanced version
 */
public class GymQueue<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static class Node<T> {
        T data;
        Node<T> next;
        
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
    
    private Node<T> front;
    private Node<T> rear;
    private int size;
    
    public GymQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }
    
    /**
     * Add element to queue
     */
    public void enqueue(T data) {
        Node<T> newNode = new Node<T>(data);
        
        if (isEmpty()) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        
        size++;
    }
    
    /**
     * Remove and return front element
     */
    public T dequeue() {
        if (isEmpty()) {
            return null;
        }
        
        T data = front.data;
        front = front.next;
        
        if (front == null) {
            rear = null;
        }
        
        size--;
        return data;
    }
    
    /**
     * Peek at front element without removing
     */
    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return front.data;
    }
    
    /**
     * Check if queue is empty
     */
    public boolean isEmpty() {
        return front == null;
    }
    
    /**
     * Get queue size
     */
    public int size() {
        return size;
    }
    
    /**
     * Convert queue to list (for display)
     */
    public List<T> toList() {
        List<T> list = new ArrayList<T>();
        Node<T> current = front;
        
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        
        return list;
    }
    
    /**
     * Clear queue
     */
    public void clear() {
        front = null;
        rear = null;
        size = 0;
    }
    
    @Override
    public String toString() {
        if (isEmpty()) {
            return "Queue is empty";
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Queue (size=").append(size).append("): ");
        
        Node<T> current = front;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(" <- ");
            }
            current = current.next;
        }
        
        return sb.toString();
    }
}