package com.example.demo.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(unique = true)
    private String phonenumber;

    @Lob
    private String address;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    public enum Gender {
        M, F, OTHER
    }

    public enum Role {
        USER, ADMIN, SELLER
    }

    @Builder
    public User(String email, String password, String name, LocalDate birthdate, String gender, String phonenumber, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender != null ? Gender.valueOf(gender.toUpperCase()) : null;
        this.role = Role.USER;
        this.phonenumber = phonenumber;
        this.address = address;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateDetails(String newPassword, String newAddress, String newPhonenumber) {
        if (newPassword != null && !newPassword.isBlank()) {
            this.password = newPassword;
        }
        this.address = newAddress != null ? newAddress : this.address;
        this.phonenumber = newPhonenumber != null ? newPhonenumber : this.phonenumber;
        this.updatedAt = LocalDateTime.now();
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }
}