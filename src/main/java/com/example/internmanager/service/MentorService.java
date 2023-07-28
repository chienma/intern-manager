package com.example.internmanager.service;

import com.example.internmanager.model.Mentor;
import com.example.internmanager.repository.MentorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// By inheriting UserService, it reuses existing logic
@Service
public class MentorService extends UserService<Mentor, MentorRepository> {
    private final MentorRepository mentorRepository;

    @Autowired
    public MentorService(MentorRepository mentorRepository) {
        this.mentorRepository = mentorRepository;
    }

    @Override
    protected MentorRepository getRepository() {
        return mentorRepository;
    }
}
