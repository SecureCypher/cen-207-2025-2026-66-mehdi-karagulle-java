package com.ibrahim.mehdi.gymmanager.datastructures;

import java.io.Serializable;
import java.util.NoSuchElementException;

/**
 * Queue implementation (FIFO) for waiting member queue.
 * 
 * @param <T> Type of elements
 * @author ibrahim.mehdi
 */
public class GymQueue<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * Node class for queue
     */
    private static class Node<T> implements Serializable {
        private static final long serialVersionUID = 1L;
        T data;
        Node<T> next;
        
        Node(T data) {
            this.data = data;
        }
    }
    
    private Node<T> front;
    private Node<T> rear;
    private int size;
    
    /**
     * Default constructor
     */
    public GymQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }
    
    /**
     * Enqueue (add) element to the queue
     * 
     * @param data Element to add
     */
    public void enqueue(T data) {
        Node<T> newNode = new Node<>(data);
        
        if (rear == null) {
            front = rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }
    
    /**
     * Dequeue (remove) element from the queue
     * 
     * @return Removed element
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
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
     * 
     * @return Front element
     */
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("Queue is empty");
        }
        return front.data;
    }
    
    /**
     * Check if queue is empty
     * 
     * @return True if empty
     */
    public boolean isEmpty() {
        return front == null;
    }
    
    /**
     * Get queue size
     * 
     * @return Size
     */
    public int size() {
        return size;
    }
    
    /**
     * Clear the queue
     */
    public void clear() {
        front = rear = null;
        size = 0;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Queue[");
        Node<T> current = front;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(" <- ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
