package com.example.boatrental.datafetchers;

import com.example.boatrental.datafetchers.records.SubmittedBoat;
import com.example.boatrental.dtos.BoatDto;
import com.example.boatrental.dtos.UserDto;
import com.example.boatrental.services.BoatService;
import com.netflix.graphql.dgs.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;
import java.time.format.DateTimeFormatter;

@DgsComponent
public class BoatDataFetcher {

    private final BoatService boatService;
    private final ModelMapper modelMapper;

    @Autowired
    public BoatDataFetcher(BoatService boatService, ModelMapper modelMapper) {
        this.boatService = boatService;
        this.modelMapper = modelMapper;
    }

    @DgsQuery
    public List<BoatDto> allBoats() {
        return boatService.getAllBoats();
    }

    @DgsQuery
    public BoatDto findBoat(@InputArgument String name) {
        return boatService.findBoat(name).orElse(null);
    }

    @DgsMutation
    public BoatDto registerBoat(@InputArgument SubmittedBoat input) {
        BoatDto newBoat = new BoatDto();
        newBoat.setName(input.name());
        newBoat.setDescription(input.description());
        newBoat.setType(input.type());
        newBoat.setCapacity(input.capacity());
        newBoat.setStatus(input.status());
        newBoat.setPricePerHour(input.pricePerHour());
        newBoat.setCreatedAt(LocalDate.now());
        newBoat.setRepairAt(LocalDate.now());
        return boatService.register(newBoat);
    }

    @DgsMutation
    public BoatDto updateBoat(@InputArgument String name, @InputArgument SubmittedBoat input) {
        BoatDto updateBoat = modelMapper.map(boatService.findBoat(name), BoatDto.class);
        updateBoat.setName(input.name());
        updateBoat.setDescription(input.description());
        updateBoat.setType(input.type());
        updateBoat.setCapacity(input.capacity());
        updateBoat.setStatus(input.status());
        updateBoat.setPricePerHour(input.pricePerHour());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        updateBoat.setRepairAt(LocalDate.parse(input.repairAt(), formatter));
        return boatService.updateBoat(name, updateBoat).orElse(null);
    }

    @DgsMutation
    public String deleteBoat(@InputArgument String name) {
        boatService.deleteBoatbyName(name);
        return "Лодка с именем " + name + " была удалена";
    }
}
