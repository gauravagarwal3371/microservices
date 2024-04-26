package com.codergv.receiptms.controller;

import com.codergv.receiptms.dto.FeeCollectionDTO;
import com.codergv.receiptms.dto.ReceiptDTO;
import com.codergv.receiptms.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
        }catch (Exception e){
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while generating the receipt.");
        }
        if (receiptDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(receiptDTO);
    }

    @GetMapping("/reference/{referenceNo}")
    public ResponseEntity<ReceiptDTO> getReceiptByReferenceNumber(@PathVariable String referenceNo) {

        return receiptService.getReceiptByReferenceNumber(referenceNo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping("/fee/collect/receipt")
    public ResponseEntity<?> generateReceipt(@RequestBody FeeCollectionDTO feeCollectionDTO) {
        ReceiptDTO receiptDTO =null;
        try {
            receiptDTO = receiptService.generateReceipt(feeCollectionDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while generating the receipt.");
        }
        if (receiptDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(receiptDTO);
    }

}
