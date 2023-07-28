package com.example.internmanager.service;

import com.example.internmanager.model.User;
import com.example.internmanager.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;

// Abstract UserService class centralizes common logic for Mentor, and Intern services
public abstract class UserService<T extends User, R extends UserRepository<T>> {
    // Repository used for the data query, eg: InternRepository, MentorRepository
    protected abstract R getRepository();

    public List<T> getAll() {
        return getRepository().findAll();
    }

    public T getById(Long id) {
        return getRepository().findById(id).orElse(null);
    }

    // Search for T-type Users with name, email, phone, position fields similar to keywords
    public List<T> findByKeyword(String keyword) {
        return getRepository().findByKeyword(keyword);
    }

    // Add a new User and make sure the email is unique
    public T create(T user) throws DataIntegrityViolationException {
        if (getRepository().existsUsersByEmail(user.getEmail())) {
            throw new DataIntegrityViolationException("User's email address already exists");
        }
        return getRepository().save(user);
    }

    // Update User and make sure path is correct, User already exists and email is not duplicate
    public T update(Long id, T newUser) throws IllegalArgumentException, DataIntegrityViolationException, EmptyResultDataAccessException {
        if (!id.equals(newUser.getId())) {
            throw new IllegalArgumentException("Mismatched IDs between path and user object");
        }

        if (!getRepository().existsById(id)) {
            throw new EmptyResultDataAccessException("There are no user with the given ID", 0);
        }

        try {
            return getRepository().save(newUser);
        } catch (DataIntegrityViolationException e) {
            if (!getRepository().findUsersByEmail(newUser.getEmail()).getId().equals(id)) {
                throw new DataIntegrityViolationException("User's new email already exists");
            }
        }
        return newUser;
    }

    public boolean deleteById(Long id) {
        try {
            getRepository().deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;   // When can't find by id
        }
    }
}
