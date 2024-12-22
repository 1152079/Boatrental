package com.example.boatrental.models.entities;

import com.example.boatrental.models.enums.Role;
import jakarta.persistence.*;
import org.hibernate.annotations.Cascade;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Column(name = "full_name", length = 100, nullable = false)
    private String name;
    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;
    @Column(name = "phone", length = 50)
    private String phone;
    @Column(name = "password", length = 25, nullable = false)
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;
    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Cascade({org.hibernate.annotations.CascadeType.MERGE, org.hibernate.annotations.CascadeType.REMOVE})
    protected Set<Booking> bookings;

    public User(String name, String email, String phone, String password, Role role, LocalDate registrationDate) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
        this.registrationDate = registrationDate;
        bookings = new HashSet<Booking>();

    }
    protected User() {
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    @Enumerated(EnumType.STRING)
    public Role getRole() {
        return role;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    @Override
    public String toString() {
        return "User { id=" + getId() + ", fullName=" + name + ", email=" + email +
                ", phone=" + phone + ", role=" + role + ", registrationDate=" + registrationDate + " }";
    }
}
