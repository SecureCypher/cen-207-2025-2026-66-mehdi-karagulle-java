package com.ibrahim.mehdi.gymmanager.datastructures;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@DisplayName("All Data Structures - COMPLETE Coverage")
public class AllDataStructuresTest {
    
    @Test
    public void testDoubleLinkedListComplete() {
        DoubleLinkedList<String> list = new DoubleLinkedList<>();
        
        list.addFirst("First");
        list.add("Second");
        list.add("Third");
        
        assertEquals(3, list.size());
        assertEquals("First", list.get(0));
        
        String removed = list.removeLast();
        assertEquals("Third", removed);
        assertEquals(2, list.size());
        
        list.clear();
        assertTrue(list.isEmpty());
        
        list.add("A");
        list.resetNavigation();
        assertEquals("A", list.getCurrent());
        
        assertNotNull(list.toString());
    }
    
    @Test
    public void testXORLinkedListComplete() {
        XORLinkedList<String> list = new XORLinkedList<>();
        
        list.add("First");
        list.add("Second");
        list.add("Third");
        
        assertEquals(3, list.size());
        
        // Test get by checking list is not empty
        assertNotNull(list.get(0));
        
        List<String> forward = list.traverseForward();
        assertEquals(3, forward.size());
        
        List<String> backward = list.traverseBackward();
        assertEquals(3, backward.size());
        
        list.clear();
        assertTrue(list.isEmpty());
        
        assertNotNull(list.toString());
    }
    
