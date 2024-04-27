package com.codergv.feecollms.service;

import com.codergv.feecollms.client.ReceiptClient;
import com.codergv.feecollms.domain.FeeCollectionDomain;
import com.codergv.feecollms.dto.FeeCollectionRequestDTO;
import com.codergv.feecollms.dto.FeeCollectionResponseDTO;
import com.codergv.feecollms.entity.FeeCollectionDAO;
import com.codergv.feecollms.mapper.FeeDomainAndDaoMapper;
import com.codergv.feecollms.mapper.FeeDtoAndDomainMapper;
import com.codergv.feecollms.repository.FeeCollectionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeeCollectionServiceTest {

    @Mock
    private FeeCollectionRepository feeCollectionRepository;

    @Mock
    private FeeDtoAndDomainMapper feeDtoAndDomainMapper;

    @Mock
    private FeeDomainAndDaoMapper feeDomainAndDaoMapper;

    @Mock
    private ReceiptClient receiptClient;

    @InjectMocks
    private FeeCollectionService feeCollectionService;

    private FeeCollectionRequestDTO feeCollectionRequestDTO;
    private FeeCollectionDAO feeCollectionDAO;
    private FeeCollectionResponseDTO feeCollectionResponseDTO;

    private FeeCollectionDomain feeCollectionDomain;

    @BeforeEach
    void setUp() {
        feeCollectionRequestDTO = new FeeCollectionRequestDTO();
        feeCollectionRequestDTO.setStudentId("1");
        feeCollectionDAO = new FeeCollectionDAO();
        feeCollectionDAO.setStudentId("1");
        feeCollectionResponseDTO = new FeeCollectionResponseDTO();
        feeCollectionResponseDTO.setStudentId("1");
        feeCollectionDomain = new FeeCollectionDomain();
        feeCollectionDomain.setStudentId(1l);
    }

    @Test
    void testCollectFeeAndGenerateReceipt_Success() {
        when(feeDtoAndDomainMapper.toDomain(feeCollectionRequestDTO)).thenReturn(feeCollectionDomain);
        when(feeCollectionRepository.save(any())).thenReturn(feeCollectionDAO);
        when(feeDomainAndDaoMapper.toDomain(feeCollectionDAO)).thenReturn(feeCollectionDomain);
        when(feeDtoAndDomainMapper.toDTO(feeCollectionDomain)).thenReturn(feeCollectionResponseDTO);
        when(receiptClient.generateReceipt(any())).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> response = feeCollectionService.collectFeeAndGenerateReceipt(feeCollectionRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testCollectFeeAndGenerateReceipt_Retryable() {
        when(feeDtoAndDomainMapper.toDomain(feeCollectionRequestDTO)).thenReturn(feeCollectionDomain);
        when(feeCollectionRepository.save(any())).thenReturn(feeCollectionDAO);
        when(feeDomainAndDaoMapper.toDomain(feeCollectionDAO)).thenReturn(feeCollectionDomain);
        when(feeDtoAndDomainMapper.toDTO(feeCollectionDomain)).thenReturn(feeCollectionResponseDTO);
        when(receiptClient.generateReceipt(any())).thenThrow(new RuntimeException());

        assertThrows(RuntimeException.class, () -> feeCollectionService.collectFeeAndGenerateReceipt(feeCollectionRequestDTO));
    }

    @Test
    void testGetCollectedFeeByStudentId_Success() {
        when(feeCollectionRepository.findByStudentId(any())).thenReturn(Optional.of(Collections.singletonList(feeCollectionDAO)));
        when(feeDomainAndDaoMapper.toDomain(anyList())).thenReturn(Collections.singletonList(feeCollectionDomain));
        when(feeDtoAndDomainMapper.toDTO(anyList())).thenReturn(Collections.singletonList(feeCollectionResponseDTO));

        Optional<List<FeeCollectionResponseDTO>> response = feeCollectionService.getCollectedFeeByStudentId("123");

        assertTrue(response.isPresent());
        assertFalse(response.get().isEmpty());
    }

    @Test
    void testGetCollectedFeeByStudentId_EmptyResult() {
        when(feeCollectionRepository.findByStudentId(any())).thenReturn(Optional.empty());

        Optional<List<FeeCollectionResponseDTO>> response = feeCollectionService.getCollectedFeeByStudentId("123");

        assertFalse(response.isPresent());
    }

}

