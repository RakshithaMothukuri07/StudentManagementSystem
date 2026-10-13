
package com.example.student.service;

import com.example.student.Student;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final List<Student> students = new ArrayList<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    public Student addStudent(Student student) {
        student.setId(idGenerator.getAndIncrement());
        students.add(student);
        return student;
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }

    public Optional<Student> getStudentById(int id) {
        return students.stream()
                .filter(student -> student.getId() == id)
                .findFirst();
    }

    public Optional<Student> updateStudent(int id, Student updatedStudent) {
        return getStudentById(id).map(student -> {
            student.setName(updatedStudent.getName());
            student.setAge(updatedStudent.getAge());
            student.setEmail(updatedStudent.getEmail());
            student.setCourse(updatedStudent.getCourse());
            return student;
        });
    }

    public Optional<Student> patchStudent(int id, Map<String, Object> updates) {
        return getStudentById(id).map(student -> {
            if (updates.containsKey("name")) {
                student.setName((String) updates.get("name"));
            }
            if (updates.containsKey("age")) {
                Object age = updates.get("age");
                student.setAge(age instanceof Number
                        ? ((Number) age).intValue() : null);
            }
            if (updates.containsKey("email")) {
                student.setEmail((String) updates.get("email"));
            }
            if (updates.containsKey("course")) {
                student.setCourse((String) updates.get("course"));
            }
            return student;
        });
    }

    public boolean deleteStudent(int id) {
        return students.removeIf(student -> student.getId() == id);
    }

    public List<Student> getStudentsByCourse(String course) {
        return students.stream()
                .filter(student -> student.getCourse() != null
                        && student.getCourse().equalsIgnoreCase(course))
                .collect(Collectors.toList());
    }

    public List<Student> getStudentsSortedByName() {
        return students.stream()
                .sorted(Comparator.comparing(
                        Student::getName,
                        String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public long getStudentCount() {
        return students.stream().count();
    }
}
