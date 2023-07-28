package com.example.internmanager.repository;

import com.example.internmanager.model.Intern;
import org.springframework.stereotype.Repository;

@Repository
public interface InternRepository extends UserRepository<Intern> {
}
