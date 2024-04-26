package com.codergv.feecollms.mapper;

import com.codergv.feecollms.domain.FeeCollectionDomain;
import com.codergv.feecollms.dto.FeeCollectionRequestDTO;
import com.codergv.feecollms.dto.FeeCollectionResponseDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FeeDtoAndDomainMapper {


    FeeCollectionDomain toDomain(FeeCollectionResponseDTO feeCollectionResponseDTO);
    FeeCollectionDomain toDomain(FeeCollectionRequestDTO feeCollectionRequestDTO);

    FeeCollectionResponseDTO toDTO(FeeCollectionDomain feeCollectionDomain);

    List<FeeCollectionResponseDTO> toDTO(List<FeeCollectionDomain> feeCollectionDomain);

}
