package com.codergv.receiptms.controller;

import com.codergv.receiptms.dto.FeeCollectionDTO;
import com.codergv.receiptms.dto.ReceiptDTO;
import com.codergv.receiptms.exception.NotFoundException;
import com.codergv.receiptms.exception.ReceiptGenerationException;
import com.codergv.receiptms.service.ReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ReceiptControllerTest {

    @Mock
    private ReceiptService receiptService;

    @InjectMocks
    private ReceiptController receiptController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetReceiptByStudentId_Success() {
        String studentId = "123";
        ReceiptDTO expectedReceiptDTO = new ReceiptDTO();
        when(receiptService.getReceiptByStudentId(studentId)).thenReturn(expectedReceiptDTO);

        ResponseEntity<?> responseEntity = receiptController.getReceiptByStudentId(studentId);

        assertEquals(expectedReceiptDTO, responseEntity.getBody());
    }

    @Test
    void testGetReceiptByStudentId_NotFound() {
        String studentId = "123";
        when(receiptService.getReceiptByStudentId(studentId)).thenThrow(NotFoundException.class);

        assertThrows(NotFoundException.class, () -> receiptController.getReceiptByStudentId(studentId));
    }

    @Test
    void testGetReceiptByStudentId_ReceiptGenerationError() {
        String studentId = "123";
        when(receiptService.getReceiptByStudentId(studentId)).thenThrow(WebClientResponseException.class);

        assertThrows(ReceiptGenerationException.class, () -> receiptController.getReceiptByStudentId(studentId));
    }

    @Test
    void testGetReceiptByReferenceNumber_Success() {
        String referenceNo = "REF123";
        ReceiptDTO expectedReceiptDTO = new ReceiptDTO();
        when(receiptService.getReceiptByReferenceNumber(referenceNo)).thenReturn(Optional.of(expectedReceiptDTO));

        ResponseEntity<ReceiptDTO> responseEntity = receiptController.getReceiptByReferenceNumber(referenceNo);

        assertEquals(expectedReceiptDTO, responseEntity.getBody());
    }

    @Test
    void testGetReceiptByReferenceNumber_NotFound() {
        String referenceNo = "REF123";
        when(receiptService.getReceiptByReferenceNumber(referenceNo)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> receiptController.getReceiptByReferenceNumber(referenceNo));
    }

    @Test
    void testGenerateReceipt_Success() {
        FeeCollectionDTO feeCollectionDTO = new FeeCollectionDTO();
        ReceiptDTO expectedReceiptDTO = new ReceiptDTO();
        when(receiptService.generateReceipt(feeCollectionDTO)).thenReturn(expectedReceiptDTO);

        ResponseEntity<?> responseEntity = receiptController.generateReceipt(feeCollectionDTO);

        assertEquals(expectedReceiptDTO, responseEntity.getBody());
    }

    @Test
    void testGenerateReceipt_ReceiptGenerationError() {
        FeeCollectionDTO feeCollectionDTO = new FeeCollectionDTO();
        when(receiptService.generateReceipt(feeCollectionDTO)).thenThrow(WebClientResponseException.class);

        assertThrows(ReceiptGenerationException.class, () -> receiptController.generateReceipt(feeCollectionDTO));
    }
}
