package com.example.boatrental.datafetchers;

import com.example.boatrental.datafetchers.records.SubmittedUser;
import com.example.boatrental.dtos.UserDto;
import com.example.boatrental.services.UserService;
import com.netflix.graphql.dgs.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.List;

@DgsComponent
public class UserDataFetcher {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserDataFetcher(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @DgsQuery
    public List<UserDto> allUsers() {
        return userService.getAllUsers();
    }

    @DgsQuery
    public UserDto findUser(@InputArgument String email) {
        return userService.findUser(email).orElse(null);
    }

    @DgsMutation
    public UserDto registerUser(@InputArgument SubmittedUser input) {
        UserDto newUser = new UserDto();
        newUser.setName(input.name());
        newUser.setEmail(input.email());
        newUser.setPhone(input.phone());
        newUser.setPassword(input.password());
        newUser.setRole(input.role());
        newUser.setRegistrationDate(LocalDate.now());
        return userService.register(newUser);
    }

    @DgsMutation
    public UserDto updateUser(@InputArgument String email, @InputArgument SubmittedUser input) {
        UserDto updateUser = modelMapper.map(userService.findUser(email), UserDto.class);
        updateUser.setName(input.name());
        updateUser.setEmail(input.email());
        updateUser.setPhone(input.phone());
        updateUser.setPassword(input.password());
        updateUser.setRole(input.role());
            return userService.changeUserInfo(email, updateUser).orElse(null);
    }

    @DgsMutation
    public String deleteUser(@InputArgument String email) {
        userService.delete(email);
        return "Пользователь с почтой " + email + " был удален";
    }
}
