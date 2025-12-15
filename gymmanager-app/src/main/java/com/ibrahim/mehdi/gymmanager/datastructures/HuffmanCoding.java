package com.ibrahim.mehdi.gymmanager.datastructures;

import java.io.Serializable;
import java.util.*;

/**
 * Huffman Coding for data compression.
 * Used for compressing gym reports and data.
 * 
 * @author ibrahim.mehdi
 */
public class HuffmanCoding implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static class Node implements Comparable<Node>, Serializable {
        private static final long serialVersionUID = 1L;
        char character;
        int frequency;
        Node left, right;
        
        Node(char character, int frequency) {
            this.character = character;
            this.frequency = frequency;
        }
        
        @Override
        public int compareTo(Node other) {
            return this.frequency - other.frequency;
        }
    }
    
    private Map<Character, String> huffmanCode = new HashMap<>();
    private Node root;
    
    /**
     * Encode text using Huffman coding
     */
    public String encode(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        
        // Calculate frequency
        Map<Character, Integer> frequency = new HashMap<>();
        for (char c : text.toCharArray()) {
            frequency.put(c, frequency.getOrDefault(c, 0) + 1);
        }
        
        // Build Huffman tree
        PriorityQueue<Node> pq = new PriorityQueue<>();
        for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
            pq.offer(new Node(entry.getKey(), entry.getValue()));
        }
        
        while (pq.size() > 1) {
            Node left = pq.poll();
            Node right = pq.poll();
            
            Node parent = new Node('\0', left.frequency + right.frequency);
            parent.left = left;
            parent.right = right;
            
            pq.offer(parent);
        }
        
        root = pq.poll();
        
        // Generate codes
        huffmanCode.clear();
        generateCodes(root, "");
        
        // Encode text
        StringBuilder encoded = new StringBuilder();
        for (char c : text.toCharArray()) {
            encoded.append(huffmanCode.get(c));
        }
        
        return encoded.toString();
    }
    
    /**
     * Decode Huffman encoded text
     */
    public String decode(String encoded) {
        if (encoded == null || encoded.isEmpty() || root == null) {
            return "";
        }
        
        StringBuilder decoded = new StringBuilder();
        Node current = root;
        
        for (char bit : encoded.toCharArray()) {
            current = (bit == '0') ? current.left : current.right;
            
            if (current.left == null && current.right == null) {
                decoded.append(current.character);
                current = root;
            }
        }
        
        return decoded.toString();
    }
    
    private void generateCodes(Node node, String code) {
        if (node == null) return;
        
        if (node.left == null && node.right == null) {
            huffmanCode.put(node.character, code.isEmpty() ? "0" : code);
            return;
        }
        
        generateCodes(node.left, code + "0");
        generateCodes(node.right, code + "1");
    }
    
    /**
     * Get compression ratio
     */
    public double getCompressionRatio(String original, String encoded) {
        if (original.isEmpty()) return 0;
        int originalBits = original.length() * 8; // Assuming 8-bit chars
        int encodedBits = encoded.length();
        return (1.0 - (double) encodedBits / originalBits) * 100;
    }
    
    public Map<Character, String> getHuffmanCode() {
        return new HashMap<>(huffmanCode);
    }
}