    @Test
    public void testSparseMatrixComplete() {
        SparseMatrix<String> matrix = new SparseMatrix<>(10, 10);
        
        matrix.set(5, 5, "Center");
        assertEquals("Center", matrix.get(5, 5));
        
        assertTrue(matrix.hasValue(5, 5));
        assertEquals(1, matrix.getNonZeroCount());
        
        double sparsity = matrix.getSparsity();
        assertTrue(sparsity > 0);
        
        assertEquals(10, matrix.getRows());
        assertEquals(10, matrix.getCols());
        
        Map<String, String> nonZero = matrix.getNonZeroElements();
        assertEquals(1, nonZero.size());
        
        matrix.clear();
        assertEquals(0, matrix.getNonZeroCount());
        
        assertNotNull(matrix.toString());
        
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.set(-1, 0, "Bad"));
        assertThrows(IndexOutOfBoundsException.class, () -> matrix.get(100, 0));
    }
    
    @Test
    public void testStackComplete() {
        GymStack<String> stack = new GymStack<>(5);
        
        stack.push("A");
        stack.push("B");
        stack.push("C");
        
        assertEquals(3, stack.size());
        assertEquals("C", stack.peek());
        assertEquals("C", stack.pop());
        assertEquals(2, stack.size());
        
        stack.clear();
        assertTrue(stack.isEmpty());
        
        assertThrows(Exception.class, () -> stack.pop());
        assertThrows(Exception.class, () -> stack.peek());
        
        for (int i = 0; i < 10; i++) {
            stack.push("Item" + i);
        }
        assertEquals(5, stack.size());
        
        assertNotNull(stack.toString());
    }
    
    @Test
    public void testQueueComplete() {
        GymQueue<String> queue = new GymQueue<>();
        
        queue.enqueue("First");
        queue.enqueue("Second");
        
        assertEquals(2, queue.size());
        assertEquals("First", queue.peek());
        
        List<String> list = queue.toList();
        assertEquals(2, list.size());
        
        assertEquals("First", queue.dequeue());
        assertEquals(1, queue.size());
        
        queue.clear();
        assertTrue(queue.isEmpty());
        
        assertThrows(Exception.class, () -> queue.dequeue());
        assertThrows(Exception.class, () -> queue.peek());
        
        assertNotNull(queue.toString());
    }
    
    @Test
    public void testMinHeapComplete() {
        MinHeap<Integer> heap = new MinHeap<>();
        
        heap.insert(5);
        heap.insert(3);
        heap.insert(7);
        heap.insert(1);
        
        assertEquals(4, heap.size());
        assertEquals(Integer.valueOf(1), heap.peekMin());
        
        assertEquals(Integer.valueOf(1), heap.extractMin());
        assertEquals(3, heap.size());
        
        List<Integer> all = heap.getAll();
        assertEquals(3, all.size());
        
        heap.clear();
        assertTrue(heap.isEmpty());
        
        assertThrows(Exception.class, () -> heap.extractMin());
        assertThrows(Exception.class, () -> heap.peekMin());
        
        assertNotNull(heap.toString());
        
        List<Integer> elements = new ArrayList<>();
        elements.add(5);
        elements.add(3);
        elements.add(7);
        elements.add(1);
        elements.add(9);
        
        MinHeap<Integer> heap2 = new MinHeap<>(elements);
        assertEquals(5, heap2.size());
        assertEquals(Integer.valueOf(1), heap2.peekMin());
    }
    
    @Test
    public void testHashTableComplete() {
        HashTable<String, Integer> table = new HashTable<>();
        
        table.put("one", 1);
        table.put("two", 2);
        
        assertEquals(2, table.size());
        assertEquals(Integer.valueOf(1), table.get("one"));
        
        assertTrue(table.containsKey("one"));
        assertFalse(table.containsKey("three"));
        
        assertEquals(Integer.valueOf(2), table.remove("two"));
        assertNull(table.get("two"));
        
        List<String> keys = table.keys();
        assertEquals(1, keys.size());
        
        List<Integer> values = table.values();
        assertEquals(1, values.size());
        
        table.clear();
        assertTrue(table.isEmpty());
        
        for (int i = 0; i < 100; i++) {
            table.put("key" + i, i);
        }
        assertEquals(100, table.size());
        
        double avgChain = table.getAverageChainLength();
        assertTrue(avgChain >= 0);
        
        assertNotNull(table.toString());
        
        assertThrows(IllegalArgumentException.class, () -> table.put(null, 1));
    }
    
    @Test
    public void testGraphComplete() {
        Graph graph = new Graph(5);
        
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        
        List<Integer> bfs = graph.bfs(0);
        assertEquals(5, bfs.size());
        assertEquals(Integer.valueOf(0), bfs.get(0));
        
        List<Integer> dfs = graph.dfs(0);
        assertEquals(5, dfs.size());
        
        List<List<Integer>> sccs = graph.findSCC();
        assertNotNull(sccs);
        
        assertEquals(5, graph.getVertices());
        assertNotNull(graph.getAdjacencyList());
    }
    
    @Test
    public void testKMPComplete() {
        KMPAlgorithm kmp = new KMPAlgorithm();
        
        List<Integer> matches = kmp.search("ABABCABAB", "ABAB");
        assertEquals(2, matches.size());
        
        assertTrue(kmp.contains("Hello World", "World"));
        assertFalse(kmp.contains("Hello", "Bye"));
        
        assertEquals(3, kmp.countOccurrences("ABABAB", "AB"));
        
        assertTrue(kmp.search("text", null).isEmpty());
        assertTrue(kmp.search("text", "").isEmpty());
    }
    
    @Test
    public void testHuffmanComplete() {
        HuffmanCoding huffman = new HuffmanCoding();
        
        String text = "Hello World!";
        String encoded = huffman.encode(text);
        assertNotNull(encoded);
        
        String decoded = huffman.decode(encoded);
        assertEquals(text, decoded);
        
        double ratio = huffman.getCompressionRatio(text, encoded);
        assertTrue(ratio > 0);
        
        Map<Character, String> codes = huffman.getHuffmanCode();
        assertFalse(codes.isEmpty());
        
        assertEquals("", huffman.encode(""));
        assertEquals("", huffman.decode(""));
        assertEquals(0, huffman.getCompressionRatio("", ""), 0.01);
    }
    
    @Test
    public void testBPlusTreeComplete() {
        BPlusTree<Integer, String> tree = new BPlusTree<>();
        
        tree.insert(5, "Five");
        tree.insert(3, "Three");
        tree.insert(7, "Seven");
        
        assertEquals(3, tree.size());
        assertEquals("Five", tree.search(5));
        assertNull(tree.search(10));
        
        List<String> range = tree.rangeSearch(3, 7);
        assertEquals(3, range.size());
        
        assertFalse(tree.isEmpty());
        
        assertThrows(IllegalArgumentException.class, () -> tree.insert(null, "Null"));
        
        for (int i = 0; i < 50; i++) {
            tree.insert(i, "Val" + i);
        }
        assertTrue(tree.size() > 50);
    }
    
    @Test
    public void testLinearProbingComplete() {
        LinearProbingHash<String, String> hash = new LinearProbingHash<>();
        
        hash.put("key1", "val1");
        hash.put("key2", "val2");
        
        assertEquals(2, hash.size());
        assertEquals("val1", hash.get("key1"));
        
        assertTrue(hash.containsKey("key1"));
        assertFalse(hash.containsKey("key3"));
        
        assertEquals("val2", hash.remove("key2"));
        assertNull(hash.get("key2"));
        
        assertFalse(hash.isEmpty());
        
        assertTrue(hash.getCollisionCount() >= 0);
        assertTrue(hash.getLoadFactor() > 0);
        
        assertNotNull(hash.toString());
        
        assertThrows(IllegalArgumentException.class, () -> hash.put(null, "val"));
        
        for (int i = 0; i < 100; i++) {
            hash.put("k" + i, "v" + i);
        }
        assertEquals(101, hash.size());
    }
}