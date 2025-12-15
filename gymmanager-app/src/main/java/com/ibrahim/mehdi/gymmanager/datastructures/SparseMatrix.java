package com.ibrahim.mehdi.gymmanager.datastructures;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Sparse Matrix implementation for equipment location map.
 * Only stores non-zero values to save memory.
 * 
 * @param <T> Type of elements
 * @author ibrahim.mehdi
 */
public class SparseMatrix<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * Position class for matrix coordinates
     */
    private static class Position implements Serializable {
        private static final long serialVersionUID = 1L;
        int row;
        int col;
        
        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Position position = (Position) obj;
            return row == position.row && col == position.col;
        }
        
        @Override
        public int hashCode() {
            return 31 * row + col;
        }
        
        @Override
        public String toString() {
            return "(" + row + "," + col + ")";
        }
    }
    
    private Map<Position, T> matrix;
    private int rows;
    private int cols;
    private int nonZeroCount;
    
    /**
     * Constructor with dimensions
     * 
     * @param rows Number of rows
     * @param cols Number of columns
     */
    public SparseMatrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.matrix = new HashMap<>();
        this.nonZeroCount = 0;
    }
    
    /**
     * Set value at position
     * 
     * @param row Row index
     * @param col Column index
     * @param value Value to set
     */
    public void set(int row, int col, T value) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException(
                String.format("Invalid position (%d,%d)", row, col));
        }
        
        Position pos = new Position(row, col);
        
        if (value == null) {
            if (matrix.remove(pos) != null) {
                nonZeroCount--;
            }
        } else {
            if (matrix.put(pos, value) == null) {
                nonZeroCount++;
            }
        }
    }
    
    /**
     * Get value at position
     * 
     * @param row Row index
     * @param col Column index
     * @return Value at position or null
     */
    public T get(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException(
                String.format("Invalid position (%d,%d)", row, col));
        }
        
        return matrix.get(new Position(row, col));
    }
    
    /**
     * Check if position has a value
     * 
     * @param row Row index
     * @param col Column index
     * @return True if position has non-null value
     */
    public boolean hasValue(int row, int col) {
        return matrix.containsKey(new Position(row, col));
    }
    
    /**
     * Get all non-zero positions
     * 
     * @return Map of positions to values
     */
    public Map<String, T> getNonZeroElements() {
        Map<String, T> result = new HashMap<>();
        for (Map.Entry<Position, T> entry : matrix.entrySet()) {
            result.put(entry.getKey().toString(), entry.getValue());
        }
        return result;
    }
    
    /**
     * Get memory efficiency ratio
     * 
     * @return Ratio of used cells to total cells
     */
    public double getSparsity() {
        int totalCells = rows * cols;
        return totalCells > 0 ? (double) nonZeroCount / totalCells : 0;
    }
    
    /**
     * Clear all values
     */
    public void clear() {
        matrix.clear();
        nonZeroCount = 0;
    }
    
    /**
     * Get number of rows
     * 
     * @return Number of rows
     */
    public int getRows() {
        return rows;
    }
    
    /**
     * Get number of columns
     * 
     * @return Number of columns
     */
    public int getCols() {
        return cols;
    }
    
    /**
     * Get count of non-zero elements
     * 
     * @return Non-zero count
     */
    public int getNonZeroCount() {
        return nonZeroCount;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("SparseMatrix[%dx%d, NonZero=%d, Sparsity=%.2f%%]\n",
                rows, cols, nonZeroCount, getSparsity() * 100));
        
        for (Map.Entry<Position, T> entry : matrix.entrySet()) {
            sb.append(String.format("  %s = %s\n", 
                entry.getKey(), entry.getValue()));
        }
        
        return sb.toString();
    }
}
