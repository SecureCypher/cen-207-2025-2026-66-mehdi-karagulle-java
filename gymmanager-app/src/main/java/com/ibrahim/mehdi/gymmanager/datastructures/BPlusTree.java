package com.ibrahim.mehdi.gymmanager.datastructures;

import java.io.Serializable;
import java.util.*;

/**
 * B+ Tree implementation for member indexing.
 * Supports efficient range queries and sequential access.
 * 
 * @param <K> Key type (must be Comparable)
 * @param <V> Value type
 * @author ibrahim.mehdi
 */
public class BPlusTree<K extends Comparable<K>, V> implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int ORDER = 4; // Minimum degree
    
    private abstract static class Node<K extends Comparable<K>, V> implements Serializable {
        private static final long serialVersionUID = 1L;
        List<K> keys;
        
        Node() {
            keys = new ArrayList<>();
        }
        
        abstract boolean isLeaf();
    }
    
    private static class InternalNode<K extends Comparable<K>, V> extends Node<K, V> {
        private static final long serialVersionUID = 1L;
        List<Node<K, V>> children;
        
        InternalNode() {
            super();
            children = new ArrayList<>();
        }
        
        @Override
        boolean isLeaf() {
            return false;
        }
    }
    
    private static class LeafNode<K extends Comparable<K>, V> extends Node<K, V> {
        private static final long serialVersionUID = 1L;
        List<V> values;
        LeafNode<K, V> next;
        
        LeafNode() {
            super();
            values = new ArrayList<>();
        }
        
        @Override
        boolean isLeaf() {
            return true;
        }
    }
    
    private Node<K, V> root;
    private int size;
    
    public BPlusTree() {
        root = new LeafNode<>();
        size = 0;
    }
    
    /**
     * Insert key-value pair
     */
    public void insert(K key, V value) {
        if (key == null) throw new IllegalArgumentException("Key cannot be null");
        
        if (root.keys.size() >= ORDER - 1) {
            InternalNode<K, V> newRoot = new InternalNode<>();
            newRoot.children.add(root);
            splitChild(newRoot, 0);
            root = newRoot;
        }
        
        insertNonFull(root, key, value);
        size++;
    }
    
    @SuppressWarnings("unchecked")
    private void insertNonFull(Node<K, V> node, K key, V value) {
        if (node.isLeaf()) {
            LeafNode<K, V> leaf = (LeafNode<K, V>) node;
            int i = 0;
            while (i < leaf.keys.size() && key.compareTo(leaf.keys.get(i)) > 0) {
                i++;
            }
            leaf.keys.add(i, key);
            leaf.values.add(i, value);
        } else {
            InternalNode<K, V> internal = (InternalNode<K, V>) node;
            int i = 0;
            while (i < internal.keys.size() && key.compareTo(internal.keys.get(i)) > 0) {
                i++;
            }
            
            if (internal.children.get(i).keys.size() >= ORDER - 1) {
                splitChild(internal, i);
                if (key.compareTo(internal.keys.get(i)) > 0) {
                    i++;
                }
            }
            
            insertNonFull(internal.children.get(i), key, value);
        }
    }
    
    @SuppressWarnings("unchecked")
    private void splitChild(InternalNode<K, V> parent, int index) {
        Node<K, V> child = parent.children.get(index);
        int mid = ORDER / 2;
        
        if (child.isLeaf()) {
            LeafNode<K, V> left = (LeafNode<K, V>) child;
            LeafNode<K, V> right = new LeafNode<>();
            
            right.keys.addAll(left.keys.subList(mid, left.keys.size()));
            right.values.addAll(left.values.subList(mid, left.values.size()));
            
            left.keys.subList(mid, left.keys.size()).clear();
            left.values.subList(mid, left.values.size()).clear();
            
            right.next = left.next;
            left.next = right;
            
            parent.keys.add(index, right.keys.get(0));
            parent.children.add(index + 1, right);
        } else {
            InternalNode<K, V> left = (InternalNode<K, V>) child;
            InternalNode<K, V> right = new InternalNode<>();
            
            right.keys.addAll(left.keys.subList(mid + 1, left.keys.size()));
            right.children.addAll(left.children.subList(mid + 1, left.children.size()));
            
            K promoteKey = left.keys.get(mid);
            
            left.keys.subList(mid, left.keys.size()).clear();
            left.children.subList(mid + 1, left.children.size()).clear();
            
            parent.keys.add(index, promoteKey);
            parent.children.add(index + 1, right);
        }
    }
    
    /**
     * Search for value by key
     */
    @SuppressWarnings("unchecked")
    public V search(K key) {
        Node<K, V> node = root;
        
        while (!node.isLeaf()) {
            InternalNode<K, V> internal = (InternalNode<K, V>) node;
            int i = 0;
            while (i < internal.keys.size() && key.compareTo(internal.keys.get(i)) >= 0) {
                i++;
            }
            node = internal.children.get(i);
        }
        
        LeafNode<K, V> leaf = (LeafNode<K, V>) node;
        for (int i = 0; i < leaf.keys.size(); i++) {
            if (leaf.keys.get(i).equals(key)) {
                return leaf.values.get(i);
            }
        }
        
        return null;
    }
    
    /**
     * Range search
     */
    @SuppressWarnings("unchecked")
    public List<V> rangeSearch(K start, K end) {
        List<V> result = new ArrayList<>();
        Node<K, V> node = root;
        
        // Find starting leaf
        while (!node.isLeaf()) {
            InternalNode<K, V> internal = (InternalNode<K, V>) node;
            int i = 0;
            while (i < internal.keys.size() && start.compareTo(internal.keys.get(i)) >= 0) {
                i++;
            }
            node = internal.children.get(i);
        }
        
        // Traverse leaves
        LeafNode<K, V> leaf = (LeafNode<K, V>) node;
        while (leaf != null) {
            for (int i = 0; i < leaf.keys.size(); i++) {
                K key = leaf.keys.get(i);
                if (key.compareTo(start) >= 0 && key.compareTo(end) <= 0) {
                    result.add(leaf.values.get(i));
                } else if (key.compareTo(end) > 0) {
                    return result;
                }
            }
            leaf = leaf.next;
        }
        
        return result;
    }
    
    public int size() {
        return size;
    }
    
    public boolean isEmpty() {
        return size == 0;
    }
}
