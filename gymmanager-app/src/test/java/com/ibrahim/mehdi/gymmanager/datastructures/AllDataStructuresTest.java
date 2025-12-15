package com.ibrahim.mehdi.gymmanager.datastructures;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

/**
 * Comprehensive test class for all 12 data structures
 * 
 * @author ibrahim.mehdi
 */
public class AllDataStructuresTest {
    
    @Test
    @DisplayName("1. Double Linked List - Navigation Test")
    public void testDoubleLinkedList() {
        DoubleLinkedList<String> list = new DoubleLinkedList<>();
        
        list.add("First");
        list.add("Second");
        list.add("Third");
        
        assertEquals(3, list.size());
        assertEquals("First", list.getCurrent());
        
        assertEquals("Second", list.navigateForward());
        assertEquals("Third", list.navigateForward());
        
        assertEquals("Second", list.navigateBackward());
        assertEquals("First", list.navigateBackward());
    }
    
    @Test
    @DisplayName("2. XOR Linked List - Memory Efficient Test")
    public void testXORLinkedList() {
        XORLinkedList<Integer> list = new XORLinkedList<>();
        
        list.add(10);
        list.add(20);
        list.add(30);
        
        assertEquals(3, list.size());
        
        List<Integer> forward = list.traverseForward();
        assertEquals(3, forward.size());
        assertEquals(10, forward.get(0));
        assertEquals(30, forward.get(2));
        
        List<Integer> backward = list.traverseBackward();
        assertEquals(30, backward.get(0));
        assertEquals(10, backward.get(2));
    }
    
    @Test
    @DisplayName("3. Sparse Matrix - Memory Efficiency Test")
    public void testSparseMatrix() {
        SparseMatrix<String> matrix = new SparseMatrix<>(100, 100);
        
        matrix.set(0, 0, "Equipment1");
        matrix.set(99, 99, "Equipment2");
        
        assertEquals(2, matrix.getNonZeroCount());
        assertTrue(matrix.getSparsity() < 0.001); // Very sparse
        
        assertEquals("Equipment1", matrix.get(0, 0));
        assertEquals("Equipment2", matrix.get(99, 99));
        assertNull(matrix.get(50, 50));
    }
    
    @Test
    @DisplayName("4. Stack - LIFO Undo Test")
    public void testStack() {
        GymStack<String> stack = new GymStack<>();
        
        stack.push("Action1");
        stack.push("Action2");
        stack.push("Action3");
        
        assertEquals(3, stack.size());
        assertEquals("Action3", stack.pop());
        assertEquals("Action2", stack.pop());
        assertEquals(1, stack.size());
    }
    
    @Test
    @DisplayName("5. Queue - FIFO Test")
    public void testQueue() {
        GymQueue<String> queue = new GymQueue<>();
        
        queue.enqueue("Member1");
        queue.enqueue("Member2");
        queue.enqueue("Member3");
        
        assertEquals(3, queue.size());
        assertEquals("Member1", queue.dequeue());
        assertEquals("Member2", queue.dequeue());
        assertEquals(1, queue.size());
    }
    
    @Test
    @DisplayName("6. Min Heap - Priority Queue Test")
    public void testMinHeap() {
        MinHeap<Integer> heap = new MinHeap<>();
        
        heap.insert(5);
        heap.insert(3);
        heap.insert(7);
        heap.insert(1);
        
        assertEquals(4, heap.size());
        assertEquals(1, heap.extractMin());
        assertEquals(3, heap.extractMin());
        assertEquals(5, heap.extractMin());
    }
    
    @Test
    @DisplayName("7. Hash Table - Fast Lookup Test")
    public void testHashTable() {
        HashTable<Integer, String> table = new HashTable<>();
        
        table.put(1, "Member1");
        table.put(2, "Member2");
        table.put(3, "Member3");
        
        assertEquals(3, table.size());
        assertEquals("Member1", table.get(1));
        assertEquals("Member2", table.get(2));
        
        assertTrue(table.containsKey(3));
        assertFalse(table.containsKey(99));
    }
    
    @Test
    @DisplayName("8. Graph BFS/DFS Test")
    public void testGraph() {
        Graph graph = new Graph(5);
        
        graph.addEdge(0, 1);
        graph.addEdge(0, 2);
        graph.addEdge(1, 3);
        graph.addEdge(2, 4);
        
        List<Integer> bfs = graph.bfs(0);
        assertTrue(bfs.size() >= 1);
        assertEquals(0, bfs.get(0));
        
        List<Integer> dfs = graph.dfs(0);
        assertTrue(dfs.size() >= 1);
        assertEquals(0, dfs.get(0));
    }
    
    @Test
    @DisplayName("9. SCC - Strongly Connected Components Test")
    public void testStronglyConnectedComponents() {
        Graph graph = new Graph(4);
        
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(2, 0);
        graph.addEdge(2, 3);
        
        List<List<Integer>> sccs = graph.findSCC();
        assertNotNull(sccs);
        assertTrue(sccs.size() > 0);
    }
    
    @Test
    @DisplayName("10. KMP Algorithm - Pattern Matching Test")
    public void testKMPAlgorithm() {
        KMPAlgorithm kmp = new KMPAlgorithm();
        
        String text = "ABABDABACDABABCABAB";
        String pattern = "ABABCABAB";
        
        List<Integer> matches = kmp.search(text, pattern);
        assertEquals(1, matches.size());
        assertEquals(10, matches.get(0));
        
        assertTrue(kmp.contains("hello world", "world"));
        assertFalse(kmp.contains("hello world", "xyz"));
    }
    
    @Test
    @DisplayName("11. Huffman Coding - Compression Test")
    public void testHuffmanCoding() {
        HuffmanCoding huffman = new HuffmanCoding();
        
        String original = "hello world";
        String encoded = huffman.encode(original);
        
        assertNotNull(encoded);
        assertTrue(encoded.length() > 0);
        
        String decoded = huffman.decode(encoded);
        assertEquals(original, decoded);
        
        double ratio = huffman.getCompressionRatio(original, encoded);
        assertTrue(ratio >= 0);
    }
    
    @Test
    @DisplayName("12. B+ Tree - Range Query Test")
    public void testBPlusTree() {
        BPlusTree<Integer, String> tree = new BPlusTree<>();
        
        tree.insert(1, "Member1");
        tree.insert(5, "Member5");
        tree.insert(3, "Member3");
        tree.insert(7, "Member7");
        
        assertEquals(4, tree.size());
        assertEquals("Member1", tree.search(1));
        assertEquals("Member5", tree.search(5));
        
        List<String> range = tree.rangeSearch(2, 6);
        assertTrue(range.size() >= 2);
    }
    
    @Test
    @DisplayName("13. Linear Probing Hash - File Operations Test")
    public void testLinearProbingHash() {
        LinearProbingHash<String, String> hash = new LinearProbingHash<>();
        
        hash.put("file1.txt", "metadata1");
        hash.put("file2.txt", "metadata2");
        hash.put("file3.txt", "metadata3");
        
        assertEquals(3, hash.size());
        assertEquals("metadata1", hash.get("file1.txt"));
        assertEquals("metadata2", hash.get("file2.txt"));
        
        assertTrue(hash.containsKey("file3.txt"));
        assertFalse(hash.containsKey("file99.txt"));
        
        assertTrue(hash.getLoadFactor() < 1.0);
    }
}
