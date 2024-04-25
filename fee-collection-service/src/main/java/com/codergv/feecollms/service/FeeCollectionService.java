package com.codergv.feecollms.service;


import com.codergv.feecollms.domain.FeeCollectionDomain;
import com.codergv.feecollms.dto.FeeCollectionRequestDTO;
import com.codergv.feecollms.dto.FeeCollectionResponseDTO;
import com.codergv.feecollms.entity.FeeCollectionDAO;
import com.codergv.feecollms.mapper.FeeDomainAndDaoMapper;
import com.codergv.feecollms.mapper.FeeDtoAndDomainMapper;
import com.codergv.feecollms.repository.FeeCollectionRepository;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Service
public class FeeCollectionService {
    private final FeeCollectionRepository feeCollectionRepository;

    private final FeeDtoAndDomainMapper feeDtoAndDomainMapper;

    private final FeeDomainAndDaoMapper feeDomainAndDaoMapper;

    @Autowired
    public FeeCollectionService(FeeCollectionRepository feeCollectionRepository,FeeDtoAndDomainMapper feeDtoAndDomainMapper,
                                FeeDomainAndDaoMapper feeDomainAndDaoMapper) {
        this.feeCollectionRepository = feeCollectionRepository;
        this.feeDtoAndDomainMapper=feeDtoAndDomainMapper;
        this.feeDomainAndDaoMapper=feeDomainAndDaoMapper;
    }

    public FeeCollectionResponseDTO collectFee(FeeCollectionRequestDTO feeCollectionRequestDTO) {

        FeeCollectionDomain feeCollectionDomain = feeDtoAndDomainMapper.toDomain(feeCollectionRequestDTO);
        FeeCollectionDAO feeCollectionDAO = feeCollectionRepository.save(feeDomainAndDaoMapper.toEntity(feeCollectionDomain));
        feeCollectionDomain = feeDomainAndDaoMapper.toDomain(feeCollectionDAO);
        return feeDtoAndDomainMapper.toDTO(feeCollectionDomain);
    }

    public Optional<FeeCollectionResponseDTO> getCollectedFeeByStudentId(String studentId){
        Optional<FeeCollectionDAO> feeCollectionDAO = feeCollectionRepository.findByStudentId(studentId);
        return feeCollectionDAO.map(feeDomainAndDaoMapper::toDomain)
                .map(feeDtoAndDomainMapper::toDTO);
    }
}
