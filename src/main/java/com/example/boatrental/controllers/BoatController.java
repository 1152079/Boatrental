package com.example.boatrental.controllers;

import com.example.boatrental.controllers.exception.NotFoundException;
import com.example.boatrental.dtos.BoatDto;
import com.example.boatrental.services.BoatService;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/boats")
public class BoatController {

    private final BoatService boatService;
    private final ModelMapper modelMapper;
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public BoatController(BoatService boatService, ModelMapper modelMapper) {
        this.boatService = boatService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<CustomResponse<BoatDto>>> getAllBoats() {
        List<BoatDto> boats = boatService.getAllBoats();
        List<CustomResponse<BoatDto>> response = boats.stream()
                .map(this::createBoatResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CustomResponse<BoatDto>> createBoat(@RequestBody BoatDto newBoatDto) {
        BoatDto savedBoat = boatService.register(newBoatDto);
        CustomResponse<BoatDto> response = createBoatResponse(savedBoat);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{name}")
    public ResponseEntity<CustomResponse<BoatDto>> getBoatByName(@PathVariable String name) {
        BoatDto boat = boatService.findBoat(name)
                .orElseThrow(() -> new NotFoundException(name));
        CustomResponse<BoatDto> response = createBoatResponse(boat);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{name}")
    public ResponseEntity<CustomResponse<BoatDto>> updateBoat(@PathVariable String name, @RequestBody BoatDto boatDto) {
        BoatDto updatedBoat = boatService.updateBoat(name, boatDto)
                .orElseThrow(() -> new NotFoundException(name));
        CustomResponse<BoatDto> response = createBoatResponse(updatedBoat);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Map<String, Object>> deleteBoat(@PathVariable String name) {
        boatService.deleteBoat(name);
        Map<String, Object> response = Map.of(
                "message", "Boat deleted successfully.",
                "links", List.of(
                        WebMvcLinkBuilder.linkTo(methodOn(BoatController.class).getAllBoats()).withRel("all-boats").getHref(),
                        WebMvcLinkBuilder.linkTo(methodOn(BoatController.class).createBoat(null)).withRel("create-new").getHref()
                )
        );

        return ResponseEntity.ok(response);
    }

    private CustomResponse<BoatDto> createBoatResponse(BoatDto boat) {
        EntityModel<BoatDto> resource = EntityModel.of(boat);
        addBoatLinks(resource, boat);
        addActions(resource, boat);

        return new CustomResponse<>(resource);
    }

    private void addBoatLinks(EntityModel<BoatDto> resource, BoatDto boat) {
        Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(BoatController.class).getBoatByName(boat.getName())).withSelfRel();
        Link allBoatsLink = WebMvcLinkBuilder.linkTo(methodOn(BoatController.class).getAllBoats()).withRel("all-boats");
        resource.add(selfLink, allBoatsLink);
    }

    private void addActions(EntityModel<BoatDto> resource, BoatDto boat) {
        Link updateLink = WebMvcLinkBuilder.linkTo(methodOn(BoatController.class).updateBoat(boat.getName(), boat)).withRel("update");
        Link deleteLink = WebMvcLinkBuilder.linkTo(methodOn(BoatController.class).deleteBoat(boat.getName())).withRel("delete");
        resource.add(updateLink, deleteLink);
    }
}
