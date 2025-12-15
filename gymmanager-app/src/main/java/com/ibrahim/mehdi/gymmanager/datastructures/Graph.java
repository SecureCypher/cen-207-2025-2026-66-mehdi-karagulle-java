package com.ibrahim.mehdi.gymmanager.datastructures;

import java.io.Serializable;
import java.util.*;

/**
 * Graph implementation for equipment dependency analysis.
 * Supports BFS, DFS, and Strongly Connected Components (SCC).
 * 
 * @author ibrahim.mehdi
 */
public class Graph implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Map<Integer, List<Integer>> adjacencyList;
    private int vertices;
    
    public Graph(int vertices) {
        this.vertices = vertices;
        this.adjacencyList = new HashMap<>();
        for (int i = 0; i < vertices; i++) {
            adjacencyList.put(i, new ArrayList<>());
        }
    }
    
    public void addEdge(int from, int to) {
        adjacencyList.get(from).add(to);
    }
    
    /**
     * BFS traversal
     */
    public List<Integer> bfs(int start) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[vertices];
        Queue<Integer> queue = new LinkedList<>();
        
        visited[start] = true;
        queue.offer(start);
        
        while (!queue.isEmpty()) {
            int vertex = queue.poll();
            result.add(vertex);
            
            for (int neighbor : adjacencyList.get(vertex)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }
        }
        
        return result;
    }
    
    /**
     * DFS traversal
     */
    public List<Integer> dfs(int start) {
        List<Integer> result = new ArrayList<>();
        boolean[] visited = new boolean[vertices];
        dfsUtil(start, visited, result);
        return result;
    }
    
    private void dfsUtil(int vertex, boolean[] visited, List<Integer> result) {
        visited[vertex] = true;
        result.add(vertex);
        
        for (int neighbor : adjacencyList.get(vertex)) {
            if (!visited[neighbor]) {
                dfsUtil(neighbor, visited, result);
            }
        }
    }
    
    /**
     * Find Strongly Connected Components using Kosaraju's algorithm
     */
    public List<List<Integer>> findSCC() {
        Stack<Integer> stack = new Stack<>();
        boolean[] visited = new boolean[vertices];
        
        // Fill stack with vertices in order of finish time
        for (int i = 0; i < vertices; i++) {
            if (!visited[i]) {
                fillOrder(i, visited, stack);
            }
        }
        
        // Create transpose graph
        Graph transpose = getTranspose();
        
        // Process all vertices in order defined by stack
        Arrays.fill(visited, false);
        List<List<Integer>> sccs = new ArrayList<>();
        
        while (!stack.isEmpty()) {
            int v = stack.pop();
            if (!visited[v]) {
                List<Integer> component = new ArrayList<>();
                transpose.dfsUtil(v, visited, component);
                sccs.add(component);
            }
        }
        
        return sccs;
    }
    
    private void fillOrder(int v, boolean[] visited, Stack<Integer> stack) {
        visited[v] = true;
        for (int neighbor : adjacencyList.get(v)) {
            if (!visited[neighbor]) {
                fillOrder(neighbor, visited, stack);
            }
        }
        stack.push(v);
    }
    
    private Graph getTranspose() {
        Graph g = new Graph(vertices);
        for (int v = 0; v < vertices; v++) {
            for (int neighbor : adjacencyList.get(v)) {
                g.adjacencyList.get(neighbor).add(v);
            }
        }
        return g;
    }
    
    public int getVertices() {
        return vertices;
    }
    
    public Map<Integer, List<Integer>> getAdjacencyList() {
        return adjacencyList;
    }
}
