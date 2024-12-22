package com.example.boatrental.dtos;

import com.example.boatrental.models.enums.BoatStatus;
import com.example.boatrental.models.enums.BoatType;

import java.time.LocalDate;
import java.util.UUID;

public class BoatDto {

    private String name;
    private String description;
    private BoatType type;
    private int capacity;
    private BoatStatus status;
    private double pricePerHour;
    private LocalDate createdAt;
    private LocalDate repairAt;

    public BoatDto(String name, String description, BoatType type, int capacity, BoatStatus status,
                   double pricePerHour, LocalDate createdAt, LocalDate repairAt) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.capacity = capacity;
        this.status = status;
        this.pricePerHour = pricePerHour;
        this.createdAt = createdAt;
        this.repairAt = repairAt;
    }

    public BoatDto() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BoatType getType() {
        return type;
    }

    public void setType(BoatType type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public BoatStatus getStatus() {
        return status;
    }

    public void setStatus(BoatStatus status) {
        this.status = status;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(double pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getRepairAt() {
        return repairAt;
    }

    public void setRepairAt(LocalDate repairAt) {
        this.repairAt = repairAt;
    }

    @Override
    public String toString() {
        return "BoatDto { name='" + name + '\'' +
                ", description='" + description + '\'' + ", type=" + type +
                ", capacity=" + capacity + ", status=" + status +
                ", pricePerHour=" + pricePerHour + ", createdAt=" + createdAt +
                ", repairAt=" + repairAt +
                " }";
    }
}
