package com.example.boatrental.models.entities;

import java.util.UUID;

import jakarta.persistence.*;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    protected UUID id;
    public UUID getId() {
        return id;
    }
    protected void setId(UUID id) {
        this.id = id;
    }
}
