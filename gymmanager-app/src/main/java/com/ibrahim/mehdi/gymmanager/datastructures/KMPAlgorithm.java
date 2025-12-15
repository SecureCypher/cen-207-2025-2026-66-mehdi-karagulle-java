package com.ibrahim.mehdi.gymmanager.datastructures;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * KMP (Knuth-Morris-Pratt) string pattern matching algorithm.
 * Used for efficient member name search.
 * 
 * @author ibrahim.mehdi
 */
public class KMPAlgorithm implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * Compute LPS (Longest Proper Prefix which is also Suffix) array
     */
    private int[] computeLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        
        lps[0] = 0;
        
        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        
        return lps;
    }
    
    /**
     * Search for pattern in text using KMP algorithm
     * 
     * @param text Text to search in
     * @param pattern Pattern to search for
     * @return List of starting indices where pattern is found
     */
    public List<Integer> search(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        
        if (text == null || pattern == null || pattern.isEmpty()) {
            return matches;
        }
        
        int n = text.length();
        int m = pattern.length();
        
        int[] lps = computeLPS(pattern);
        
        int i = 0; // Index for text
        int j = 0; // Index for pattern
        
        while (i < n) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }
            
            if (j == m) {
                matches.add(i - j);
                j = lps[j - 1];
            } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        
        return matches;
    }
    
    /**
     * Check if pattern exists in text
     */
    public boolean contains(String text, String pattern) {
        return !search(text, pattern).isEmpty();
    }
    
    /**
     * Count occurrences of pattern in text
     */
    public int countOccurrences(String text, String pattern) {
        return search(text, pattern).size();
    }
}
