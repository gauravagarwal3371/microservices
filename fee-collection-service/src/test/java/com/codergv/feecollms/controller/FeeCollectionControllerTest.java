package com.codergv.feecollms.controller;

import com.codergv.feecollms.dto.FeeCollectionRequestDTO;
import com.codergv.feecollms.dto.FeeCollectionResponseDTO;
import com.codergv.feecollms.exception.FeeCollectionException;
import com.codergv.feecollms.exception.NotFoundException;
import com.codergv.feecollms.service.FeeCollectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeeCollectionControllerTest {

    @Mock
    private FeeCollectionService feeCollectionService;

    @InjectMocks
    private FeeCollectionController feeCollectionController;

    private FeeCollectionRequestDTO validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new FeeCollectionRequestDTO();
    }

    @Test
    void testCollectFeeAndGenerateReceipt_Success() {
        ResponseEntity responseEntity = ResponseEntity.ok("Receipt details");
        when(feeCollectionService.collectFeeAndGenerateReceipt(any())).thenReturn(responseEntity);

        ResponseEntity<?> response = feeCollectionController.collectFeeAndGenerateReceipt(validRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCollectFeeAndGenerateReceipt_FeeCollectionException() {
        when(feeCollectionService.collectFeeAndGenerateReceipt(any())).thenThrow(new FeeCollectionException("Error"));

        assertThrows(FeeCollectionException.class, () -> feeCollectionController.collectFeeAndGenerateReceipt(validRequest));
    }

    @Test
    void testCollectFeeAndGenerateReceipt_WebClientResponse5XXException() {
        when(feeCollectionService.collectFeeAndGenerateReceipt(any())).thenThrow(new WebClientResponseException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error", null, null, null));

        assertThrows(FeeCollectionException.class, () -> feeCollectionController.collectFeeAndGenerateReceipt(validRequest));
    }

    @Test
    void testCollectFeeAndGenerateReceipt_WebClientResponse4XXException() {
        when(feeCollectionService.collectFeeAndGenerateReceipt(any())).thenThrow(new WebClientResponseException(HttpStatus.NOT_FOUND.value(), "Error", null, null, null));

        assertThrows(NotFoundException.class, () -> feeCollectionController.collectFeeAndGenerateReceipt(validRequest));
    }

    @Test
    void testGetCollectedFeeByStudentId_Success() {
        when(feeCollectionService.getCollectedFeeByStudentId(anyString())).thenReturn(Optional.of(Collections.singletonList(new FeeCollectionResponseDTO())));

        ResponseEntity<List<FeeCollectionResponseDTO>> response = feeCollectionController.getCollectedFeeByStudentId("123");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetCollectedFeeByStudentId_NotFoundException() {
        when(feeCollectionService.getCollectedFeeByStudentId(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> feeCollectionController.getCollectedFeeByStudentId("123"));
    }

}
