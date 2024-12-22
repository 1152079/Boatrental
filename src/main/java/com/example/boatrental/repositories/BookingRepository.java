package com.example.boatrental.repositories;

import com.example.boatrental.models.entities.Booking;
import com.example.boatrental.models.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    Optional<Booking> findById(UUID id);

    List<Booking> findAllByUserId(UUID userId);

    List<Booking> findAllByBoatId(UUID boatId);

    boolean existsByUserId(UUID userId);

    boolean existsByBoatId(UUID boatId);

    List<Booking> findByStatus(BookingStatus status);

}
