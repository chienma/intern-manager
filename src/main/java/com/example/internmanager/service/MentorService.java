package com.example.internmanager.service;

import com.example.internmanager.model.Mentor;
import com.example.internmanager.repository.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MentorService {
    private final MentorRepository mentorRepository;

    @Autowired
    public MentorService(MentorRepository mentorRepository) {
        this.mentorRepository = mentorRepository;
    }

    public List<Mentor> getAllMentors() {
        return mentorRepository.findAll();
    }

    public Mentor getMentorById(Long id) {
        return mentorRepository.findById(id).orElse(null);
    }

    public List<Mentor> findMentorsByKeyword(String keyword) {
        return mentorRepository.findByKeyword(keyword);
    }

    public Mentor createMentor(Mentor mentor) throws DataIntegrityViolationException {
        if (mentorRepository.existsMentorByEmail(mentor.getEmail())) {
            throw new DataIntegrityViolationException("Mentor's email address already exists");
        }
        return mentorRepository.save(mentor);
    }

    public Mentor updateMentor(Mentor mentor) {
        return mentorRepository.save(mentor);
    }

    public boolean deleteById(Long id) {
        try {
            mentorRepository.deleteById(id);
            return true;
        } catch (EmptyResultDataAccessException e) {
            return false;   // When can't find by id
        }
    }

}
