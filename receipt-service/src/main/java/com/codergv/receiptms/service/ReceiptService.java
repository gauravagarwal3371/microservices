package com.codergv.receiptms.service;

import com.codergv.receiptms.client.FeeCollectionClient;
import com.codergv.receiptms.client.StudentClient;
import com.codergv.receiptms.domain.ReceiptDomain;
import com.codergv.receiptms.dto.FeeCollectionDTO;
import com.codergv.receiptms.dto.ReceiptDTO;
import com.codergv.receiptms.dto.StudentDTO;
import com.codergv.receiptms.entity.ReceiptDAO;
import com.codergv.receiptms.mapper.ReceiptDomainAndDaoMapper;
import com.codergv.receiptms.mapper.ReceiptDtoAndDomainMapper;
import com.codergv.receiptms.mapper.ReceiptDtoMapper;
import com.codergv.receiptms.repository.ReceiptRepository;
import com.codergv.receiptms.util.ReferenceNumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final ReceiptDtoMapper receiptDtoMapper;
    private final ReceiptDtoAndDomainMapper receiptDtoAndDomainMapper;
    private final ReceiptDomainAndDaoMapper receiptDomainAndDaoMapper;
    private final StudentClient studentClient;
    private final FeeCollectionClient feeCollectionClient;

    @Autowired
    public ReceiptService(ReceiptRepository receiptRepository, ReceiptDtoMapper receiptDtoMapper,
                          ReceiptDtoAndDomainMapper receiptDtoAndDomainMapper, ReceiptDomainAndDaoMapper receiptDomainAndDaoMapper,
                          StudentClient studentClient, FeeCollectionClient feeCollectionClient) {
        this.receiptRepository = receiptRepository;
        this.receiptDtoMapper = receiptDtoMapper;
        this.receiptDtoAndDomainMapper = receiptDtoAndDomainMapper;
        this.receiptDomainAndDaoMapper = receiptDomainAndDaoMapper;
        this.studentClient = studentClient;
        this.feeCollectionClient = feeCollectionClient;
    }


    public StudentDTO getStudentByIdWithRetry(String studentId) {
        return studentClient.getStudentById(studentId).getBody();
    }

    public FeeCollectionDTO getCollectedFeeByStudentIdWithRetry(String studentId) {
        return feeCollectionClient.getCollectedFeeByStudentId(studentId).getBody();
    }
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ReceiptDTO getReceiptByStudentId(String studentId) {
        StudentDTO studentDTO = getStudentByIdWithRetry(studentId);
        FeeCollectionDTO feeCollectionDTO = getCollectedFeeByStudentIdWithRetry(studentId);

        ReceiptDTO receiptDTO = receiptDtoMapper.receiptDTO(studentDTO, feeCollectionDTO);
        ReceiptDomain receiptDomain = receiptDtoAndDomainMapper.toDomain(receiptDTO);
        receiptDomain.setReference(ReferenceNumberUtil.getRandomReferenceNumber());
        ReceiptDAO receiptDAO = receiptDomainAndDaoMapper.toEntity(receiptDomain);

        receiptDAO = receiptRepository.save(receiptDAO);
        return receiptDtoAndDomainMapper.toDTO(receiptDomainAndDaoMapper.toDomain(receiptDAO));
    }

    public Optional<ReceiptDTO> getReceiptByReferenceNumber(String referenceNo) {
        return receiptRepository.findByReference(referenceNo)
                .map(receiptDomainAndDaoMapper::toDomain)
                .map(receiptDtoAndDomainMapper::toDTO);
    }

    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ReceiptDTO generateReceipt(FeeCollectionDTO feeCollectionDTO) {
        StudentDTO studentDTO = getStudentByIdWithRetry(feeCollectionDTO.getStudentId());

        ReceiptDTO receiptDTO = receiptDtoMapper.receiptDTO(studentDTO, feeCollectionDTO);
        ReceiptDomain receiptDomain = receiptDtoAndDomainMapper.toDomain(receiptDTO);
        receiptDomain.setReference(ReferenceNumberUtil.getRandomReferenceNumber());
        ReceiptDAO receiptDAO = receiptDomainAndDaoMapper.toEntity(receiptDomain);

        receiptDAO = receiptRepository.save(receiptDAO);
        return receiptDtoAndDomainMapper.toDTO(receiptDomainAndDaoMapper.toDomain(receiptDAO));
    }
}