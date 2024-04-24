package com.codergv.receiptms.controller;

import com.codergv.receiptms.entity.Receipt;
import com.codergv.receiptms.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    @Autowired
    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @PostMapping
    public Receipt generateReceipt(@RequestParam Long studentId) {
        return receiptService.generateReceipt(studentId);
    }

    @GetMapping("/{receiptId}")
    public Receipt getReceiptById(@PathVariable Long receiptId) {
        return receiptService.getReceiptById(receiptId);
    }

}
