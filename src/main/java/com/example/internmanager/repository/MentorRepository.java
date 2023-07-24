package com.example.internmanager.repository;

import com.example.internmanager.model.Mentor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MentorRepository extends JpaRepository<Mentor, Long> {
    // Search for interns by keyword using JPQL
    @Query("SELECT m FROM Mentor m WHERE m.name LIKE %:keyword% OR m.email LIKE %:keyword% OR m.phoneNumber LIKE %:keyword% OR m.position LIKE %:keyword%")
    List<Mentor> findByKeyword(@Param("keyword") String keyword);
}
