package com.codergv.studentms.service;

import com.codergv.studentms.domain.StudentDomain;
import com.codergv.studentms.dto.StudentRequestDTO;
import com.codergv.studentms.dto.StudentResponseDTO;
import com.codergv.studentms.entity.SchoolDAO;
import com.codergv.studentms.entity.StudentDAO;
import com.codergv.studentms.mapper.StudentDomainAndDaoMapper;
import com.codergv.studentms.mapper.StudentDtoAndDomainMapper;
import com.codergv.studentms.repository.SchoolRepository;
import com.codergv.studentms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    private final StudentDtoAndDomainMapper studentDtoAndDomainMapper;

    private final StudentDomainAndDaoMapper studentDomainAndDaoMapper;

    private final SchoolRepository schoolRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, StudentDtoAndDomainMapper studentDtoAndDomainMapper,
                          StudentDomainAndDaoMapper studentDomainAndDaoMapper,SchoolRepository schoolRepository) {
        this.studentRepository = studentRepository;
        this.studentDtoAndDomainMapper=studentDtoAndDomainMapper;
        this.studentDomainAndDaoMapper=studentDomainAndDaoMapper;
        this.schoolRepository=schoolRepository;
    }

    public List<StudentResponseDTO> getAllStudents() {
        List<StudentDAO> studentDAOList = studentRepository.findAll();
        List<StudentDomain> studentDomainList = studentDomainAndDaoMapper.toDomain(studentDAOList);
        return studentDtoAndDomainMapper.toDTO(studentDomainList);
    }

    public Optional<StudentResponseDTO> getStudentById(String id) {
        Optional<StudentDAO> studentDAO = studentRepository.findById(Long.valueOf(id));
        return studentDAO.map(studentDomainAndDaoMapper::toDomain)
                .map(studentDtoAndDomainMapper::toDTO);
    }

    public StudentResponseDTO addStudent(StudentRequestDTO studentRequestDTO) {
        StudentDomain studentDomain = studentDtoAndDomainMapper.toDomain(studentRequestDTO);

        studentDomain.performBusinessLogic(studentDomain);

        StudentDAO studentDao = studentDomainAndDaoMapper.toEntity(studentDomain);
        SchoolDAO schoolDAO = schoolRepository.findByName(studentDao.getSchool().getName());
        if(schoolDAO!=null){
            studentDao.setSchool(schoolDAO);
        }
        else{
            schoolDAO=schoolRepository.save(studentDao.getSchool());
            studentDao.setSchool(schoolDAO);
        }
        studentDao = studentRepository.save(studentDao);

        studentDomain = studentDomainAndDaoMapper.toDomain(studentDao);

        studentDomain.performBusinessLogic1(studentDomain);

        return studentDtoAndDomainMapper.toDTO(studentDomain);
    }

    public StudentResponseDTO updateStudent(String id, StudentRequestDTO studentRequestDTO) {
        StudentDomain studentDomain = studentDtoAndDomainMapper.toDomain(studentRequestDTO);
        studentDomain.setStudentId(id);
        studentDomain.performBusinessLogic(studentDomain);
        StudentDAO studentDao = studentDomainAndDaoMapper.toEntity(studentDomain);
        SchoolDAO schoolDAO = schoolRepository.findByName(studentDao.getSchool().getName());
        if(studentDao.getId()!=null){
            studentDao.setSchool(schoolDAO);
        }
        else{
            schoolDAO=schoolRepository.save(studentDao.getSchool());
            studentDao.setSchool(schoolDAO);
        }
        studentDao = studentRepository.save(studentDao);
        studentDomain = studentDomainAndDaoMapper.toDomain(studentDao);
        studentDomain.performBusinessLogic1(studentDomain);

        return studentDtoAndDomainMapper.toDTO(studentDomain);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
}
