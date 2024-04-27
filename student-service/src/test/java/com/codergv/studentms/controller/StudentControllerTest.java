package com.codergv.studentms.controller;

import com.codergv.studentms.dto.StudentRequestDTO;
import com.codergv.studentms.dto.StudentResponseDTO;
import com.codergv.studentms.exception.NotFoundException;
import com.codergv.studentms.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    private StudentRequestDTO studentRequestDTO;
    private StudentResponseDTO studentResponseDTO;

    @BeforeEach
    void setUp() {
        studentRequestDTO = new StudentRequestDTO();
        studentRequestDTO.setName("John Doe");
        studentRequestDTO.setGrade("10");

        studentResponseDTO = new StudentResponseDTO();
        studentResponseDTO.setStudentId("1");
        studentResponseDTO.setName("John Doe");
        studentResponseDTO.setGrade("10");
    }

    @Test
    void testAddStudent() {
        when(studentService.addStudent(any(StudentRequestDTO.class))).thenReturn(studentResponseDTO);

        ResponseEntity<StudentResponseDTO> responseEntity = studentController.addStudent(studentRequestDTO);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(studentResponseDTO, responseEntity.getBody());
        verify(studentService, times(1)).addStudent(any(StudentRequestDTO.class));
    }

    @Test
    void testGetAllStudents() {
        when(studentService.getAllStudents()).thenReturn(Collections.singletonList(studentResponseDTO));

        List<StudentResponseDTO> students = studentController.getAllStudents();

        assertFalse(students.isEmpty());
        assertEquals(1, students.size());
        assertEquals(studentResponseDTO, students.get(0));
        verify(studentService, times(1)).getAllStudents();
    }

    @Test
    void testGetStudentById() {
        String studentId = "1";
        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(studentResponseDTO));

        ResponseEntity<StudentResponseDTO> responseEntity = studentController.getStudentById(studentId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(studentResponseDTO, responseEntity.getBody());
        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    void testGetStudentById_NotFound() {
        String studentId = "1";
        when(studentService.getStudentById(studentId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> studentController.getStudentById(studentId));

        assertEquals("Student not found with studentId: " + studentId, exception.getMessage());
        verify(studentService, times(1)).getStudentById(studentId);
    }

    @Test
    void testUpdateStudent() {
        String studentId = "1";
        when(studentService.getStudentById(studentId)).thenReturn(Optional.of(studentResponseDTO));
        when(studentService.updateStudent(eq(studentId), any(StudentRequestDTO.class))).thenReturn(studentResponseDTO);

        ResponseEntity<?> responseEntity = studentController.updateStudent(studentId, studentRequestDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(studentService, times(1)).updateStudent(eq(studentId), any(StudentRequestDTO.class));
    }

    @Test
    void testUpdateStudent_NotFound() {
        String studentId = "1";
        when(studentService.getStudentById(studentId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> studentController.updateStudent(studentId, studentRequestDTO));

        assertEquals("Student not found with studentId: " + studentId, exception.getMessage());
        verify(studentService, times(0)).updateStudent(anyString(), any(StudentRequestDTO.class));
    }

    @Test
    void testDeleteStudent() {
        Long studentId = 1L;
        assertDoesNotThrow(() -> studentController.deleteStudent(studentId));
        verify(studentService, times(1)).deleteStudent(studentId);
    }
}

