package com.codergv.receiptms.controller;

import com.codergv.receiptms.dto.FeeCollectionDTO;
import com.codergv.receiptms.dto.ReceiptDTO;
import com.codergv.receiptms.exception.NotFoundException;
import com.codergv.receiptms.exception.ReceiptGenerationException;
import com.codergv.receiptms.service.ReceiptService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    @Autowired
    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<?> getReceiptByStudentId(@PathVariable String studentId) {
        ReceiptDTO receiptDTO =null;
        try {
            receiptDTO = receiptService.getReceiptByStudentId(studentId);
        }catch (WebClientResponseException e){
            throw new ReceiptGenerationException("An error occurred while fetching the receipt for studentId: " + studentId);
        }catch (Exception e) {
            throw new NotFoundException("An error occurred while fetching the receipt for studentId: " + studentId);
        }
        return ResponseEntity.ok(receiptDTO);
    }

    @GetMapping("/reference/{referenceNo}")
    public ResponseEntity<ReceiptDTO> getReceiptByReferenceNumber(@PathVariable String referenceNo) {

        return receiptService.getReceiptByReferenceNumber(referenceNo)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundException("Receipt not found with referenceNo: " + referenceNo));
    }


    @PostMapping("/fee/collect/receipt")
    @Hidden
    public ResponseEntity<?> generateReceipt(@RequestBody FeeCollectionDTO feeCollectionDTO) {
        ReceiptDTO receiptDTO =null;
        try {
            receiptDTO = receiptService.generateReceipt(feeCollectionDTO);
        }catch (WebClientResponseException e){
            throw new ReceiptGenerationException("An error occurred while generating the receipt for studentId: " + feeCollectionDTO.getStudentId());
        }catch (Exception e) {
            throw new NotFoundException("An error occurred while generating the receipt for studentId: " + feeCollectionDTO.getStudentId());
        }
        return ResponseEntity.ok(receiptDTO);
    }

}
