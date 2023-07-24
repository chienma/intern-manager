package com.example.internmanager.service;

import com.example.internmanager.model.Intern;
import com.example.internmanager.repository.InternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternService {
    private final InternRepository internRepository;

    @Autowired
    public InternService(InternRepository internRepository) {
        this.internRepository = internRepository;
    }

    public List<Intern> getAllInterns() {
        return internRepository.findAll();
    }

    public Intern getInternById(Long id) {
        return internRepository.findById(id).orElse(null);
    }

    public List<Intern> findInternsByKeyword(String keyword) {
        return internRepository.findByKeyword(keyword);
    }

    //
    public Intern createIntern(Intern intern) throws DataIntegrityViolationException {
        if (internRepository.existsInternByEmail(intern.getEmail())) {
            throw new DataIntegrityViolationException("Intern's email address already exists");
        }
        return internRepository.save(intern);
    }

    public Intern updateIntern(Long internId, Intern newIntern) throws IllegalArgumentException, DataIntegrityViolationException, EmptyResultDataAccessException {
        if (!internId.equals(newIntern.getInternId())) {
            throw new IllegalArgumentException("Mismatched IDs between path and mentor object");
        }

        if (!internRepository.existsById(internId)) {
            throw new EmptyResultDataAccessException("There are no interns with the given ID", 0);
        }

        try {
            return internRepository.save(newIntern);
        } catch (DataIntegrityViolationException e) {
            if (!internRepository.findInternByEmail(newIntern.getEmail()).getInternId().equals(internId)) {
                throw new DataIntegrityViolationException("Intern's new email already exists");
            }
        }
        return newIntern;
    }

    public boolean deleteInternById(Long id) {
        try {
            internRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;   // When can't find by id
        }
    }

}

