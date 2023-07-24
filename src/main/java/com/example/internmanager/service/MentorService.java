package com.example.internmanager.service;

import com.example.internmanager.model.Mentor;
import com.example.internmanager.repository.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<Mentor> findAll() {
        return mentorRepository.findAll();
    }

    public Mentor findById(Long id) {
        return mentorRepository.findById(id).orElse(null);
    }

    public List<Mentor> findByKeyword(String keyword) {
        return mentorRepository.findByKeyword(keyword);
    }

    public Mentor save(Mentor mentor) {
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
