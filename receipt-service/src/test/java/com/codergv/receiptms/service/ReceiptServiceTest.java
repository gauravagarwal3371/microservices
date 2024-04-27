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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ReceiptServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private ReceiptDtoMapper receiptDtoMapper;

    @Mock
    private ReceiptDtoAndDomainMapper receiptDtoAndDomainMapper;

    @Mock
    private ReceiptDomainAndDaoMapper receiptDomainAndDaoMapper;

    @Mock
    private StudentClient studentClient;

    @Mock
    private FeeCollectionClient feeCollectionClient;

    @InjectMocks
    private ReceiptService receiptService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetStudentByIdWithRetry() {
        String studentId = "123";
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setStudentId("1");

        ResponseEntity<StudentDTO> studentResponseEntity = ResponseEntity.ok(studentDTO);
        when(studentClient.getStudentById(studentId)).thenReturn(studentResponseEntity);

        assertEquals(studentDTO, receiptService.getStudentByIdWithRetry(studentId));
    }

    @Test
    void testGetCollectedFeeByStudentIdWithRetry() {
        String studentId = "123";
        FeeCollectionDTO feeCollectionDTO = new FeeCollectionDTO();
        feeCollectionDTO.setStudentId("123");
        feeCollectionDTO.setTimestamp(LocalDateTime.now());
        List<FeeCollectionDTO> feeCollectionDTOS = new LinkedList<>();
        feeCollectionDTOS.add(feeCollectionDTO);

        ResponseEntity<List<FeeCollectionDTO>> feeCollectionResponseEntity = ResponseEntity.ok(feeCollectionDTOS);
        when(feeCollectionClient.getCollectedFeeByStudentId(studentId)).thenReturn(feeCollectionResponseEntity);

        assertEquals(feeCollectionDTOS, receiptService.getCollectedFeeByStudentIdWithRetry(studentId));
    }

    @Test
    void testGetReceiptByStudentId_Success() {
        String studentId = "123";
        StudentDTO studentDTO = new StudentDTO();
        studentDTO.setStudentId("123");
        FeeCollectionDTO feeCollectionDTO = new FeeCollectionDTO();
        feeCollectionDTO.setStudentId("123");
        ReceiptDTO receiptDTO = new ReceiptDTO();
        receiptDTO.setStudentId("123");
        ReceiptDomain receiptDomain = new ReceiptDomain();
        receiptDomain.setStudentId("123");
        ReceiptDAO receiptDAO = new ReceiptDAO();
        receiptDAO.setStudentId("123");
        feeCollectionDTO.setTimestamp(LocalDateTime.now());
        List<FeeCollectionDTO> feeCollectionDTOS = new LinkedList<>();
        feeCollectionDTOS.add(feeCollectionDTO);

        ResponseEntity<StudentDTO> studentResponseEntity = ResponseEntity.ok(studentDTO);
        when(studentClient.getStudentById(studentId)).thenReturn(studentResponseEntity);

        // Mocking the behavior of feeCollectionClient.getCollectedFeeByStudentId(studentId)
        ResponseEntity<List<FeeCollectionDTO>> feeCollectionResponseEntity = ResponseEntity.ok(feeCollectionDTOS);
        when(feeCollectionClient.getCollectedFeeByStudentId(studentId)).thenReturn(feeCollectionResponseEntity);
        when(receiptDtoAndDomainMapper.toDomain(any())).thenReturn(receiptDomain);
//        when(ReferenceNumberUtil.getRandomReferenceNumber()).thenReturn(UUID.randomUUID().toString());
        when(receiptDomainAndDaoMapper.toEntity(receiptDomain)).thenReturn(receiptDAO);
        when(receiptRepository.save(receiptDAO)).thenReturn(receiptDAO);
        when(receiptDtoAndDomainMapper.toDTO(any())).thenReturn(receiptDTO);
        // Mocking other method calls as before...

        assertEquals(receiptDTO, receiptService.getReceiptByStudentId(studentId));
    }

    @Test
    void testGetReceiptByReferenceNumber_Success() {
        String referenceNo = "REF123";
        ReceiptDAO receiptDAO = new ReceiptDAO();
        ReceiptDomain receiptDomain = new ReceiptDomain();
        ReceiptDTO receiptDTO = new ReceiptDTO();

        when(receiptRepository.findByReference(referenceNo)).thenReturn(Optional.of(receiptDAO));
        when(receiptDomainAndDaoMapper.toDomain(receiptDAO)).thenReturn(receiptDomain);
        when(receiptDtoAndDomainMapper.toDTO(receiptDomain)).thenReturn(receiptDTO);

        assertEquals(Optional.of(receiptDTO), receiptService.getReceiptByReferenceNumber(referenceNo));
    }

    @Test
    void testGetReceiptByReferenceNumber_NotFound() {
        String referenceNo = "REF123";

        when(receiptRepository.findByReference(referenceNo)).thenReturn(Optional.empty());

        assertEquals(Optional.empty(), receiptService.getReceiptByReferenceNumber(referenceNo));
    }

    @Test
    void testGenerateReceipt_Success() {
        FeeCollectionDTO feeCollectionDTO = new FeeCollectionDTO();
        feeCollectionDTO.setStudentId("123");
        String studentId="123";
        StudentDTO studentDTO = new StudentDTO();
        ReceiptDTO receiptDTO = new ReceiptDTO();
        ReceiptDomain receiptDomain = new ReceiptDomain();
        ReceiptDAO receiptDAO = new ReceiptDAO();
        feeCollectionDTO.setTimestamp(LocalDateTime.now());
        List<FeeCollectionDTO> feeCollectionDTOS = new LinkedList<>();
        feeCollectionDTOS.add(feeCollectionDTO);

        ResponseEntity<StudentDTO> studentResponseEntity = ResponseEntity.ok(studentDTO);
        when(studentClient.getStudentById(studentId)).thenReturn(studentResponseEntity);

        ResponseEntity<List<FeeCollectionDTO>> feeCollectionResponseEntity = ResponseEntity.ok(feeCollectionDTOS);
        when(feeCollectionClient.getCollectedFeeByStudentId(studentId)).thenReturn(feeCollectionResponseEntity);
        when( receiptDtoAndDomainMapper.toDomain(any())).thenReturn(receiptDomain);
        when(receiptDomainAndDaoMapper.toEntity(any())).thenReturn(receiptDAO);
        when(receiptRepository.save(any())).thenReturn(receiptDAO);
        when(receiptDtoAndDomainMapper.toDTO(any())).thenReturn(receiptDTO);

        assertEquals(receiptDTO, receiptService.generateReceipt(feeCollectionDTO));
    }
}

