package com.example.internmanager.controller;

import com.example.internmanager.model.Intern;
import com.example.internmanager.service.InternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/interns")
public class InternController {
    private final InternService internService;

    @Autowired
    public InternController(InternService internService) {
        this.internService = internService;
    }

    @GetMapping
    public List<Intern> getAllInterns() {
        return internService.getAll();
    }

    @GetMapping("/{intern-id}")
    public ResponseEntity<Intern> getInternById(@PathVariable("intern-id") Long id) {
        Intern intern = internService.getById(id);
        if (intern != null) {
            return ResponseEntity.ok(intern);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<Intern> searchInterns(@RequestParam("keyword") String keyword) {
        return internService.findByKeyword(keyword);
    }

    @PostMapping
    public ResponseEntity<?> addIntern(@RequestBody Intern intern) {
        try {
            Intern newIntern = internService.create(intern);
            return ResponseEntity.ok(newIntern);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{intern-id}")
    public ResponseEntity<?> updateIntern(@PathVariable("intern-id") Long internId, @RequestBody Intern intern) {
        try {
            Intern newIntern = internService.update(internId, intern);
            return ResponseEntity.ok(newIntern);
        } catch (IllegalArgumentException | DataIntegrityViolationException |
                 EmptyResultDataAccessException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping("/{intern-id}/mentor/{mentor-id}")
    public ResponseEntity<?> setMentorForIntern(@PathVariable("intern-id") Long internId, @PathVariable("mentor-id") Long mentorId) {
        try {
            Intern newIntern = internService.setMentorForIntern(internId, mentorId);
            return ResponseEntity.ok(newIntern);
        } catch (EmptyResultDataAccessException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("/{intern-id}")
    public ResponseEntity<Void> deleteIntern(@PathVariable("intern-id") Long internId) {
        boolean deleted = internService.deleteById(internId);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
