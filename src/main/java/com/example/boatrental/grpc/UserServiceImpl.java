package com.example.boatrental.grpc;

import com.example.boatrental.*;
import com.example.boatrental.models.entities.User;
import com.example.boatrental.repositories.UserRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service("userServiceImplGrpc")
public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void findUser(UserNameRequest request, StreamObserver<UserResponse> responseObserver) {
        List<User> users = userRepository.findByName(request.getName());
        if (!users.isEmpty()) {
            User user = users.get(0);

            UserResponse response = UserResponse.newBuilder()
                    .setName(user.getName() != null ? user.getName() : "null")
                    .setEmail(user.getEmail() != null ? user.getEmail() : "null")
                    .setPhone(user.getPhone() != null ? user.getPhone() : "null")
                    .setRole(user.getRole().name())
                    .build();

            responseObserver.onNext(response);
        } else {
            responseObserver.onError(Status.NOT_FOUND
                    .withDescription("Пользователь с именем " + request.getName() + " не найден")
                    .asRuntimeException());
        }
        responseObserver.onCompleted();
    }

    @Override
    public void getAllUsers(EmptyRequest request, StreamObserver<UserListResponse> responseObserver) {
        List<User> users = userRepository.findAll();

        List<UserResponse> userResponses = users.stream()
                .map(user -> UserResponse.newBuilder()
                        .setName(user.getName() != null ? user.getName() : "null")
                        .setEmail(user.getEmail() != null ? user.getEmail() : "null")
                        .setPhone(user.getPhone() != null ? user.getPhone() : "null")
                        .setRole(user.getRole().name())
                        .build())
                .collect(Collectors.toList());

        UserListResponse response = UserListResponse.newBuilder()
                .addAllUsers(userResponses)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}