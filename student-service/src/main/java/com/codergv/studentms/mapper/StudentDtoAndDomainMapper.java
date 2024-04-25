package com.codergv.studentms.mapper;

import com.codergv.studentms.domain.StudentDomain;
import com.codergv.studentms.dto.StudentRequestDTO;
import com.codergv.studentms.dto.StudentResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface StudentDtoAndDomainMapper {


    StudentDomain toDomain(StudentResponseDTO studentResponseDTO);
    StudentDomain toDomain(StudentRequestDTO studentRequestDTO);

    StudentResponseDTO toDTO(StudentDomain studentDomain);

    List<StudentResponseDTO> toDTO(List<StudentDomain> studentDomainList);
}
