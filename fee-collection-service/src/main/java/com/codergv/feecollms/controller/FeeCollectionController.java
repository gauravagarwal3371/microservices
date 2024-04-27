package com.codergv.feecollms.controller;

import com.codergv.feecollms.dto.FeeCollectionRequestDTO;
import com.codergv.feecollms.dto.FeeCollectionResponseDTO;
import com.codergv.feecollms.exception.FeeCollectionException;
import com.codergv.feecollms.exception.NotFoundException;
import com.codergv.feecollms.service.FeeCollectionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;

@RestController
@RequestMapping("/fees")
public class FeeCollectionController {

    private static final Logger logger = LoggerFactory.getLogger(FeeCollectionController.class);
    private final FeeCollectionService feeCollectionService;

    @Autowired
    public FeeCollectionController(FeeCollectionService feeCollectionService) {
        this.feeCollectionService = feeCollectionService;
    }

    @PostMapping("/collect")
    public ResponseEntity<?> collectFeeAndGenerateReceipt(@RequestBody @Valid FeeCollectionRequestDTO feeCollectionRequestDTO) {
        logger.info("Collect fee and generate receipt for studentId: {}", feeCollectionRequestDTO.getStudentId());
        ResponseEntity<?> receiptResponseEntity = null;
        try {
            feeCollectionService.setShouldExecuteGenerateReceipt(true);
            receiptResponseEntity = feeCollectionService.collectFeeAndGenerateReceipt(feeCollectionRequestDTO);
        }catch (WebClientResponseException e){
            if(e.getStatusCode().is4xxClientError()){
                logger.error("Student not found with the given studentId: {}", feeCollectionRequestDTO.getStudentId());
                throw new NotFoundException("Student not found with the given studentId: " + feeCollectionRequestDTO.getStudentId());
            }
            logger.error("Error while collecting fee and generating receipt: {}", e.getMessage());
            throw new FeeCollectionException("Transaction successful, but receipt not generated. " +
                    "Please fetch from the app in some time");
        }
        if (receiptResponseEntity.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(receiptResponseEntity.getBody());
        }
        logger.error("Error while collecting fee for studentId: {}", feeCollectionRequestDTO.getStudentId());
        throw new FeeCollectionException("Fees not collected for studentId: " + feeCollectionRequestDTO.getStudentId());
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<List<FeeCollectionResponseDTO>> getCollectedFeeByStudentId(@PathVariable String studentId) {
        logger.info("Fetching collected fee for studentId: {}", studentId);
        return feeCollectionService.getCollectedFeeByStudentId(studentId).map(ResponseEntity::ok)
                .orElseThrow(() -> {
                    logger.error("No fee collected for studentId: {}", studentId);
                    return new NotFoundException("Fees not collected for studentId: " + studentId);
                });
    }
}
