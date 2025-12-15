package com.ibrahim.mehdi.gymmanager.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Member model class representing a gym member
 */
public class Member implements Serializable, Comparable<Member> {
    private static final long serialVersionUID = 1L;
    
    /**
     * Membership types with prices
     */
    public enum MembershipType {
        MONTHLY(1000, 1),      // 1000₺ per month
        QUARTERLY(2700, 3),    // 2700₺ for 3 months (10% discount)
        YEARLY(9600, 12),      // 9600₺ for 12 months (20% discount)
        VIP(15000, 12);        // 15000₺ for 12 months with extras
        
        private final int price;
        private final int durationMonths;
        
        MembershipType(int price, int durationMonths) {
            this.price = price;
            this.durationMonths = durationMonths;
        }
        
        public int getPrice() {
            return price;
        }
        
        public int getDurationMonths() {
            return durationMonths;
        }
        
        public String getPriceText() {
            return price + "₺";
        }
        
        public String getDescription() {
            return this.name() + " (" + durationMonths + " ay - " + price + "₺)";
        }
    }
    
    private int id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private MembershipType membershipType;
    private LocalDate registrationDate;
    private LocalDate membershipEndDate;
    private boolean active;
    
    public Member(int id, String name, String surname, String phoneNumber, 
                 String email, MembershipType membershipType) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.membershipType = membershipType;
        this.registrationDate = LocalDate.now();
        this.membershipEndDate = registrationDate.plusMonths(membershipType.getDurationMonths());
        this.active = true;
    }
    
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSurname() { return surname; }
    public void setSurname(String surname) { this.surname = surname; }
    
    public String getFullName() { return name + " " + surname; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public MembershipType getMembershipType() { return membershipType; }
    public void setMembershipType(MembershipType membershipType) { 
        this.membershipType = membershipType;
        // Update end date when type changes
        this.membershipEndDate = LocalDate.now().plusMonths(membershipType.getDurationMonths());
    }
    
    public int getMembershipPrice() {
        return membershipType.getPrice();
    }
    
    public LocalDate getRegistrationDate() { return registrationDate; }
    public void setRegistrationDate(LocalDate registrationDate) { 
        this.registrationDate = registrationDate; 
    }
    
    public LocalDate getMembershipEndDate() { return membershipEndDate; }
    public void setMembershipEndDate(LocalDate membershipEndDate) { 
        this.membershipEndDate = membershipEndDate; 
    }
    
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    
    public boolean isMembershipExpired() {
        return LocalDate.now().isAfter(membershipEndDate);
    }
    
    public long getDaysUntilExpiry() {
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), membershipEndDate);
    }
    
    @Override
    public int compareTo(Member other) {
        return Integer.compare(this.id, other.id);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id == member.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return String.format("Member[ID=%d, Name=%s %s, Type=%s, Price=%d₺, Active=%s]",
            id, name, surname, membershipType, membershipType.getPrice(), active);
    }
}