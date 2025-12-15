package com.ibrahim.mehdi.gymmanager.model;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Member class representing a gym member.
 * Implements Serializable for binary file storage.
 * 
 * @author ibrahim.mehdi
 */
public class Member implements Serializable, Comparable<Member> {
    private static final long serialVersionUID = 1L;
    
    private int id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private LocalDate membershipStartDate;
    private LocalDate membershipEndDate;
    private MembershipType membershipType;
    private boolean isActive;
    
    /**
     * Membership type enumeration
     */
    public enum MembershipType {
        MONTHLY, QUARTERLY, YEARLY, VIP
    }
    
    /**
     * Default constructor
     */
    public Member() {
        this.isActive = true;
    }
    
    /**
     * Parameterized constructor
     * 
     * @param id Member ID
     * @param name Member name
     * @param surname Member surname
     * @param phoneNumber Phone number
     * @param email Email address
     * @param membershipType Type of membership
     */
    public Member(int id, String name, String surname, String phoneNumber, 
                  String email, MembershipType membershipType) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.membershipType = membershipType;
        this.membershipStartDate = LocalDate.now();
        this.membershipEndDate = calculateEndDate(membershipType);
        this.isActive = true;
    }
    
    /**
     * Calculate membership end date based on type
     */
    private LocalDate calculateEndDate(MembershipType type) {
        LocalDate start = LocalDate.now();
        switch (type) {
            case MONTHLY:
                return start.plusMonths(1);
            case QUARTERLY:
                return start.plusMonths(3);
            case YEARLY:
                return start.plusYears(1);
            case VIP:
                return start.plusYears(2);
            default:
                return start.plusMonths(1);
        }
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public LocalDate getMembershipStartDate() { return membershipStartDate; }
    public void setMembershipStartDate(LocalDate membershipStartDate) { 
        this.membershipStartDate = membershipStartDate; 
    }
    
    public LocalDate getMembershipEndDate() { return membershipEndDate; }
    public void setMembershipEndDate(LocalDate membershipEndDate) { 
        this.membershipEndDate = membershipEndDate; 
    }
    
    public MembershipType getMembershipType() { return membershipType; }
    public void setMembershipType(MembershipType membershipType) { 
        this.membershipType = membershipType; 
    }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public String getFullName() {
        return name + " " + surname;
    }
    
    @Override
    public int compareTo(Member other) {
        return Integer.compare(this.id, other.id);
    }
    
    @Override
    public String toString() {
        return String.format("Member[ID=%d, Name=%s %s, Type=%s, Active=%b]",
                id, name, surname, membershipType, isActive);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member member = (Member) obj;
        return id == member.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
