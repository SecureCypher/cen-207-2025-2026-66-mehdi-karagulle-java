package com.ibrahim.mehdi.gymmanager.service;

import com.ibrahim.mehdi.gymmanager.model.*;
import com.ibrahim.mehdi.gymmanager.datastructures.GymQueue;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Enhanced Queue Manager - Manages multiple queue types
 */
public class QueueManager {
    
    // Multiple queues for different purposes
    private Map<QueueRequest.QueueType, GymQueue<QueueRequest>> queues;
    
    // Track equipment availability
    private Map<String, Integer> equipmentAvailability;  // Equipment name -> Available count
    private Map<String, Integer> equipmentInUse;         // Equipment name -> In use count
    
    // Track trainer availability
    private Map<String, Boolean> trainerAvailability;    // Trainer name -> Available
    
    // Statistics
    private int totalProcessed;
    private int totalWaitingTime;  // Total minutes
    private int requestIdCounter;
    
    public QueueManager() {
        queues = new HashMap<QueueRequest.QueueType, GymQueue<QueueRequest>>();
        
        // Initialize queues for each type
        for (QueueRequest.QueueType type : QueueRequest.QueueType.values()) {
            queues.put(type, new GymQueue<QueueRequest>());
        }
        
        equipmentAvailability = new HashMap<String, Integer>();
        equipmentInUse = new HashMap<String, Integer>();
        trainerAvailability = new HashMap<String, Boolean>();
        
        totalProcessed = 0;
        totalWaitingTime = 0;
        requestIdCounter = 1;
        
        initializeResources();
    }
    
    /**
     * Initialize gym resources
     */
    private void initializeResources() {
        // Equipment (name -> total count)
        equipmentAvailability.put("Treadmill", 5);
        equipmentAvailability.put("Bench Press", 3);
        equipmentAvailability.put("Dumbbells", 10);
        equipmentAvailability.put("Rowing Machine", 4);
        equipmentAvailability.put("Leg Press", 2);
        
        equipmentInUse.put("Treadmill", 2);      // 2 in use, 3 available
        equipmentInUse.put("Bench Press", 3);     // 3 in use, 0 available
        equipmentInUse.put("Dumbbells", 5);       // 5 in use, 5 available
        equipmentInUse.put("Rowing Machine", 1);  // 1 in use, 3 available
        equipmentInUse.put("Leg Press", 2);       // 2 in use, 0 available
        
        // Trainers
        trainerAvailability.put("John Smith", true);
        trainerAvailability.put("Sarah Johnson", false);  // Busy
        trainerAvailability.put("Mike Wilson", true);
    }
    
    /**
     * Add member to queue
     */
    public QueueRequest addToQueue(Member member, QueueRequest.QueueType queueType, 
                                   String resourceName) {
        
        QueueRequest request = new QueueRequest(requestIdCounter++, member, 
                                               queueType, resourceName);
        
        GymQueue<QueueRequest> queue = queues.get(queueType);
        
        // Check if VIP - add to front
        if (request.getPriority() == QueueRequest.Priority.VIP) {
            // VIP goes to front (after other VIPs)
            queue.enqueue(request);
        } else {
            queue.enqueue(request);
        }
        
        updateQueuePositions(queueType);
        updateEstimatedWaitTimes(queueType);
        
        return request;
    }
    
    /**
     * Process next request from specific queue
     */
    public QueueRequest processNext(QueueRequest.QueueType queueType) {
        GymQueue<QueueRequest> queue = queues.get(queueType);
        
        if (queue.isEmpty()) return null;
        
        QueueRequest request = queue.dequeue();
        
        // Update statistics
        totalProcessed++;
        totalWaitingTime += (int) request.getWaitingTimeMinutes();
        
        // Update resource usage
        if (queueType == QueueRequest.QueueType.EQUIPMENT_WAIT) {
            allocateEquipment(request.getResourceName());
        } else if (queueType == QueueRequest.QueueType.TRAINER_SESSION) {
            allocateTrainer(request.getResourceName());
        }
        
        updateQueuePositions(queueType);
        updateEstimatedWaitTimes(queueType);
        
        return request;
    }
    
    /**
     * Release equipment (member finished using it)
     */
    public void releaseEquipment(String equipmentName) {
        Integer inUse = equipmentInUse.get(equipmentName);
        if (inUse != null && inUse > 0) {
            equipmentInUse.put(equipmentName, inUse - 1);
            
            // Auto-process queue if someone waiting
            GymQueue<QueueRequest> queue = queues.get(QueueRequest.QueueType.EQUIPMENT_WAIT);
            if (!queue.isEmpty()) {
                // Check if anyone waiting for this equipment
                QueueRequest next = queue.peek();
                if (next != null && next.getResourceName().equals(equipmentName)) {
                    processNext(QueueRequest.QueueType.EQUIPMENT_WAIT);
                }
            }
        }
    }
    
    /**
     * Release trainer (session ended)
     */
    public void releaseTrainer(String trainerName) {
        trainerAvailability.put(trainerName, true);
        
        // Auto-process queue
        GymQueue<QueueRequest> queue = queues.get(QueueRequest.QueueType.TRAINER_SESSION);
        if (!queue.isEmpty()) {
            QueueRequest next = queue.peek();
            if (next != null && next.getResourceName().equals(trainerName)) {
                processNext(QueueRequest.QueueType.TRAINER_SESSION);
            }
        }
    }
    
