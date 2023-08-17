package com.example.internmanager.repository;

import com.example.internmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {
    @Query("SELECT u FROM #{#entityName} u WHERE u.fullName LIKE %:keyword% OR u.email LIKE %:keyword% OR u.phoneNumber LIKE %:keyword% OR u.position LIKE %:keyword%")
    List<T> findByKeyword(@Param("keyword") String keyword);

    @Query("SELECT u FROM User u WHERE u.email LIKE :email")
    T findUsersByEmail(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.email = :email")
    boolean existsUsersByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.username LIKE :username")
    Optional<T> findUsersByUsername(@Param("username") String username);
}
