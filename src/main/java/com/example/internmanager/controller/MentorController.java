package com.example.internmanager.controller;

import com.example.internmanager.model.Intern;
import com.example.internmanager.model.Mentor;
import com.example.internmanager.service.MentorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mentors")
public class MentorController {
    private final MentorService mentorService;

    @Autowired
    public MentorController(MentorService mentorService) {
        this.mentorService = mentorService;
    }


    @GetMapping
    public List<Mentor> getAllMentors() {
        return mentorService.getAll();
    }

    @GetMapping("/{mentor-id}")
    public ResponseEntity<Mentor> getMentorById(@PathVariable("mentor-id") Long id) {
        Mentor mentor = mentorService.getById(id);
        if (mentor != null) {
            return ResponseEntity.ok(mentor);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<Mentor> searchMentors(@RequestParam("keyword") String keyword) {
        return mentorService.findByKeyword(keyword);
    }

    @GetMapping("/{mentor-id}/interns")
    public ResponseEntity<?> getInternsOfMentor(@PathVariable("mentor-id") Long mentorId) {
        Mentor mentor = mentorService.getById(mentorId);
        List<Intern> internList = mentor.getInternList();
        if(internList.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(internList);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addMentor(@RequestBody Mentor mentor) {
        try {
            Mentor newMentor = mentorService.create(mentor);
            return ResponseEntity.ok(newMentor);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{mentor-id}")
    public ResponseEntity<?> updateMentor(@PathVariable("mentor-id") Long mentorId, @RequestBody Mentor mentor) {
        try {
            Mentor newMentor = mentorService.update(mentorId, mentor);
            return ResponseEntity.ok(newMentor);
        } catch (IllegalArgumentException | DataIntegrityViolationException |
                 EmptyResultDataAccessException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PutMapping("/{mentor-id}/assign-intern/{intern-id}")
    public ResponseEntity<?> setMentorForIntern(@PathVariable("intern-id") Long internId, @PathVariable("mentor-id") Long mentorId) {
        try {
            Intern newIntern = mentorService.setMentorForIntern(internId, mentorId);
            return ResponseEntity.ok(newIntern);
        } catch (EmptyResultDataAccessException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @DeleteMapping("/{mentor-id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteMentor(@PathVariable("mentor-id") Long mentorId) {
        boolean deleted = mentorService.deleteById(mentorId);
        if (deleted) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