    /**
     * Allocate equipment to member
     */
    private void allocateEquipment(String equipmentName) {
        Integer inUse = equipmentInUse.get(equipmentName);
        if (inUse != null) {
            equipmentInUse.put(equipmentName, inUse + 1);
        }
    }
    
    /**
     * Allocate trainer to member
     */
    private void allocateTrainer(String trainerName) {
        trainerAvailability.put(trainerName, false);
    }
    
    /**
     * Update queue positions
     */
    private void updateQueuePositions(QueueRequest.QueueType queueType) {
        GymQueue<QueueRequest> queue = queues.get(queueType);
        List<QueueRequest> requests = queue.toList();
        
        for (int i = 0; i < requests.size(); i++) {
            requests.get(i).setPositionInQueue(i + 1);
        }
    }
    
    /**
     * Update estimated wait times
     */
    private void updateEstimatedWaitTimes(QueueRequest.QueueType queueType) {
        GymQueue<QueueRequest> queue = queues.get(queueType);
        List<QueueRequest> requests = queue.toList();
        
        int avgServiceTime = getAverageServiceTime(queueType);
        
        for (int i = 0; i < requests.size(); i++) {
            LocalDateTime estimated = LocalDateTime.now().plusMinutes((i + 1) * avgServiceTime);
            requests.get(i).setEstimatedWaitTime(estimated);
        }
    }
    
    /**
     * Get average service time for queue type
     */
    private int getAverageServiceTime(QueueRequest.QueueType queueType) {
        switch (queueType) {
            case EQUIPMENT_WAIT:
                return 15;  // 15 min average equipment usage
            case TRAINER_SESSION:
                return 45;  // 45 min PT session
            case SHOWER_ROOM:
                return 10;  // 10 min shower
            case LOCKER_ROOM:
                return 5;   // 5 min locker
            default:
                return 10;
        }
    }
    
    /**
     * Get queue size
     */
    public int getQueueSize(QueueRequest.QueueType queueType) {
        return queues.get(queueType).size();
    }
    
    /**
     * Get all requests in queue
     */
    public List<QueueRequest> getQueueRequests(QueueRequest.QueueType queueType) {
        return queues.get(queueType).toList();
    }
    
    /**
     * Check equipment availability
     */
    public int getAvailableEquipment(String equipmentName) {
        Integer total = equipmentAvailability.get(equipmentName);
        Integer inUse = equipmentInUse.get(equipmentName);
        if (total == null || inUse == null) return 0;
        return Math.max(0, total - inUse);
    }
    
    /**
     * Check trainer availability
     */
    public boolean isTrainerAvailable(String trainerName) {
        Boolean available = trainerAvailability.get(trainerName);
        return available != null && available;
    }
    
    /**
     * Get all equipment names
     */
    public List<String> getAllEquipment() {
        return new ArrayList<String>(equipmentAvailability.keySet());
    }
    
    /**
     * Get all trainer names
     */
    public List<String> getAllTrainers() {
        return new ArrayList<String>(trainerAvailability.keySet());
    }
    
    /**
     * Get statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<String, Object>();
        
        int totalInQueue = 0;
        for (GymQueue<QueueRequest> queue : queues.values()) {
            totalInQueue += queue.size();
        }
        
        stats.put("Total in Queue", totalInQueue);
        stats.put("Total Processed", totalProcessed);
        stats.put("Avg Wait Time", totalProcessed > 0 ? 
                 (totalWaitingTime / totalProcessed) + " min" : "N/A");
        
        // Queue sizes by type
        for (QueueRequest.QueueType type : QueueRequest.QueueType.values()) {
            stats.put(type.getDisplayName() + " Queue", getQueueSize(type));
        }
        
        // Equipment usage
        for (String equipment : equipmentAvailability.keySet()) {
            int available = getAvailableEquipment(equipment);
            int total = equipmentAvailability.get(equipment);
            stats.put(equipment, available + "/" + total + " available");
        }
        
        return stats;
    }
    
    /**
     * Get detailed queue info
     */
    public String getQueueInfo(QueueRequest.QueueType queueType) {
        StringBuilder sb = new StringBuilder();
        GymQueue<QueueRequest> queue = queues.get(queueType);
        
        sb.append("========================================\n");
        sb.append(queueType.getDisplayName().toUpperCase()).append("\n");
        sb.append("========================================\n\n");
        sb.append("Queue Size: ").append(queue.size()).append("\n");
        sb.append("Average Wait: ").append(getAverageServiceTime(queueType)).append(" min\n\n");
        
        if (queue.isEmpty()) {
            sb.append("No one in queue.\n");
        } else {
            sb.append("Position | Member | Priority | Wait Time | Resource\n");
            sb.append("---------|--------|----------|-----------|----------\n");
            
            List<QueueRequest> requests = queue.toList();
            for (QueueRequest req : requests) {
                sb.append(String.format("   %2d    | %-15s | %-8s | %3d min   | %s\n",
                    req.getPositionInQueue(),
                    req.getMember().getFullName(),
                    req.getPriority().getName(),
                    req.getWaitingTimeMinutes(),
                    req.getResourceName()
                ));
            }
        }
        
        return sb.toString();
    }
}