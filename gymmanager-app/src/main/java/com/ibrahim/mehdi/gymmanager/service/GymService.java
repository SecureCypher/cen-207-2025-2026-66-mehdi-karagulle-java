package com.ibrahim.mehdi.gymmanager.service;

import com.ibrahim.mehdi.gymmanager.datastructures.*;
import com.ibrahim.mehdi.gymmanager.model.*;
import com.ibrahim.mehdi.gymmanager.util.BinaryFileStorage;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Main service class integrating all 12 data structures.
 * Implements Serializable for binary file storage.
 * 
 * @author ibrahim.mehdi
 */
public class GymService implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String DATA_FILE = "gymservice.dat";
    
    // 1. Double Linked List - Member history navigation
    private DoubleLinkedList<String> memberHistory;
    
    // 2. XOR Linked List - Workout history
    private XORLinkedList<String> workoutHistory;
    
    // 3. Sparse Matrix - Equipment location map (20x20)
    private SparseMatrix<Equipment> equipmentMap;
    
    // 4. Stack - Undo operations (max 50)
    private GymStack<String> undoStack;
    
    // 5. Queue - Waiting member queue
    private GymQueue<Member> waitingQueue;
    
    // 6. Min Heap - Appointment priority queue
    private MinHeap<Appointment> appointmentHeap;
    
    // 7. Hash Table - Fast member lookup
    private HashTable<Integer, Member> memberHashTable;
    
    // 8. Graph - Equipment dependencies
    private Graph equipmentGraph;
    
    // 9. KMP Algorithm - Name search
    private KMPAlgorithm kmpSearch;
    
    // 10. Huffman Coding - Data compression
    private HuffmanCoding huffman;
    
    // 11. B+ Tree - Member indexing
    private BPlusTree<Integer, Member> memberIndex;
    
    // 12. Linear Probing Hash - File operations
    private LinearProbingHash<String, String> fileOperations;
    
    // Counters
    private int nextMemberId = 1;
    private int nextAppointmentId = 1;
    private int nextEquipmentId = 1;
    
    /**
     * Constructor - Initialize all data structures
     */
    public GymService() {
        // Load saved data or initialize
        GymService loaded = BinaryFileStorage.load(DATA_FILE);
        
        if (loaded != null) {
            copyFrom(loaded);
        } else {
            initializeDataStructures();
            initializeSampleData();
        }
    }
    
    /**
     * Initialize all data structures
     */
    private void initializeDataStructures() {
        memberHistory = new DoubleLinkedList<>();
        workoutHistory = new XORLinkedList<>();
        equipmentMap = new SparseMatrix<>(20, 20);
        undoStack = new GymStack<>(50);
        waitingQueue = new GymQueue<>();
        appointmentHeap = new MinHeap<>();
        memberHashTable = new HashTable<>();
        equipmentGraph = new Graph(20);
        kmpSearch = new KMPAlgorithm();
        huffman = new HuffmanCoding();
        memberIndex = new BPlusTree<>();
        fileOperations = new LinearProbingHash<>();
    }
    
    /**
     * Copy data from loaded service
     */
    private void copyFrom(GymService other) {
        this.memberHistory = other.memberHistory;
        this.workoutHistory = other.workoutHistory;
        this.equipmentMap = other.equipmentMap;
        this.undoStack = other.undoStack;
        this.waitingQueue = other.waitingQueue;
        this.appointmentHeap = other.appointmentHeap;
        this.memberHashTable = other.memberHashTable;
        this.equipmentGraph = other.equipmentGraph;
        this.kmpSearch = other.kmpSearch;
        this.huffman = other.huffman;
        this.memberIndex = other.memberIndex;
        this.fileOperations = other.fileOperations;
        this.nextMemberId = other.nextMemberId;
        this.nextAppointmentId = other.nextAppointmentId;
        this.nextEquipmentId = other.nextEquipmentId;
    }
    
    /**
     * Initialize sample data
     */
    private void initializeSampleData() {
        // Add sample members
        addMember("Ahmet", "Yılmaz", "555-0001", "ahmet@gym.com", Member.MembershipType.YEARLY);
        addMember("Mehmet", "Kaya", "555-0002", "mehmet@gym.com", Member.MembershipType.MONTHLY);
        addMember("Ayşe", "Demir", "555-0003", "ayse@gym.com", Member.MembershipType.QUARTERLY);
        
        // Add sample equipment
        addEquipment("Treadmill", "Cardio", 5, 10, 10);
        addEquipment("Bench Press", "Strength", 3, 5, 5);
        addEquipment("Dumbbells", "Free Weights", 20, 15, 15);
        
        // Add equipment dependencies
        addEquipmentDependency(0, 1);
        addEquipmentDependency(1, 2);
        
        // Add sample appointments
        createAppointment(1, "Personal Training", 1);
        createAppointment(2, "Group Class", 3);
        
        // Add workout history
        addWorkoutRecord("Ahmet completed 30min cardio");
        addWorkoutRecord("Mehmet completed strength training");
        
        saveData();
    }
    
    // ==================== MEMBER OPERATIONS ====================
    
    /**
     * Add new member
     */
    public Member addMember(String name, String surname, String phone, 
                           String email, Member.MembershipType type) {
        Member member = new Member(nextMemberId++, name, surname, phone, email, type);
        
        // 7. Hash Table - Fast lookup
        memberHashTable.put(member.getId(), member);
        
        // 11. B+ Tree - Indexing
        memberIndex.insert(member.getId(), member);
        
        // 1. Double Linked List - History
        memberHistory.add("Added member: " + member.getFullName());
        
        // 4. Stack - Undo
        undoStack.push("ADD_MEMBER:" + member.getId());
        
        // 12. File Operations
        fileOperations.put("member_" + member.getId(), member.toString());
        
        saveData();
        return member;
    }
    
    /**
     * Search member by ID
     */
    public Member searchMember(int id) {
        return memberHashTable.get(id);
    }
    
    /**
     * Search members by name using KMP algorithm
     */
    public List<Member> searchMemberByName(String searchTerm) {
        List<Member> results = new ArrayList<>();
        
        for (Member member : memberHashTable.values()) {
            String fullName = member.getFullName().toLowerCase();
            String term = searchTerm.toLowerCase();
            
            // 9. KMP Algorithm
            if (kmpSearch.contains(fullName, term)) {
                results.add(member);
            }
        }
        
        return results;
    }
    
    /**
     * Get all members
     */
    public List<Member> getAllMembers() {
        return memberHashTable.values();
    }
    
    // ==================== APPOINTMENT OPERATIONS ====================
    
    /**
     * Create appointment
     */
    public Appointment createAppointment(int memberId, String service, int priority) {
        Member member = searchMember(memberId);
        if (member == null) return null;
        
        Appointment appointment = new Appointment(
            nextAppointmentId++,
            memberId,
            member.getFullName(),
            LocalDateTime.now().plusDays(1),
            service,
            priority
        );
        
        // 6. Min Heap - Priority queue
        appointmentHeap.insert(appointment);
        
        memberHistory.add("Created appointment for: " + member.getFullName());
        undoStack.push("ADD_APPOINTMENT:" + appointment.getId());
        
        saveData();
        return appointment;
    }
    
    /**
     * Get next appointment (highest priority)
     */
    public Appointment getNextAppointment() {
        if (appointmentHeap.isEmpty()) return null;
        return appointmentHeap.peekMin();
    }
    
    /**
     * Process next appointment
     */
    public Appointment processNextAppointment() {
        if (appointmentHeap.isEmpty()) return null;
        
        Appointment appointment = appointmentHeap.extractMin();
        appointment.setStatus(Appointment.AppointmentStatus.COMPLETED);
        
        memberHistory.add("Processed appointment: " + appointment.getMemberName());
        
        saveData();
        return appointment;
    }
    
    // ==================== EQUIPMENT OPERATIONS ====================
    
    /**
     * Add equipment
     */
    public Equipment addEquipment(String name, String type, int quantity, int x, int y) {
        Equipment equipment = new Equipment(nextEquipmentId++, name, type, quantity, x, y);
        
        // 3. Sparse Matrix - Location map
        equipmentMap.set(x, y, equipment);
        
        memberHistory.add("Added equipment: " + name);
        undoStack.push("ADD_EQUIPMENT:" + equipment.getId());
        
        saveData();
        return equipment;
    }
    
    /**
     * Get equipment at location
     */
    public Equipment getEquipmentAt(int x, int y) {
        return equipmentMap.get(x, y);
    }
    
    /**
     * Get all equipment
     */
    public List<Equipment> getAllEquipment() {
        List<Equipment> equipment = new ArrayList<>();
        Map<String, Equipment> nonZero = equipmentMap.getNonZeroElements();
        equipment.addAll(nonZero.values());
        return equipment;
    }
    
    /**
     * Add equipment dependency (for graph)
     */
    public void addEquipmentDependency(int from, int to) {
        // 8. Graph - Dependencies
        equipmentGraph.addEdge(from, to);
    }
    
    /**
     * BFS traversal of equipment dependencies
     */
    public List<Integer> bfsTraversal(int start) {
        return equipmentGraph.bfs(start);
    }
    
    /**
     * DFS traversal of equipment dependencies
     */
    public List<Integer> dfsTraversal(int start) {
        return equipmentGraph.dfs(start);
    }
    
    /**
     * Find strongly connected components
     */
    public List<List<Integer>> findStronglyConnectedComponents() {
        return equipmentGraph.findSCC();
    }
    
    // ==================== QUEUE OPERATIONS ====================
    
    /**
     * Add member to waiting queue
     */
    public void addToWaitingQueue(Member member) {
        // 5. Queue - FIFO
        waitingQueue.enqueue(member);
        memberHistory.add("Added to queue: " + member.getFullName());
        saveData();
    }
    
    /**
     * Process next member in queue
     */
    public Member processNextInQueue() {
        if (waitingQueue.isEmpty()) return null;
        
        Member member = waitingQueue.dequeue();
        memberHistory.add("Processed from queue: " + member.getFullName());
        
        saveData();
        return member;
    }
    
    /**
     * Get queue size
     */
    public int getQueueSize() {
        return waitingQueue.size();
    }
    
    // ==================== HISTORY & UNDO OPERATIONS ====================
    
    /**
     * Navigate history forward
     */
    public String navigateHistoryForward() {
        return memberHistory.navigateForward();
    }
    
    /**
     * Navigate history backward
     */
    public String navigateHistoryBackward() {
        return memberHistory.navigateBackward();
    }
    
    /**
     * Get current history
     */
    public String getCurrentHistory() {
        return memberHistory.getCurrent();
    }
    
    /**
     * Undo last operation
     */
    public String undo() {
        if (undoStack.isEmpty()) return null;
        
        String action = undoStack.pop();
        memberHistory.add("Undone: " + action);
        
        saveData();
        return action;
    }
    
    // ==================== WORKOUT HISTORY (XOR List) ====================
    
    /**
     * Add workout record
     */
    public void addWorkoutRecord(String record) {
        // 2. XOR Linked List
        workoutHistory.add(record);
        saveData();
    }
    
    /**
     * Get workout history
     */
    public List<String> getWorkoutHistory() {
        return workoutHistory.traverseForward();
    }
    
    // ==================== DATA COMPRESSION ====================
    
    /**
     * Compress data using Huffman coding
     */
    public String compressData(String data) {
        // 10. Huffman Coding
        return huffman.encode(data);
    }
    
    /**
     * Decompress data
     */
    public String decompressData(String compressed) {
        return huffman.decode(compressed);
    }
    
    // ==================== RANGE QUERIES (B+ Tree) ====================
    
    /**
     * Get members in ID range
     */
    public List<Member> getRangeMembersById(int start, int end) {
        return memberIndex.rangeSearch(start, end);
    }
    
    // ==================== STATISTICS ====================
    
    /**
     * Get system statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new LinkedHashMap<>();
        
        stats.put("Total Members", memberHashTable.size());
        stats.put("Total Equipment", equipmentMap.getNonZeroCount());
        stats.put("Queue Size", waitingQueue.size());
        stats.put("Pending Appointments", appointmentHeap.size());
        stats.put("History Records", memberHistory.size());
        stats.put("Workout Records", workoutHistory.size());
        stats.put("Undo Stack Size", undoStack.size());
        
        stats.put("Hash Table Load", 
            String.format("%.2f", memberHashTable.getAverageChainLength()));
        stats.put("Matrix Sparsity", 
            String.format("%.2f%%", equipmentMap.getSparsity() * 100));
        stats.put("File Operations", fileOperations.size());
        
        return stats;
    }
    
    // ==================== DATA PERSISTENCE ====================
    
    /**
     * Save data to binary file
     */
    public void saveData() {
        BinaryFileStorage.save(DATA_FILE, this);
    }
    
    /**
     * Load data from binary file
     */
    public void loadData() {
        GymService loaded = BinaryFileStorage.load(DATA_FILE);
        if (loaded != null) {
            copyFrom(loaded);
        }
    }
}