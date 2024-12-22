package com.example.boatrental.controllers;

import com.example.boatrental.controllers.exception.NotFoundException;
import com.example.boatrental.dtos.UserDto;
import com.example.boatrental.services.UserService;
import org.modelmapper.ModelMapper;
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
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<CustomResponse<UserDto>>> all() {
        List<UserDto> users = userService.getAllUsers();
        List<CustomResponse<UserDto>> response = users.stream()
                .map(this::createUserResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<CustomResponse<UserDto>> newUser(@RequestBody UserDto newUserDto) {
        UserDto newUser = modelMapper.map(newUserDto, UserDto.class);
        UserDto user = userService.register(newUser);
        CustomResponse<UserDto> response = createUserResponse(user);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{email}")
    public ResponseEntity<CustomResponse<UserDto>> findUser(@PathVariable String email) {
        UserDto user = userService.findUser(email).orElseThrow(() -> new NotFoundException(email));
        CustomResponse<UserDto> response = createUserResponse(user);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{email}")
    public ResponseEntity<CustomResponse<UserDto>> updateUser(@PathVariable String email, @RequestBody UserDto userDto) {
        UserDto user = modelMapper.map(userDto, UserDto.class);
        userService.changeUserInfo(email, user);
        UserDto updatedUser = userService.findUser(email).orElseThrow(() -> new NotFoundException(email));
        CustomResponse<UserDto> response = createUserResponse(updatedUser);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable String email) {
        userService.delete(email);
        Map<String, Object> response = Map.of(
                "message", "User deleted successfully.",
                "_links", List.of(
                        WebMvcLinkBuilder.linkTo(methodOn(UserController.class).all()).withRel("all-users").getHref(),
                        WebMvcLinkBuilder.linkTo(methodOn(UserController.class).newUser(null)).withRel("create-new").getHref()
                )
        );

        return ResponseEntity.ok(response);
    }

    private CustomResponse<UserDto> createUserResponse(UserDto user) {
        EntityModel<UserDto> resource = EntityModel.of(user);
        addUserLinks(resource, user);
        addActions(resource, user);
        addAllUsersLink(resource);

        return new CustomResponse<>(resource);
    }

    private void addUserLinks(EntityModel<UserDto> resource, UserDto user) {
        Link selfLink = WebMvcLinkBuilder.linkTo(methodOn(UserController.class).findUser(user.getEmail())).withSelfRel();
        resource.add(selfLink);
    }

    private void addActions(EntityModel<UserDto> resource, UserDto user) {
        Link updateLink = WebMvcLinkBuilder.linkTo(methodOn(UserController.class).updateUser(user.getEmail(), user)).withRel("update");
        Link deleteLink = WebMvcLinkBuilder.linkTo(methodOn(UserController.class).deleteUser(user.getEmail())).withRel("delete");
        resource.add(updateLink);
        resource.add(deleteLink);
    }

    private void addAllUsersLink(EntityModel<UserDto> resource) {
        Link allUsersLink = WebMvcLinkBuilder.linkTo(methodOn(UserController.class).all()).withRel("all-users");
        resource.add(allUsersLink);
    }
}
