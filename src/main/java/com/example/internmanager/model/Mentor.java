package com.example.internmanager.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;


@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

@Entity
@Table(name = "mentors")
public class Mentor extends User {
    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL)
    @JsonIdentityReference(alwaysAsId = true)
    private List<Intern> internList;
}
