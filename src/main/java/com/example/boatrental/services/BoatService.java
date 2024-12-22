package com.example.boatrental.services;

import com.example.boatrental.dtos.BoatDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BoatService {
    BoatDto register(BoatDto boat);
    Optional<BoatDto> findBoat(String name);
    List<BoatDto> getAllBoats();
    Optional<BoatDto> updateBoat(String id, BoatDto updatedBoat);
    void deleteBoat(String id);
    void deleteBoatbyName(String name);
    Optional<UUID> getBoatIdByName(String name);
}
