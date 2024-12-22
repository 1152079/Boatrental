package com.example.boatrental.models.entities;

import com.example.boatrental.models.enums.BoatStatus;
import com.example.boatrental.models.enums.BoatType;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "boats")
public class Boat extends BaseEntity {

    private String name;
    private String description;
    private BoatType type;
    private int capacity;
    private BoatStatus status;
    @Column(name = "price", nullable = false)
    private double pricePerHour;
    @Column(name = "register", nullable = false)
    private LocalDate createdAt;
    @Column(name = "repair_date", nullable = false)
    private LocalDate repairAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "boat")
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REMOVE})
    protected Set<Booking> bookings;

    public Boat(String name, String description, BoatType type, int capacity, BoatStatus status,
                double pricePerHour, LocalDate createdAt, LocalDate repairAt) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.capacity = capacity;
        this.status = status;
        this.pricePerHour = pricePerHour;
        this.createdAt = createdAt;
        this.repairAt = repairAt;
        bookings = new HashSet<Booking>();
    }

    protected Boat() {
    }

    @Column(name = "name", length = 100, nullable = false, unique = true)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    public BoatType getType() {
        return type;
    }

    public void setType(BoatType type) {
        this.type = type;
    }

    @Column(name = "capacity", nullable = false)
    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    public BoatStatus getStatus() {
        return status;
    }

    public void setStatus(BoatStatus status) {
        this.status = status;
    }

    @Column(name = "price_per_hour", nullable = false)
    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    @Column(name = "created_at", nullable = false)
    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Column(name = "repair_at")
    public LocalDate getRepairAt() {
        return repairAt;
    }

    public void setRepairAt(LocalDate repairAt) {
        this.repairAt = repairAt;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public String toString() {
        return "Boat { id=" + getId() + ", name=" + name + ", description=" + description +
                ", type=" + type + ", capacity=" + capacity + ", status=" + status +
                ", pricePerHour=" + pricePerHour + ", createdAt=" + createdAt + ", repairAt=" + repairAt +
                " }";
    }
}
