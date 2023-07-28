package com.example.internmanager.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "interns")
public class Intern extends User {
    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;
}
