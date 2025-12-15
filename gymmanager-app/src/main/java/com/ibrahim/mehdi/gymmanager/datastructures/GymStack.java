package com.ibrahim.mehdi.gymmanager.datastructures;

import java.io.Serializable;
import java.util.EmptyStackException;

/**
 * Stack implementation (LIFO) for undo operations.
 * 
 * @param <T> Type of elements
 * @author ibrahim.mehdi
 */
public class GymStack<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * Node class for stack
     */
    private static class Node<T> implements Serializable {
        private static final long serialVersionUID = 1L;
        T data;
        Node<T> next;
        
        Node(T data) {
            this.data = data;
        }
    }
    
    private Node<T> top;
    private int size;
    private int maxSize;
    
    /**
     * Constructor with default max size
     */
    public GymStack() {
        this(100);
    }
    
    /**
     * Constructor with max size
     * 
     * @param maxSize Maximum stack size
     */
    public GymStack(int maxSize) {
        this.top = null;
        this.size = 0;
        this.maxSize = maxSize;
    }
    
    /**
     * Push element onto stack
     * 
     * @param data Element to push
     */
    public void push(T data) {
        if (size >= maxSize) {
            // Remove bottom element if max size reached
            removeBottom();
        }
        
        Node<T> newNode = new Node<>(data);
        newNode.next = top;
        top = newNode;
        size++;
    }
    
    /**
     * Pop element from stack
     * 
     * @return Popped element
     */
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        
        T data = top.data;
        top = top.next;
        size--;
        return data;
    }
    
    /**
     * Peek at top element without removing
     * 
     * @return Top element
     */
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return top.data;
    }
    
    /**
     * Remove bottom element (for max size constraint)
     */
    private void removeBottom() {
        if (top == null) return;
        
        if (top.next == null) {
            top = null;
            size = 0;
            return;
        }
        
        Node<T> current = top;
        while (current.next.next != null) {
            current = current.next;
        }
        current.next = null;
        size--;
    }
    
    /**
     * Check if stack is empty
     * 
     * @return True if empty
     */
    public boolean isEmpty() {
        return top == null;
    }
    
    /**
     * Get stack size
     * 
     * @return Size
     */
    public int size() {
        return size;
    }
    
    /**
     * Clear the stack
     */
    public void clear() {
        top = null;
        size = 0;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Stack[");
        Node<T> current = top;
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
