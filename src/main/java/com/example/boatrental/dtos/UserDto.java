package com.example.boatrental.dtos;

import com.example.boatrental.models.enums.Role;
import java.time.LocalDate;

public class UserDto {

    private String name;
    private String email;
    private String phone;
    private String password;
    private Role role;
    private LocalDate registrationDate;

    public UserDto(String name, String email, String phone, String password, Role role, LocalDate registrationDate) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.registrationDate = registrationDate;
    }

    public UserDto() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public String toString() {
        return "UserDto { name=" + name + ", email=" + email +
                ", phone=" + phone + ", role=" + role + ", registrationDate=" + registrationDate + " }";
    }
}
