package com.example.internmanager.repository;

import com.example.internmanager.model.Intern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternRepository extends JpaRepository<Intern, Long> {
    // Search for mentors by keyword using JPQL
    @Query("SELECT i FROM Intern i WHERE i.name LIKE %:keyword% OR i.email LIKE %:keyword% OR i.phoneNumber LIKE %:keyword% OR i.position LIKE %:keyword%")
    List<Intern> findByKeyword(@Param("keyword") String keyword);

    Intern findInternByEmail(String email);

    boolean existsInternByEmail(String email);
}
