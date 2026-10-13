package com.example.student.controller;

import com.example.student.Student;
import com.example.student.service.StudentService;
import com.example.student.exception.StudentNotFoundException;
import jakarta.validation.Validator;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService service;
    private final Validator validator;
    public StudentController(StudentService service, Validator validator) {
        this.service = service;
        this.validator = validator;
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(
            @Valid @RequestBody Student student) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.addStudent(student));
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return service.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        Student student = service.getStudentById(id)
            .orElseThrow(() ->
                new StudentNotFoundException(
                    "Student not found with id: " + id));

        return ResponseEntity.ok(student);
    }

   
    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(
            @PathVariable int id,
            @Valid @RequestBody Student updatedStudent) {

        Student student = service.updateStudent(id, updatedStudent)
                .orElseThrow(() ->
                    new StudentNotFoundException(
                        "Student not found with id: " + id));

        return ResponseEntity.ok(student);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Student> patchStudent(
            @PathVariable int id,
            @RequestBody Map<String, Object> updates) {

        Student existing = service.getStudentById(id)
                .orElseThrow(() ->
                    new StudentNotFoundException(
                        "Student not found with id: " + id));

        // Create a separate copy so the stored student isn't changed yet
        Student candidate = new Student(
                existing.getId(),
                existing.getName(),
                existing.getAge(),
                existing.getEmail(),
                existing.getCourse()
        );

        if (updates.containsKey("name")) {
            candidate.setName((String) updates.get("name"));
        }
        if (updates.containsKey("age")) {
            Object age = updates.get("age");
            candidate.setAge(age instanceof Number
                    ? ((Number) age).intValue() : null);
        }
        if (updates.containsKey("email")) {
            candidate.setEmail((String) updates.get("email"));
        }
        if (updates.containsKey("course")) {
            candidate.setCourse((String) updates.get("course"));
        }

        var errors = validator.validate(candidate);
        if (!errors.isEmpty()) {
            throw new jakarta.validation.ConstraintViolationException(errors);
        }

        service.updateStudent(id, candidate);
        return ResponseEntity.ok(candidate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable int id) {

        if (!service.deleteStudent(id)) {
            throw new StudentNotFoundException(
                    "Student not found with id: " + id);
        }

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/course/{course}")
    public List<Student> getStudentsByCourse(
            @PathVariable String course) {
        return service.getStudentsByCourse(course);
    }

    @GetMapping("/sorted-by-name")
    public List<Student> getStudentsSortedByName() {
        return service.getStudentsSortedByName();
    }

    @GetMapping("/count")
    public Map<String, Long> getStudentCount() {
        return Map.of("totalStudents", service.getStudentCount());
    }
}
