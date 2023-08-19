package com.example.internmanager.repository;

import com.example.internmanager.model.Intern;
import com.example.internmanager.model.Mentor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface  InternRepository extends UserRepository<Intern> {
    @Modifying
    @Query("UPDATE Intern i SET i.mentor = :mentor WHERE i.id = :id")
    void setMentorForIntern(@Param("id") Long id, @Param("mentor") Mentor mentor);
}
