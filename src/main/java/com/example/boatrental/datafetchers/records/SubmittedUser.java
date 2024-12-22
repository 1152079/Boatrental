package com.example.boatrental.datafetchers.records;

import com.example.boatrental.models.enums.Role;

import java.time.LocalDate;

public record SubmittedUser(String name, String email, String phone, String password, Role role, LocalDate registrationDate) {

}
