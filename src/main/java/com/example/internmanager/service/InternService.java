package com.example.internmanager.service;

import com.example.internmanager.model.Intern;
import com.example.internmanager.repository.InternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// By inheriting UserService, it reuses existing logic
@Service
public class InternService extends UserService<Intern, InternRepository> {
    private final InternRepository internRepository;

    @Autowired
    public InternService(InternRepository internRepository) {
        this.internRepository = internRepository;
    }

    @Override
    protected InternRepository getRepository() {
        return internRepository;
    }
}

