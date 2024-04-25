package com.codergv.feecollms.mapper;

import com.codergv.feecollms.domain.FeeCollectionDomain;
import com.codergv.feecollms.entity.FeeCollectionDAO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FeeDomainAndDaoMapper {

    FeeCollectionDAO toEntity(FeeCollectionDomain feeCollectionDomain);
    FeeCollectionDomain toDomain(FeeCollectionDAO feeCollectionDAO);

}
