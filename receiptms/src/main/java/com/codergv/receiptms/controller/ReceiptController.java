package com.codergv.receiptms.controller;

import com.codergv.receiptms.dto.ReceiptDTO;
import com.codergv.receiptms.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    @Autowired
    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<ReceiptDTO> generateReceipt(@PathVariable String studentId) {

        ReceiptDTO receiptDTO = receiptService.generateReceipt(studentId);
        if (receiptDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(receiptDTO);
    }

}
