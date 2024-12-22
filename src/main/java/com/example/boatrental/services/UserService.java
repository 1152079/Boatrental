package com.example.boatrental.services;

import com.example.boatrental.dtos.UserDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserDto register(UserDto user);
    Optional<UserDto> findUser(String email);
    List<UserDto> getAllUsers();
    Optional<UserDto> changeUserInfo(String email, UserDto user);
    void delete(String email);
    Optional<UUID> getUserIdByEmail(String email);
}
