package com.example.boatrental.grpc;

import com.example.boatrental.*;
import com.example.boatrental.models.entities.Boat;
import com.example.boatrental.repositories.BoatRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("boatServiceImplGrpc")
public class BoatServiceImpl extends BoatServiceGrpc.BoatServiceImplBase {

    private final BoatRepository boatRepository;

    public BoatServiceImpl(BoatRepository boatRepository) {
        this.boatRepository = boatRepository;
    }

    @Override
    public void findBoat(BoatNameRequest request, StreamObserver<BoatResponse> responseObserver) {
        Optional<Boat> boatOptional = boatRepository.findByName(request.getName());
        if (boatOptional.isPresent()) {
            Boat boat = boatOptional.get();

            BoatResponse response = BoatResponse.newBuilder()
                    .setName(boat.getName() != null ? boat.getName() : "null")
                    .setDescription(boat.getDescription() != null ? boat.getDescription() : "null")
                    .setType(boat.getType().name())
                    .setCapacity(boat.getCapacity())
                    .setStatus(boat.getStatus().name())
                    .setPricePerHour(boat.getPricePerHour())
                    .build();

            responseObserver.onNext(response);
        } else {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Лодка с именем " + request.getName() + " не найдена")
                    .asRuntimeException());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getAllBoats(EmptyRequest request, StreamObserver<BoatListResponse> responseObserver) {
        List<Boat> boats = boatRepository.findAll();

        List<BoatResponse> boatResponses = boats.stream()
                .map(boat -> BoatResponse.newBuilder()
                        .setName(boat.getName() != null ? boat.getName() : "null")
                        .setDescription(boat.getDescription() != null ? boat.getDescription() : "null")
                        .setType(boat.getType().name())
                        .setCapacity(boat.getCapacity())
                        .setStatus(boat.getStatus().name())
                        .setPricePerHour(boat.getPricePerHour())
                        .build())
                .collect(Collectors.toList());

        BoatListResponse response = BoatListResponse.newBuilder()
                .addAllBoats(boatResponses)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}