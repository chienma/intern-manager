package com.example.internmanager.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

@Entity
@Table(name = "mentors")
public class Mentor extends User {
    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL)
    @JsonIdentityReference()
    private List<Intern> internList;
}
