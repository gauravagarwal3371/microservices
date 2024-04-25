package com.codergv.studentms.mapper;

import com.codergv.studentms.domain.StudentDomain;
import com.codergv.studentms.entity.StudentDAO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentDomainAndDaoMapper {
    @Mapping(source = "studentId",target = "id")
    StudentDAO toEntity(StudentDomain studentDomain);
    @Mapping(source = "id",target = "studentId")
    StudentDomain toDomain(StudentDAO studentDAO);

    @Mapping(source = "id",target = "studentId")
    List<StudentDomain> toDomain(List<StudentDAO> studentDAOList);
}
