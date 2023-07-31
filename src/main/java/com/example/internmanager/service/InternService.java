package com.example.internmanager.service;

import com.example.internmanager.model.Intern;
import com.example.internmanager.model.Mentor;
import com.example.internmanager.repository.InternRepository;
import com.example.internmanager.repository.MentorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Transactional
// By inheriting UserService, it reuses existing logic
@Service
public class InternService extends UserService<Intern, InternRepository> {
    private final InternRepository internRepository;
    private final MentorRepository mentorRepository;

    @Autowired
    public InternService(InternRepository internRepository, MentorRepository mentorRepository) {
        this.internRepository = internRepository;
        this.mentorRepository = mentorRepository;
    }

    @Override
    protected InternRepository getRepository() {
        return internRepository;
    }

    public Intern setMentorForIntern(Long internId, Long mentorId) throws EmptyResultDataAccessException {
        Mentor mentor = mentorRepository.findById(mentorId).orElse(null);
        if (mentor == null) {
            throw new EmptyResultDataAccessException("No mentor with provided id", 0);
        }

        Intern intern = internRepository.findById(internId).orElse(null);
        if (intern == null) {
            throw new EmptyResultDataAccessException("No intern with provided id", 0);
        }
        internRepository.setMentorForIntern(internId, mentor);
        return internRepository.findById(internId).orElse(null);
    }
}

