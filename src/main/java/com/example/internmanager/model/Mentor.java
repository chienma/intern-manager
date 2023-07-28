package com.example.internmanager.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "mentors")
public class Mentor extends User {
    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL)
    private List<Intern> internList;
}
