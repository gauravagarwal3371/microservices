package com.codergv.studentms.mapper;

import com.codergv.studentms.domain.SchoolDomain;
import com.codergv.studentms.entity.SchoolDAO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SchoolDomainAndDaoMapper {
    SchoolDAO domainToEntity(SchoolDomain schoolDomain);
    SchoolDomain entityToDomain(SchoolDAO entity);
}
