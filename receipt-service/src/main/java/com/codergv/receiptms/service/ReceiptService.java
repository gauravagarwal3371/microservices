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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ReceiptService {
    private static final Logger logger = LoggerFactory.getLogger(ReceiptService.class);
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
        logger.info("Fetching student from getStudentByIdWithRetry for studentId: {}", studentId);
        return studentClient.getStudentById(studentId).getBody();
    }

    public List<FeeCollectionDTO> getCollectedFeeByStudentIdWithRetry(String studentId) {
        logger.info("Fetching fee collection from getCollectedFeeByStudentIdWithRetry for studentId: {}", studentId);
        return feeCollectionClient.getCollectedFeeByStudentId(studentId).getBody();
    }
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ReceiptDTO getReceiptByStudentId(String studentId) {
        logger.info("Fetching receipt for studentId: {}", studentId);
        StudentDTO studentDTO = getStudentByIdWithRetry(studentId);
        List<FeeCollectionDTO> feeCollectionDTO = getCollectedFeeByStudentIdWithRetry(studentId);
        Optional<FeeCollectionDTO> latestFeeCollectionDTO = feeCollectionDTO.stream().sorted(Comparator.comparing(FeeCollectionDTO::getTimestamp).reversed()).findFirst();
        ReceiptDTO receiptDTO = receiptDtoMapper.receiptDTO(studentDTO, latestFeeCollectionDTO.get());
        ReceiptDomain receiptDomain = receiptDtoAndDomainMapper.toDomain(receiptDTO);
        receiptDomain.setReference(ReferenceNumberUtil.getRandomReferenceNumber());
        ReceiptDAO receiptDAO = receiptDomainAndDaoMapper.toEntity(receiptDomain);
        receiptDAO = receiptRepository.save(receiptDAO);
        logger.info("Receipt generated successfully for studentId: {}", studentId);
        return receiptDtoAndDomainMapper.toDTO(receiptDomainAndDaoMapper.toDomain(receiptDAO));
    }

    public Optional<ReceiptDTO> getReceiptByReferenceNumber(String referenceNo) {
        logger.info("Fetching receipt with referenceNo: {}", referenceNo);
        return receiptRepository.findByReference(referenceNo)
                .map(receiptDomainAndDaoMapper::toDomain)
                .map(receiptDtoAndDomainMapper::toDTO);
    }

    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 1000))
    public ReceiptDTO generateReceipt(FeeCollectionDTO feeCollectionDTO) {
        logger.info("Generating receipt using  feeCollectionDTO: {}", feeCollectionDTO);
        StudentDTO studentDTO = getStudentByIdWithRetry(feeCollectionDTO.getStudentId());

        ReceiptDTO receiptDTO = receiptDtoMapper.receiptDTO(studentDTO, feeCollectionDTO);
        ReceiptDomain receiptDomain = receiptDtoAndDomainMapper.toDomain(receiptDTO);
        receiptDomain.setReference(ReferenceNumberUtil.getRandomReferenceNumber());
        ReceiptDAO receiptDAO = receiptDomainAndDaoMapper.toEntity(receiptDomain);

        receiptDAO = receiptRepository.save(receiptDAO);
        logger.info("Receipt generated successfully: {}", receiptDomain);
        return receiptDtoAndDomainMapper.toDTO(receiptDomainAndDaoMapper.toDomain(receiptDAO));
    }
}