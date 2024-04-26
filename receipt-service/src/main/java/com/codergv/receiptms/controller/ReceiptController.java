package com.codergv.receiptms.controller;

import com.codergv.receiptms.dto.FeeCollectionDTO;
import com.codergv.receiptms.dto.ReceiptDTO;
import com.codergv.receiptms.exception.NotFoundException;
import com.codergv.receiptms.exception.ReceiptGenerationException;
import com.codergv.receiptms.service.ReceiptService;
import io.swagger.v3.oas.annotations.Hidden;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private static final Logger logger = LoggerFactory.getLogger(ReceiptController.class);
    private final ReceiptService receiptService;

    @Autowired
    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getReceiptByStudentId(@PathVariable String studentId) {
        logger.info("Fetching receipt for studentId: {}", studentId);
        ReceiptDTO receiptDTO =null;
        try {
            receiptDTO = receiptService.getReceiptByStudentId(studentId);
        }catch (WebClientResponseException e){
            logger.error("Error fetching receipt for studentId: {}", studentId, e);
            throw new ReceiptGenerationException("An error occurred while fetching the receipt for studentId: " + studentId);
        }catch (Exception e) {
            logger.error("Error fetching receipt for studentId: {}", studentId, e);
            throw new NotFoundException("An error occurred while fetching the receipt for studentId: " + studentId);
        }
        return ResponseEntity.ok(receiptDTO);
    }

    @GetMapping("/reference/{referenceNo}")
    public ResponseEntity<ReceiptDTO> getReceiptByReferenceNumber(@PathVariable String referenceNo) {
        logger.info("Fetching receipt for reference number: {}", referenceNo);
        return receiptService.getReceiptByReferenceNumber(referenceNo)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> {
                    logger.error("Receipt not found with referenceNo: {}", referenceNo);
                    return new NotFoundException("Receipt not found with referenceNo: " + referenceNo);
                });
    }

    /*This api is used internally by fee-collection-service*/
    @PostMapping("/fee/collect/receipt")
    @Hidden
    public ResponseEntity<?> generateReceipt(@RequestBody FeeCollectionDTO feeCollectionDTO) {
        logger.info("Generating receipt for studentId: {}", feeCollectionDTO.getStudentId());
        ReceiptDTO receiptDTO =null;
        try {
            receiptDTO = receiptService.generateReceipt(feeCollectionDTO);
        }catch (WebClientResponseException e){
            logger.error("An error occurred while generating the receipt for studentId: {}", feeCollectionDTO.getStudentId(), e);
            throw new ReceiptGenerationException("An error occurred while generating the receipt for studentId: " + feeCollectionDTO.getStudentId());
        }catch (Exception e) {
            logger.error("An error occurred while generating the receipt for studentId: {}", feeCollectionDTO.getStudentId(), e);
            throw new NotFoundException("An error occurred while generating the receipt for studentId: " + feeCollectionDTO.getStudentId());
        }
        return ResponseEntity.ok(receiptDTO);
    }

}
