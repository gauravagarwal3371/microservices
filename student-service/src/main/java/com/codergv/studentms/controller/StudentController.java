package com.codergv.studentms.controller;

import com.codergv.studentms.dto.StudentRequestDTO;
import com.codergv.studentms.dto.StudentResponseDTO;
import com.codergv.studentms.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<StudentResponseDTO> addStudent(@Valid @RequestBody StudentRequestDTO studentRequestDTO) {
        StudentResponseDTO addedStudentResponseDTO = studentService.addStudent(studentRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedStudentResponseDTO);
    }

    @GetMapping
    public List<StudentResponseDTO> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable String id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(@PathVariable String id, @RequestBody StudentRequestDTO studentRequestDTO) {
        return studentService.getStudentById(id)
                .map(existingStudentResponseDTO -> {
                    StudentResponseDTO savedStudentResponseDTO = studentService.updateStudent(id, studentRequestDTO);
                    return ResponseEntity.ok(savedStudentResponseDTO);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }
}