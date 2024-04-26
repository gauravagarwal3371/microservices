package com.codergv.feecollms.service;


import com.codergv.feecollms.client.ReceiptClient;
import com.codergv.feecollms.domain.FeeCollectionDomain;
import com.codergv.feecollms.dto.FeeCollectionRequestDTO;
import com.codergv.feecollms.dto.FeeCollectionResponseDTO;
import com.codergv.feecollms.entity.FeeCollectionDAO;
import com.codergv.feecollms.mapper.FeeDomainAndDaoMapper;
import com.codergv.feecollms.mapper.FeeDtoAndDomainMapper;
import com.codergv.feecollms.repository.FeeCollectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class FeeCollectionService {
    private static final Logger logger = LoggerFactory.getLogger(FeeCollectionService.class);
    private final FeeCollectionRepository feeCollectionRepository;

    private final FeeDtoAndDomainMapper feeDtoAndDomainMapper;

    private final FeeDomainAndDaoMapper feeDomainAndDaoMapper;

    private final ReceiptClient receiptClient;

    private final AtomicBoolean shouldExecuteGenerateReceipt = new AtomicBoolean(true);

    @Autowired
    public FeeCollectionService(FeeCollectionRepository feeCollectionRepository,FeeDtoAndDomainMapper feeDtoAndDomainMapper,
                                FeeDomainAndDaoMapper feeDomainAndDaoMapper,ReceiptClient receiptClient) {
        this.feeCollectionRepository = feeCollectionRepository;
        this.feeDtoAndDomainMapper=feeDtoAndDomainMapper;
        this.feeDomainAndDaoMapper=feeDomainAndDaoMapper;
        this.receiptClient = receiptClient;
    }

    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ResponseEntity<?> collectFeeAndGenerateReceipt(FeeCollectionRequestDTO feeCollectionRequestDTO) {
        logger.info("Collecting fee and generating receipt for student: {}", feeCollectionRequestDTO.getStudentId());
        FeeCollectionDomain feeCollectionDomain = null;
        if(shouldExecuteGenerateReceipt.get()) {
            feeCollectionDomain = feeDtoAndDomainMapper.toDomain(feeCollectionRequestDTO);
            FeeCollectionDAO feeCollectionDAO = feeCollectionRepository.save(feeDomainAndDaoMapper.toEntity(feeCollectionDomain));
            feeCollectionDomain = feeDomainAndDaoMapper.toDomain(feeCollectionDAO);
        }
        shouldExecuteGenerateReceipt.set(false);
        ResponseEntity<?> receiptResponseEntity =  generateReceipt(feeDtoAndDomainMapper.toDTO(feeCollectionDomain));
        logger.info("Receipt generated successfully for student: {}", feeCollectionRequestDTO.getStudentId());
        return receiptResponseEntity;
    }
    public void setShouldExecuteGenerateReceipt(boolean shouldExecute) {
        shouldExecuteGenerateReceipt.set(shouldExecute);
    }
    private ResponseEntity<?> generateReceipt(FeeCollectionResponseDTO feeCollectionResponseDTO) {
        logger.info("Generating receipt for student: {}", feeCollectionResponseDTO.getStudentId());
       return receiptClient.generateReceipt(feeCollectionResponseDTO);
    }

    public Optional<List<FeeCollectionResponseDTO>> getCollectedFeeByStudentId(String studentId){
        logger.info("Fetching collected fee for student: {}", studentId);
        Optional<List<FeeCollectionDAO>> feeCollectionDAO = feeCollectionRepository.findByStudentId(studentId);
        return feeCollectionDAO.map(feeDomainAndDaoMapper::toDomain)
                .map(feeDtoAndDomainMapper::toDTO);
    }
}
