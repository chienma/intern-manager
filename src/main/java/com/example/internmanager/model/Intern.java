package com.example.internmanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "interns")
public class Intern {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "intern_id")
    private Long internId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "position")
    private String position;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;
}
