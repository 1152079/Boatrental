package com.example.boatrental.services.impl;

import com.example.boatrental.dtos.UserDto;
import com.example.boatrental.models.entities.User;
import com.example.boatrental.repositories.UserRepository;
import com.example.boatrental.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.boatrental.rabbitmq.senders.UserMessageSender;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserMessageSender userMessageSender;

    @Override
    public UserDto register(UserDto user) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            User u = modelMapper.map(user, User.class);
            User savedUser = userRepository.save(u);
            userMessageSender.sendUserMessage("User registered: " + savedUser.getEmail());
            return modelMapper.map(savedUser, UserDto.class);
        } else {
            userMessageSender.sendUserMessage("Registration failed: Email " + user.getEmail() + " already exists.");
            return null;
        }
    }

    @Override
    public Optional<UserDto> findUser(String email) {
        Optional<UserDto> userOptional = userRepository.findByEmail(email)
                .map(user -> modelMapper.map(user, UserDto.class));

        userOptional.ifPresentOrElse(
                user -> userMessageSender.sendUserMessage("User found: " + email),
                () -> userMessageSender.sendUserMessage("User not found: " + email)
        );
        return userOptional;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());

        userMessageSender.sendUserMessage("Fetched all users: " + users.size() + " users found.");
        return users;
    }

    @Override
    public Optional<UserDto> changeUserInfo(String email, UserDto updatedUser) {
        return userRepository.findByEmail(email).map(user -> {
            user.setName(updatedUser.getName());
            user.setEmail(updatedUser.getEmail());
            user.setPassword(updatedUser.getPassword());
            user.setPhone(updatedUser.getPhone());
            user.setRole(updatedUser.getRole());

            User savedUser = userRepository.save(user);
            userMessageSender.sendUserMessage("User info updated: " + savedUser.getEmail());
            return modelMapper.map(savedUser, UserDto.class);
        });
    }

    @Override
    public void delete(String email) {
        userRepository.findByEmail(email).ifPresentOrElse(
                user -> {
                    userRepository.delete(user);
                    userMessageSender.sendUserMessage("User deleted: " + email);
                },
                () -> userMessageSender.sendUserMessage("Delete failed: User not found with email " + email)
        );
    }

    @Override
    public Optional<UUID> getUserIdByEmail(String email) {
        Optional<UUID> userId = userRepository.findByEmail(email).map(User::getId);

        userId.ifPresentOrElse(
                id -> userMessageSender.sendUserMessage("User ID fetched for " + email + ": " + id),
                () -> userMessageSender.sendUserMessage("User ID fetch failed: User not found with email " + email)
        );
        return userId;
    }
}