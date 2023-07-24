package com.example.internmanager.controller;

import com.example.internmanager.model.Intern;
import com.example.internmanager.service.InternService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Intern> getAllIntern() {
        return internService.findAll();
    }

    @GetMapping("/{intern-id}")
    public ResponseEntity<Intern> getInternById(@PathVariable("intern-id") Long id) {
        Intern intern = internService.findById(id);
        if(intern != null) {
            return ResponseEntity.ok(intern);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Intern addMentor(@RequestBody Intern intern) {
        return internService.save(intern);
    }

    @PostMapping("/{intern-id}")
    public ResponseEntity<Intern> updateMentor(@PathVariable("intern-id") Long internId, @RequestBody Intern intern) {
        if(internId != intern.getInternId()) {
            return ResponseEntity.badRequest().build();
        }
        Intern existingIntern = internService.findById(internId);
        if(existingIntern == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(internService.save(intern));
    }
}
