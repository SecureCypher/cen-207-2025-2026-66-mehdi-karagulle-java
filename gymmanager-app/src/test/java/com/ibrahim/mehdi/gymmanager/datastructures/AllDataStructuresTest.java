package com.ibrahim.mehdi.gymmanager.datastructures;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Test class for all data structures
 * 
 * @author ibrahim.mehdi
 */
public class AllDataStructuresTest {
    
    @Test
    @DisplayName("GymQueue Operations")
    public void testGymQueue() {
        GymQueue<String> queue = new GymQueue<>();
        
        assertTrue(queue.isEmpty());
        
        queue.enqueue("First");
        queue.enqueue("Second");
        queue.enqueue("Third");
        
        assertEquals(3, queue.size());
        assertEquals("First", queue.peek());
        
        List<String> list = queue.toList();
        assertEquals(3, list.size());
        assertEquals("First", list.get(0));
        
        assertEquals("First", queue.dequeue());
        assertEquals(2, queue.size());
    }
    
    @Test
    @DisplayName("GymStack Operations")
    public void testGymStack() {
        GymStack<Integer> stack = new GymStack<>(10);
        
        assertTrue(stack.isEmpty());
        
        stack.push(1);
        stack.push(2);
        stack.push(3);
        
        assertEquals(3, stack.size());
        assertEquals(3, (int)stack.peek());
        assertEquals(3, (int)stack.pop());
        assertEquals(2, stack.size());
    }
    
    @Test
    @DisplayName("HashTable Operations")
    public void testHashTable() {
        HashTable<String, Integer> table = new HashTable<>();
        
        table.put("one", 1);
        table.put("two", 2);
        table.put("three", 3);
        
        assertEquals(3, table.size());
        assertEquals(2, (int)table.get("two"));
        assertTrue(table.containsKey("one"));
        
        table.remove("two");
        assertEquals(2, table.size());
        assertFalse(table.containsKey("two"));
    }
    
    @Test
    @DisplayName("MinHeap Operations")
    public void testMinHeap() {
        MinHeap<Integer> heap = new MinHeap<>();
        
        heap.insert(5);
        heap.insert(3);
        heap.insert(7);
        heap.insert(1);
        
        assertEquals(4, heap.size());
        assertEquals(1, (int)heap.peekMin());
        assertEquals(1, (int)heap.extractMin());
        assertEquals(3, heap.size());
        assertEquals(3, (int)heap.peekMin());
    }
    
    @Test
    @DisplayName("DoubleLinkedList Operations")
    public void testDoubleLinkedList() {
        DoubleLinkedList<String> list = new DoubleLinkedList<>();
        
        list.add("First");
        list.add("Second");
        list.add("Third");
        
        assertEquals(3, list.size());
        assertEquals("First", list.get(0));
        
        list.resetNavigation();
        assertEquals("First", list.getCurrent());
        
        String next = list.navigateForward();
        assertEquals("Second", next);
        
        String prev = list.navigateBackward();
        assertEquals("First", prev);
    }
    
    @Test
    @DisplayName("SparseMatrix Operations")
    public void testSparseMatrix() {
        SparseMatrix<String> matrix = new SparseMatrix<>(10, 10);
        
        matrix.set(5, 5, "Center");
        matrix.set(0, 0, "TopLeft");
        matrix.set(9, 9, "BottomRight");
        
        assertEquals(3, matrix.getNonZeroCount());
        assertEquals("Center", matrix.get(5, 5));
        assertTrue(matrix.hasValue(0, 0));
        assertFalse(matrix.hasValue(5, 6));
    }
    
    @Test
    @DisplayName("KMP Search")
    public void testKMPAlgorithm() {
        KMPAlgorithm kmp = new KMPAlgorithm();
        
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";
        
        List<Integer> matches = kmp.search(text, pattern);
        assertFalse(matches.isEmpty());
        assertTrue(kmp.contains(text, "ABAD"));
        assertEquals(2, kmp.countOccurrences(text, "ABAB"));
    }
    
    @Test
    @DisplayName("Graph BFS/DFS")
    public void testGraph() {
        Graph graph = new Graph(5);
        
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        
        List<Integer> bfs = graph.bfs(0);
        assertFalse(bfs.isEmpty());
        assertEquals(5, bfs.size());
        
        List<Integer> dfs = graph.dfs(0);
        assertFalse(dfs.isEmpty());
        assertEquals(5, dfs.size());
    }
}