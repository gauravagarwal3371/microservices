package com.codergv.studentms.service;

import com.codergv.studentms.domain.SchoolDomain;
import com.codergv.studentms.domain.StudentDomain;
import com.codergv.studentms.dto.SchoolDTO;
import com.codergv.studentms.dto.StudentRequestDTO;
import com.codergv.studentms.dto.StudentResponseDTO;
import com.codergv.studentms.entity.SchoolDAO;
import com.codergv.studentms.entity.StudentDAO;
import com.codergv.studentms.exception.StudentPersistenceException;
import com.codergv.studentms.mapper.StudentDomainAndDaoMapper;
import com.codergv.studentms.mapper.StudentDtoAndDomainMapper;
import com.codergv.studentms.repository.SchoolRepository;
import com.codergv.studentms.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentDtoAndDomainMapper studentDtoAndDomainMapper;

    @Mock
    private StudentDomainAndDaoMapper studentDomainAndDaoMapper;

    @Mock
    private SchoolRepository schoolRepository;

    @InjectMocks
    private StudentService studentService;

    private StudentRequestDTO studentRequestDTO;
    private StudentDAO studentDAO;
    private StudentDomain studentDomain;
    private StudentResponseDTO studentResponseDTO;

    private SchoolDAO schoolDAO;
    private SchoolDTO schoolDTO;
    private SchoolDomain schoolDomain;

    @BeforeEach
    void setUp() {
        studentRequestDTO = new StudentRequestDTO();
        studentRequestDTO.setName("John Doe");
        studentRequestDTO.setGrade("10");
        schoolDTO = new SchoolDTO();
        schoolDTO.setName("test school");
        studentRequestDTO.setSchool(schoolDTO);

        studentDAO = new StudentDAO();
        studentDAO.setId(1L);
        studentDAO.setName("John Doe");
        studentDAO.setGrade("10");
        schoolDAO = new SchoolDAO();
        schoolDAO.setName("test school");
        studentDAO.setSchool(schoolDAO);

        studentDomain = new StudentDomain();
        studentDomain.setStudentId("1");
        studentDomain.setName("John Doe");
        studentDomain.setGrade("10");
        schoolDomain = new SchoolDomain();
        schoolDomain.setName("test school");
        studentDomain.setSchool(schoolDomain);

        studentResponseDTO = new StudentResponseDTO();
        studentResponseDTO.setStudentId("1");
        studentResponseDTO.setName("John Doe");
        studentResponseDTO.setGrade("10");
        studentResponseDTO.setSchool(schoolDTO);
    }

    @Test
    void testGetAllStudents() {
        when(studentRepository.findAll()).thenReturn(Collections.singletonList(studentDAO));
        when(studentDomainAndDaoMapper.toDomain(Collections.singletonList(studentDAO))).thenReturn(Collections.singletonList(studentDomain));
        when(studentDtoAndDomainMapper.toDTO(Collections.singletonList(studentDomain))).thenReturn(Collections.singletonList(studentResponseDTO));

        List<StudentResponseDTO> students = studentService.getAllStudents();

        assertFalse(students.isEmpty());
        assertEquals(1, students.size());
        assertEquals(studentResponseDTO, students.get(0));
        verify(studentRepository, times(1)).findAll();
        verify(studentDomainAndDaoMapper, times(1)).toDomain(Collections.singletonList(studentDAO));
        verify(studentDtoAndDomainMapper, times(1)).toDTO(Collections.singletonList(studentDomain));
    }

    @Test
    void testGetStudentById() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(studentDAO));
        when(studentDomainAndDaoMapper.toDomain(studentDAO)).thenReturn(studentDomain);
        when(studentDtoAndDomainMapper.toDTO(studentDomain)).thenReturn(studentResponseDTO);

        Optional<StudentResponseDTO> student = studentService.getStudentById("1");

        assertTrue(student.isPresent());
        assertEquals(studentResponseDTO, student.get());
        verify(studentRepository, times(1)).findById(anyLong());
        verify(studentDomainAndDaoMapper, times(1)).toDomain(studentDAO);
        verify(studentDtoAndDomainMapper, times(1)).toDTO(studentDomain);
    }

    @Test
    void testGetStudentById_NotFound() {
        when(studentRepository.findById(anyLong())).thenReturn(Optional.empty());

        Optional<StudentResponseDTO> student = studentService.getStudentById("1");

        assertTrue(student.isEmpty());
        verify(studentRepository, times(1)).findById(anyLong());
        verify(studentDomainAndDaoMapper, never()).toDomain(any(StudentDAO.class));
        verify(studentDtoAndDomainMapper, never()).toDTO(any(StudentDomain.class));
    }

    @Test
    void testAddStudent() {
        when(studentDtoAndDomainMapper.toDomain(studentRequestDTO)).thenReturn(studentDomain);
        when(studentDomainAndDaoMapper.toEntity(studentDomain)).thenReturn(studentDAO);
        when(schoolRepository.findByName(anyString())).thenReturn(schoolDAO);
        when(studentRepository.save(studentDAO)).thenReturn(studentDAO);
        when(studentDomainAndDaoMapper.toDomain(studentDAO)).thenReturn(studentDomain);
        when(studentDtoAndDomainMapper.toDTO(studentDomain)).thenReturn(studentResponseDTO);

        StudentResponseDTO responseDTO = studentService.addStudent(studentRequestDTO);

        assertEquals(studentResponseDTO, responseDTO);
        verify(studentRepository, times(1)).save(studentDAO);
        verify(studentDtoAndDomainMapper, times(1)).toDomain(studentRequestDTO);
        verify(studentDomainAndDaoMapper, times(1)).toEntity(studentDomain);
        verify(schoolRepository, times(1)).findByName(anyString());
        verify(studentDtoAndDomainMapper, times(1)).toDTO(studentDomain);
    }

    @Test
    void testAddStudent_FailedPersistence() {
        when(studentDtoAndDomainMapper.toDomain(studentRequestDTO)).thenReturn(studentDomain);
        when(studentDomainAndDaoMapper.toEntity(studentDomain)).thenReturn(studentDAO);
        when(schoolRepository.findByName(anyString())).thenReturn(null);
        when(schoolRepository.save(any(SchoolDAO.class))).thenReturn(new SchoolDAO());
        when(studentRepository.save(studentDAO)).thenThrow(new RuntimeException());

        assertThrows(StudentPersistenceException.class, () -> studentService.addStudent(studentRequestDTO));

        verify(studentRepository, times(1)).save(studentDAO);
        verify(studentDtoAndDomainMapper, times(1)).toDomain(studentRequestDTO);
        verify(studentDomainAndDaoMapper, times(1)).toEntity(studentDomain);
        verify(schoolRepository, times(1)).findByName(anyString());
        verify(schoolRepository, times(1)).save(any(SchoolDAO.class));
        verify(studentDtoAndDomainMapper, never()).toDTO(studentDomain);
    }

    @Test
    void testUpdateStudent() {
        when(studentDtoAndDomainMapper.toDomain(studentRequestDTO)).thenReturn(studentDomain);
        when(studentDomainAndDaoMapper.toEntity(studentDomain)).thenReturn(studentDAO);
        when(schoolRepository.findByName(anyString())).thenReturn(schoolDAO);
        when(schoolRepository.save(any(SchoolDAO.class))).thenReturn(schoolDAO);
        when(studentRepository.save(studentDAO)).thenReturn(studentDAO);
        when(studentDomainAndDaoMapper.toDomain(studentDAO)).thenReturn(studentDomain);
        when(studentDtoAndDomainMapper.toDTO(studentDomain)).thenReturn(studentResponseDTO);

        StudentResponseDTO responseDTO = studentService.updateStudent("1", studentRequestDTO);

        assertEquals(studentResponseDTO, responseDTO);
        verify(studentRepository, times(1)).save(studentDAO);
        verify(studentDtoAndDomainMapper, times(1)).toDomain(studentRequestDTO);
        verify(studentDomainAndDaoMapper, times(1)).toEntity(studentDomain);
        verify(schoolRepository, times(1)).findByName(anyString());
        verify(schoolRepository, times(1)).save(any(SchoolDAO.class));
        verify(studentDtoAndDomainMapper, times(1)).toDTO(studentDomain);
    }

    @Test
    void testUpdateStudent_FailedPersistence() {
        when(studentDtoAndDomainMapper.toDomain(studentRequestDTO)).thenReturn(studentDomain);
        when(studentDomainAndDaoMapper.toEntity(studentDomain)).thenReturn(studentDAO);
        when(schoolRepository.findByName(anyString())).thenReturn(schoolDAO);
        when(schoolRepository.save(schoolDAO)).thenReturn(schoolDAO);
        when(studentRepository.save(studentDAO)).thenThrow(new RuntimeException());

        assertThrows(StudentPersistenceException.class, () -> studentService.updateStudent("1", studentRequestDTO));

        verify(studentRepository, times(1)).save(studentDAO);
        verify(studentDtoAndDomainMapper, times(1)).toDomain(studentRequestDTO);
        verify(studentDomainAndDaoMapper, times(1)).toEntity(studentDomain);
        verify(schoolRepository, times(1)).findByName(anyString());
        verify(studentDtoAndDomainMapper, never()).toDTO(studentDomain);
    }

    @Test
    void testDeleteStudent() {
        assertDoesNotThrow(() -> studentService.deleteStudent(1L));
        verify(studentRepository, times(1)).deleteById(anyLong());
    }
}

