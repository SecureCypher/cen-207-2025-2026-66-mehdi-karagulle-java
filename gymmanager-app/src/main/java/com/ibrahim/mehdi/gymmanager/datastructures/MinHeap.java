package com.ibrahim.mehdi.gymmanager.datastructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Min Heap implementation for appointment priority queue.
 * Lower priority number = higher priority.
 * 
 * @param <T> Type of elements (must be Comparable)
 * @author ibrahim.mehdi
 */
public class MinHeap<T extends Comparable<T>> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<T> heap;
    
    /**
     * Default constructor
     */
    public MinHeap() {
        this.heap = new ArrayList<>();
    }
    
    /**
     * Constructor with initial elements
     * 
     * @param elements Initial elements
     */
    public MinHeap(List<T> elements) {
        this.heap = new ArrayList<>(elements);
        buildHeap();
    }
    
    /**
     * Insert element into heap
     * 
     * @param element Element to insert
     */
    public void insert(T element) {
        heap.add(element);
        heapifyUp(heap.size() - 1);
    }
    
    /**
     * Extract minimum element
     * 
     * @return Minimum element
     */
    public T extractMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        
        T min = heap.get(0);
        T last = heap.remove(heap.size() - 1);
        
        if (!heap.isEmpty()) {
            heap.set(0, last);
            heapifyDown(0);
        }
        
        return min;
    }
    
    /**
     * Peek at minimum element without removing
     * 
     * @return Minimum element
     */
    public T peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("Heap is empty");
        }
        return heap.get(0);
    }
    
    /**
     * Build heap from existing elements
     */
    private void buildHeap() {
        for (int i = parent(heap.size() - 1); i >= 0; i--) {
            heapifyDown(i);
        }
    }
    
    /**
     * Heapify up (restore heap property upward)
     * 
     * @param index Starting index
     */
    private void heapifyUp(int index) {
        while (index > 0 && heap.get(index).compareTo(heap.get(parent(index))) < 0) {
            swap(index, parent(index));
            index = parent(index);
        }
    }
    
    /**
     * Heapify down (restore heap property downward)
     * 
     * @param index Starting index
     */
    private void heapifyDown(int index) {
        int minIndex = index;
        int left = leftChild(index);
        int right = rightChild(index);
        
        if (left < heap.size() && heap.get(left).compareTo(heap.get(minIndex)) < 0) {
            minIndex = left;
        }
        
        if (right < heap.size() && heap.get(right).compareTo(heap.get(minIndex)) < 0) {
            minIndex = right;
        }
        
        if (index != minIndex) {
            swap(index, minIndex);
            heapifyDown(minIndex);
        }
    }
    
    /**
     * Swap two elements
     */
    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }
    
    /**
     * Get parent index
     */
    private int parent(int index) {
        return (index - 1) / 2;
    }
    
    /**
     * Get left child index
     */
    private int leftChild(int index) {
        return 2 * index + 1;
    }
    
    /**
     * Get right child index
     */
    private int rightChild(int index) {
        return 2 * index + 2;
    }
    
    /**
     * Check if heap is empty
     * 
     * @return True if empty
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }
    
    /**
     * Get heap size
     * 
     * @return Size
     */
    public int size() {
        return heap.size();
    }
    
    /**
     * Clear the heap
     */
    public void clear() {
        heap.clear();
    }
    
    /**
     * Get all elements (not in sorted order)
     * 
     * @return List of all elements
     */
    public List<T> getAll() {
        return new ArrayList<>(heap);
    }
    
    @Override
    public String toString() {
        return "MinHeap" + heap.toString();
    }
}
