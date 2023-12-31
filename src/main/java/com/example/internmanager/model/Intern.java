package com.example.internmanager.model;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")

@Entity
@Table(name = "interns")
public class Intern extends User {
    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;
}
