package com.codergv.studentms.controller;

import com.codergv.studentms.dto.StudentRequestDTO;
import com.codergv.studentms.dto.StudentResponseDTO;
import com.codergv.studentms.exception.NotFoundException;
import com.codergv.studentms.service.StudentService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/student")
    public ResponseEntity<StudentResponseDTO> addStudent(@Valid @RequestBody StudentRequestDTO studentRequestDTO) {
        logger.info("Adding a new student: {}", studentRequestDTO);
        StudentResponseDTO addedStudentResponseDTO = studentService.addStudent(studentRequestDTO);
        logger.info("New student added: {}", addedStudentResponseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedStudentResponseDTO);
    }

    @GetMapping
    public List<StudentResponseDTO> getAllStudents() {
        logger.info("Fetching all students");
        List<StudentResponseDTO> allStudents = studentService.getAllStudents();
        logger.info("Fetched {} students", allStudents.size());
        return allStudents;
    }

    @GetMapping("/student/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable String id) {
        logger.info("Fetching student with ID: {}", id);
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> {
                    String errorMessage = "Student not found with studentId: " + id;
                    logger.error(errorMessage);
                    return new NotFoundException(errorMessage);
                });
    }

    @PutMapping("/student/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id, @RequestBody StudentRequestDTO studentRequestDTO) {
        logger.info("Updating student with ID: {}", id);
        return studentService.getStudentById(id)
                .map(existingStudentResponseDTO -> {
                    StudentResponseDTO savedStudentResponseDTO = studentService.updateStudent(id, studentRequestDTO);
                    logger.info("Student with ID {} updated successfully", id);
                    return ResponseEntity.ok(savedStudentResponseDTO);
                })
                .orElseThrow(() -> {
                    String errorMessage = "Student not found with studentId: " + id;
                    logger.error(errorMessage);
                    return new NotFoundException(errorMessage);
                });
    }

    @DeleteMapping("/student/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        logger.info("Deleting student with ID: {}", id);
        studentService.deleteStudent(id);
        logger.info("Student with ID {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }
}