package com.ibrahim.mehdi.gymmanager.datastructures;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Double Linked List implementation for member history navigation.
 * Allows bidirectional traversal.
 * 
 * @param <T> Type of elements stored in the list
 * @author ibrahim.mehdi
 */
public class DoubleLinkedList<T> implements Serializable, Iterable<T> {
    private static final long serialVersionUID = 1L;
    
    /**
     * Node class for double linked list
     */
    private static class Node<T> implements Serializable {
        private static final long serialVersionUID = 1L;
        T data;
        Node<T> prev;
        Node<T> next;
        
        Node(T data) {
            this.data = data;
            this.prev = null;
            this.next = null;
        }
    }
    
    private Node<T> head;
    private Node<T> tail;
    private Node<T> current; // For navigation
    private int size;
    
    /**
     * Default constructor
     */
    public DoubleLinkedList() {
        this.head = null;
        this.tail = null;
        this.current = null;
        this.size = 0;
    }
    
    /**
     * Add element to the end of the list
     * 
     * @param data Element to add
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        
        if (head == null) {
            head = tail = current = newNode;
        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
        }
        size++;
    }
    
    /**
     * Add element at the beginning
     * 
     * @param data Element to add
     */
    public void addFirst(T data) {
        Node<T> newNode = new Node<>(data);
        
        if (head == null) {
            head = tail = current = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }
    
    /**
     * Remove element from the end
     * 
     * @return Removed element
     */
    public T removeLast() {
        if (tail == null) {
            throw new NoSuchElementException("List is empty");
        }
        
        T data = tail.data;
        
        if (head == tail) {
            head = tail = current = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return data;
    }
    
    /**
     * Navigate forward (like browser forward button)
     * 
     * @return Next element or null
     */
    public T navigateForward() {
        if (current != null && current.next != null) {
            current = current.next;
            return current.data;
        }
        return null;
    }
    
    /**
     * Navigate backward (like browser back button)
     * 
     * @return Previous element or null
     */
    public T navigateBackward() {
        if (current != null && current.prev != null) {
            current = current.prev;
            return current.data;
        }
        return null;
    }
    
    /**
     * Get current element
     * 
     * @return Current element
     */
    public T getCurrent() {
        return current != null ? current.data : null;
    }
    
    /**
     * Reset navigation to head
     */
    public void resetNavigation() {
        current = head;
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
        head = tail = current = null;
        size = 0;
    }
    
    /**
     * Get element at index
     * 
     * @param index Index of element
     * @return Element at index
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index);
        }
        
        Node<T> node = head;
        for (int i = 0; i < index; i++) {
            node = node.next;
        }
        return node.data;
    }
    
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node<T> currentNode = head;
            
            @Override
            public boolean hasNext() {
                return currentNode != null;
            }
            
            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                T data = currentNode.data;
                currentNode = currentNode.next;
                return data;
            }
        };
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<T> node = head;
        while (node != null) {
            sb.append(node.data);
            if (node.next != null) {
                sb.append(" <-> ");
            }
            node = node.next;
        }
        sb.append("]");
        return sb.toString();
    }
}
