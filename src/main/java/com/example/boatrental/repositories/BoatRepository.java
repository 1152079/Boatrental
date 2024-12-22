package com.example.boatrental.repositories;

import com.example.boatrental.models.entities.Boat;
import com.example.boatrental.models.enums.BoatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.boatrental.models.enums.BoatType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BoatRepository extends JpaRepository<Boat, String> {

    boolean existsByName(String name);

    Optional<Boat> findByName(String name);

    boolean existsByType(BoatType type);

    Optional<Boat> findById(UUID id);

    List<Boat> findByStatus(BoatStatus status);
}
