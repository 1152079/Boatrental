package com.example.boatrental.services.impl;

import com.example.boatrental.dtos.BoatDto;
import com.example.boatrental.models.entities.Boat;
import com.example.boatrental.repositories.BoatRepository;
import com.example.boatrental.services.BoatService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.boatrental.rabbitmq.senders.BoatMessageSender;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BoatServiceImpl implements BoatService {

    @Autowired
    private BoatRepository boatRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BoatMessageSender boatMessageSender;

    @Override
    public BoatDto register(BoatDto boat) {
        if (!boatRepository.existsByName(boat.getName())) {
            Boat b = modelMapper.map(boat, Boat.class);
            Boat savedBoat = boatRepository.save(b);

            boatMessageSender.sendBoatMessage("Boat " + boat.getName() + " registered, status: " + boat.getStatus());
            return modelMapper.map(savedBoat, BoatDto.class);
        } else {
            boatMessageSender.sendBoatMessage("Registration failed: Boat " + boat.getName() + " already exists.");
            return null;
        }
    }

    @Override
    public Optional<BoatDto> findBoat(String name) {
        Optional<BoatDto> boatOptional = boatRepository.findByName(name)
                .map(boat -> modelMapper.map(boat, BoatDto.class));

        boatOptional.ifPresentOrElse(
                boat -> boatMessageSender.sendBoatMessage("Boat found: " + name),
                () -> boatMessageSender.sendBoatMessage("Boat not found: " + name)
        );
        return boatOptional;
    }

    @Override
    public List<BoatDto> getAllBoats() {
        List<BoatDto> boats = boatRepository.findAll()
                .stream()
                .map(boat -> modelMapper.map(boat, BoatDto.class))
                .collect(Collectors.toList());

        boatMessageSender.sendBoatMessage("Fetched all boats: " + boats.size() + " boats found.");
        return boats;
    }

    @Override
    public Optional<BoatDto> updateBoat(String name, BoatDto updatedBoat) {
        return boatRepository.findByName(name).map(boat -> {
            boat.setName(updatedBoat.getName());
            boat.setDescription(updatedBoat.getDescription());
            boat.setType(updatedBoat.getType());
            boat.setCapacity(updatedBoat.getCapacity());
            boat.setStatus(updatedBoat.getStatus());
            boat.setPricePerHour(updatedBoat.getPricePerHour());
            boat.setRepairAt(updatedBoat.getRepairAt());

            Boat savedBoat = boatRepository.save(boat);

            boatMessageSender.sendBoatMessage("Boat " + updatedBoat.getName() + " status updated: " + updatedBoat.getStatus());
            return modelMapper.map(savedBoat, BoatDto.class);
        });
    }

    @Override
    public void deleteBoat(String id) {
        boatRepository.findById(id).ifPresentOrElse(
                boat -> {
                    boatRepository.delete(boat);
                    boatMessageSender.sendBoatMessage("Boat " + id + " deleted.");
                },
                () -> boatMessageSender.sendBoatMessage("Delete failed: Boat not found with ID " + id)
        );
    }

    @Override
    public void deleteBoatbyName(String name) {
        boatRepository.findByName(name).ifPresentOrElse(
                boat -> {
                    boatRepository.delete(boat);
                    boatMessageSender.sendBoatMessage("Boat " + name + " deleted.");
                },
                () -> boatMessageSender.sendBoatMessage("Delete failed: Boat not found with name " + name)
        );
    }

    @Override
    public Optional<UUID> getBoatIdByName(String name) {
        Optional<UUID> boatId = boatRepository.findByName(name).map(Boat::getId);

        boatId.ifPresentOrElse(
                id -> boatMessageSender.sendBoatMessage("Boat ID fetched for " + name + ": " + id),
                () -> boatMessageSender.sendBoatMessage("Boat ID fetch failed: Boat not found with name " + name)
        );
        return boatId;
    }
}
