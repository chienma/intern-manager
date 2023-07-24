package com.example.internmanager.service;

import com.example.internmanager.model.Intern;
import com.example.internmanager.repository.InternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InternService {
    private final InternRepository internRepository;

    @Autowired
    public InternService(InternRepository internRepository) {
        this.internRepository = internRepository;
    }

    public List<Intern> findAll() {
        return internRepository.findAll();
    }

    public Intern findById(Long id) {
        return internRepository.findById(id).orElse(null);
    }
    public List<Intern> findByKeyword(String keyword) {
        return internRepository.findByKeyword(keyword);
    }
    public Intern save(Intern intern) {
        return internRepository.save(intern);
    }

    public void deleteById(Long id) {
        internRepository.deleteById(id);
    }

}

