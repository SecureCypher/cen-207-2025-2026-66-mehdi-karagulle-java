package com.ibrahim.mehdi.gymmanager.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Queue Request - Enhanced queue system
 */
public class QueueRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum QueueType {
        EQUIPMENT_WAIT("Equipment Waiting", "Wait for specific equipment"),
        TRAINER_SESSION("Trainer Session", "Personal trainer booking"),
        SHOWER_ROOM("Shower Room", "Waiting for shower"),
        LOCKER_ROOM("Locker Room", "Waiting for locker");
        
        private final String displayName;
        private final String description;
        
        QueueType(String displayName, String description) {
            this.displayName = displayName;
            this.description = description;
        }
        
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
    }
    
    public enum Priority {
        VIP(1, "VIP Member"),
        PREMIUM(2, "Premium Member"),
        STANDARD(3, "Standard Member"),
        TRIAL(4, "Trial Member");
        
        private final int level;
        private final String name;
        
        Priority(int level, String name) {
            this.level = level;
            this.name = name;
        }
        
        public int getLevel() { return level; }
        public String getName() { return name; }
    }
    
    private int requestId;
    private Member member;
    private QueueType queueType;
    private String resourceName;  // Equipment name, Trainer name, etc.
    private Priority priority;
    private LocalDateTime joinTime;
    private LocalDateTime estimatedWaitTime;
    private int positionInQueue;
    
    public QueueRequest(int requestId, Member member, QueueType queueType, 
                       String resourceName) {
        this.requestId = requestId;
        this.member = member;
        this.queueType = queueType;
        this.resourceName = resourceName;
        this.joinTime = LocalDateTime.now();
        
        // Set priority based on membership type
        switch (member.getMembershipType()) {
            case VIP:
                this.priority = Priority.VIP;
                break;
            case YEARLY:
                this.priority = Priority.PREMIUM;
                break;
            case QUARTERLY:
                this.priority = Priority.STANDARD;
                break;
            default:
                this.priority = Priority.STANDARD;
                break;
        }
    }
    
    public long getWaitingTimeMinutes() {
        return java.time.Duration.between(joinTime, LocalDateTime.now()).toMinutes();
    }
    
    public String getFormattedJoinTime() {
        return joinTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
    
    public String getFormattedEstimatedWait() {
        if (estimatedWaitTime == null) return "Calculating...";
        long minutes = java.time.Duration.between(LocalDateTime.now(), estimatedWaitTime).toMinutes();
        return minutes + " min";
    }
    
    // Getters and Setters
    public int getRequestId() { return requestId; }
    public Member getMember() { return member; }
    public QueueType getQueueType() { return queueType; }
    public String getResourceName() { return resourceName; }
    public Priority getPriority() { return priority; }
    public LocalDateTime getJoinTime() { return joinTime; }
    public LocalDateTime getEstimatedWaitTime() { return estimatedWaitTime; }
    public void setEstimatedWaitTime(LocalDateTime time) { this.estimatedWaitTime = time; }
    public int getPositionInQueue() { return positionInQueue; }
    public void setPositionInQueue(int pos) { this.positionInQueue = pos; }
    
    @Override
    public String toString() {
        return String.format("[%d] %s - %s (%s) - Waiting: %d min - Position: %d",
            requestId, member.getFullName(), queueType.getDisplayName(),
            resourceName, getWaitingTimeMinutes(), positionInQueue);
    }
}